package ir.vasl.chatkitlight.ui.callback;

public interface ConversationListListener<T> {

    default void onConversationItemClicked() {
    }

    default void onConversationItemClicked(T t) {
    }

}
