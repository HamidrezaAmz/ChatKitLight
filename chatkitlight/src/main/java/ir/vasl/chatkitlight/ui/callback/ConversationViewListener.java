package ir.vasl.chatkitlight.ui.callback;

import android.net.Uri;

import ir.vasl.chatkitlight.ui.audio.AttachmentOption;

public interface ConversationViewListener<T> {

    default void onAddAttachments(AttachmentOption option) {
    }

    default void onSubmit(CharSequence input) {
    }

    default void onStartTyping() {
    }

    default void onStopTyping() {
    }

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

    default void onSwipeRefresh() {
    }

    default void onVoiceRecordStarted() {
    }

    default void onVoiceRecordStopped(long recordTime) {
    }

    default void onVoiceRecordCanceled() {
    }

    default void shouldPaginate() {
    }

    default void requestStoragePermission() {
    }

    default void pdfFileClicked(Uri pdfUri) {
    }

    default void extraOptionClicked() {
    }

    default void onHintViewCloseButtonClicked() {
    }

    default void onSupportClicked() {
    }

    default void onRateClicked() {
    }

     default void onListSubmitted() {
    }

}
