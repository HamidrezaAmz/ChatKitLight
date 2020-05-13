package ir.vasl.chatkitlight.ui.callback;

public interface ConversationViewListener<T> {

    default void onAddAttachments() {
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

}
