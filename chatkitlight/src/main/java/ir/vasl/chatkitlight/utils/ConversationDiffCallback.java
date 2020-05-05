package ir.vasl.chatkitlight.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import ir.vasl.chatkitlight.model.ConversationModel;

public class ConversationDiffCallback extends DiffUtil.ItemCallback<ConversationModel> {

    @Override
    public boolean areItemsTheSame(@NonNull ConversationModel oldItem, @NonNull ConversationModel newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull ConversationModel oldConversationModel, @NonNull ConversationModel newConversationModel) {
        return oldConversationModel.equals(newConversationModel);
    }
}
