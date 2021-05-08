package ir.vasl.chatkitlight.ui.callback;

import android.net.Uri;

public interface ConversationListListener<T> {

    default void onConversationItemClicked() {
    }

    default void onConversationItemClicked(T t) {
    }

    default void onConversationItemLongClicked() {
    }

    default void onConversationItemLongClicked(T t) {
    }

    default void requestStoragePermission() {
    }

    default void pdfFileClicked(Uri pdfUri) {
    }

    default void systemSupportClicked(){

    }

    default void systemRateClicked(){

    }

    default void listSubmitted(){}
}
