package ir.vasl.chatkitlight.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.vasl.chatkitlight.R;
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
                return TextUtils.equals(newConversationModel.getTitle(), oldConversationModel.getTitle())
                        && TextUtils.equals(newConversationModel.getMessage(), oldConversationModel.getMessage())
                        && newConversationModel.getConversationStatus().getValueStr().equals(oldConversationModel.getConversationStatus().getValueStr())
                        ;
            }
        });
        this.conversationListListener = conversationListListener;
    }

//    public void setConversationModels(List<? extends ConversationModel> conversationModels) {
//        if (mConversationModels == null) {
//            this.mConversationModels = conversationModels;
//            this.notifyItemRangeInserted(0, conversationModels.size());
//        } else {
//            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//                @Override
//                public int getOldListSize() {
//                    return mConversationModels.size();
//                }
//
//                @Override
//                public int getNewListSize() {
//                    return conversationModels.size();
//                }
//
//                @Override
//                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                    return mConversationModels.get(oldItemPosition).getId().equals(conversationModels.get(newItemPosition).getId());
//                }
//
//                @Override
//                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                    ConversationModel newConversationModel = conversationModels.get(newItemPosition);
//                    ConversationModel oldConversationModel = mConversationModels.get(oldItemPosition);
//
//                    return TextUtils.equals(newConversationModel.getTitle(), oldConversationModel.getTitle())
//                            && TextUtils.equals(newConversationModel.getMessage(), oldConversationModel.getMessage())
//                            && newConversationModel.getConversationStatus().getValueStr().equals(oldConversationModel.getConversationStatus().getValueStr())
//                            ;
//                }
//            });
//
//            result.dispatchUpdatesTo(this);
//            mConversationModels = conversationModels;
//        }
//    }

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
