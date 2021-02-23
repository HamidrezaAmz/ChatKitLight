package ir.vasl.chatkitlight.ui.callback;

public interface ConversationListListener<T> {

    default void onConversationItemClicked() {
    }

    default void onConversationItemClicked(T t) {
    }

    default void onConversationItemLongClicked() {
    }

    default void onConversationItemLongClicked(T t) {
    }

    default void requestStoragePermission(){

    }

    default void onImageClicked(String url){}

}
