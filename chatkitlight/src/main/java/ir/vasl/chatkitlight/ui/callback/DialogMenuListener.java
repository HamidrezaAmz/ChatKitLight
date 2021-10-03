package ir.vasl.chatkitlight.ui.callback;

import android.net.Uri;

public interface DialogMenuListener<T> {

    default void onCopyMessageClicked() {
    }

    default void onCopyMessageClicked(T t) {
    }

    default void onResendMessageClicked() {
    }

    default void onResendMessageClicked(T t) {
    }

    default void onDeleteMessageClicked() {
    }

    default void onDeleteMessageClicked(T t) {
    }

    default void shouldPaginateNow() {
    }

    default void requestStoragePermission() {
    }

    default void onFileClicked(Uri fileUri){}

    default void onSupportClicked(){}

    default void onRateClicked(){}

    default void onListSubmitted(){}
}
