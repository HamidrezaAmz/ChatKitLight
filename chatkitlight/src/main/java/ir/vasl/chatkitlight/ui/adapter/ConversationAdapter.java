package ir.vasl.chatkitlight.ui.adapter;

import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import ir.vasl.chatkitlight.MyApplication;
import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.data.DBLayer;
import ir.vasl.chatkitlight.databinding.ViewConversationClientBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationEmptyBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationServerBinding;
import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.base.BaseViewHolder;
import ir.vasl.chatkitlight.ui.callback.ConversationListListener;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;

public class ConversationAdapter extends PagedListAdapter<ConversationModel, BaseViewHolder> implements ConversationListListener {

    private ConversationListListener conversationListListener;

    public ConversationAdapter(ConversationListListener conversationListListener) {
        super(new DiffUtil.ItemCallback<ConversationModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull ConversationModel oldItem, @NonNull ConversationModel newItem) {

                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ConversationModel oldConversationModel, @NonNull ConversationModel newConversationModel) {
                Log.e("tag", oldConversationModel.getMessage() + " areStatusesTheSame: " + oldConversationModel.getConversationStatus() + " " + newConversationModel.getConversationStatus() );
                return oldConversationModel.equals(newConversationModel);
            }
        });
        this.conversationListListener = conversationListListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (ConversationType.get(viewType)) {
            case CLIENT:
                ViewConversationClientBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_client, parent, false);
                return new ConversationViewHolder(clientBinding);

            case SERVER:
                ViewConversationServerBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_server, parent, false);
                return new ConversationViewHolder(serverBinding);

            case EMPTY:
            default:
                ViewConversationEmptyBinding emptyBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_empty, parent, false);
                return new ConversationViewHolder(emptyBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Log.e("tag", "onBindViewHolder: " + position);
        ConversationModel model = getItem(position);
        if(model == null)
            return;
        holder.onBind(position);
        // AnimView.animate(holder.itemView, position * 150 + 100, 50);
        switch (ConversationType.get(holder.getItemViewType())) {

            case CLIENT:
                ((ConversationViewHolder) holder).clientBinding.setConversationModel(model);
                ((ConversationViewHolder) holder).clientBinding.setConversationListListener(this);
                break;

            case SERVER:
                ((ConversationViewHolder) holder).serverBinding.setConversationModel(model);
                ((ConversationViewHolder) holder).serverBinding.setConversationListListener(this);
                break;

            case EMPTY:
                ((ConversationViewHolder) holder).emptyBinding.setConversationListListener(this);
                break;

            case UNDEFINE:
                break;
        }
    }

    @Override
    public void submitList(@Nullable PagedList<ConversationModel> pagedList) {
        super.submitList(pagedList);
    }

    @Override
    public int getItemViewType(int position) {
        ConversationModel model = getItem(position);
        if (model == null || getItemCount() == 0)
            return ConversationType.CLIENT.getValue();

        return model.getConversationType().getValue();
    }

    private class ConversationViewHolder extends BaseViewHolder {

        private ViewConversationClientBinding clientBinding;
        private ViewConversationServerBinding serverBinding;
        private ViewConversationEmptyBinding emptyBinding;

        public ConversationViewHolder(@NonNull ViewConversationClientBinding clientBinding) {
            super(clientBinding.getRoot());
            this.clientBinding = clientBinding;
        }

        public ConversationViewHolder(@NonNull ViewConversationServerBinding serverBinding) {
            super(serverBinding.getRoot());
            this.serverBinding = serverBinding;
        }

        public ConversationViewHolder(@NonNull ViewConversationEmptyBinding emptyBinding) {
            super(emptyBinding.getRoot());
            this.emptyBinding = emptyBinding;
        }
    }

    @Override
    public void onConversationItemClicked(Object object) {
        if (conversationListListener != null)
            conversationListListener.onConversationItemClicked(object);
    }
}
