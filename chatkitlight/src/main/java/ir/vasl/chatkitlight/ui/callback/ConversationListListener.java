package ir.vasl.chatkitlight.ui.callback;

import android.net.Uri;

public interface ConversationListListener<T> {

    default void onConversationItemClicked() {
    }

    default void onConversationItemClicked(T t) {
    }

    default void onConversationItemLongClicked() {
    }

    default boolean onConversationItemLongClicked(T t) {
        return false;
    }

    default void requestStoragePermission() {
    }

    default void onFileClicked(Uri fileUrl) {
    }

    default void systemSupportClicked() {

    }

    default void systemRateClicked() {

    }

    default void listSubmitted() {
    }

    default void onError(String error) {}
}
