package ir.vasl.chatkitlight.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.databinding.ViewConversationClientBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationEmptyBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationServerBinding;
import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.base.BaseViewHolder;
import ir.vasl.chatkitlight.ui.callback.ConversationListListener;
import ir.vasl.chatkitlight.utils.ConversationDiffCallback;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;

public class ConversationAdapter extends PagedListAdapter<ConversationModel, BaseViewHolder> implements ConversationListListener {

    private ConversationListListener conversationListListener;

    public ConversationAdapter(ConversationListListener conversationListListener) {
        super(new ConversationDiffCallback());
        this.setHasStableIds(true);
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
    public long getItemId(int position) { //todo -> must be unique
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        switch (ConversationType.get(holder.getItemViewType())) {
            case CLIENT: {
                ConversationModel model = getItem(position);
                if (model == null)
                    return;

                holder.onBind(position);
                ((ConversationViewHolder) holder).clientBinding.setConversationModel(model);
                ((ConversationViewHolder) holder).clientBinding.setConversationListListener(this);
                break;
            }
            case SERVER: {
                ConversationModel model = getItem(position);
                if (model == null)
                    return;

                ((ConversationViewHolder) holder).serverBinding.setConversationModel(model);
                ((ConversationViewHolder) holder).serverBinding.setConversationListListener(this);
                break;
            }
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

        if (getCurrentList() == null)
            return ConversationType.EMPTY.getValue();

        if (getCurrentList().snapshot().size() == 0)
            return ConversationType.EMPTY.getValue();

        if (getItem(position) == null)
            return ConversationType.EMPTY.getValue();

        ConversationModel model = getItem(position);
        if (model == null || getItemCount() == 0)
            return ConversationType.EMPTY.getValue();
        return model.getConversationType().getValue();
    }

    @Override
    public int getItemCount() {

        if (getCurrentList() == null || getCurrentList().snapshot().size() == 0)
            return 1;

        return getCurrentList().snapshot().size();
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
