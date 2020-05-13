package ir.vasl.chatkitlight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.ui.callback.AttachmentsListener;
import ir.vasl.chatkitlight.ui.callback.ConversationViewListener;
import ir.vasl.chatkitlight.ui.callback.DialogMenuListener;
import ir.vasl.chatkitlight.ui.callback.InputListener;
import ir.vasl.chatkitlight.ui.callback.TypingListener;
import ir.vasl.chatkitlight.viewmodel.ConversationListViewModel;

public class ConversationView
        extends FrameLayout
        implements TypingListener, AttachmentsListener, InputListener, DialogMenuListener, SwipyRefreshLayout.OnRefreshListener {

    private ConversationViewListener conversationViewListener;
    private SwipyRefreshLayout swipyRefreshLayout;
    private ConversationInput conversationInput;
    private ConversationList conversationList;

    public ConversationView(Context context) {
        super(context);
        initConversationView();
    }

    public ConversationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initConversationView();
    }

    public ConversationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConversationView();
    }

    private void initConversationView() {
        View viewConversation = inflate(getContext(), R.layout.layout_conversation_view, this);

        // conversation view items
        swipyRefreshLayout = viewConversation.findViewById(R.id.swipyRefreshLayout);
        conversationList = viewConversation.findViewById(R.id.conversationList);
        conversationInput = viewConversation.findViewById(R.id.conversationInput);

        // conversation view listeners
        conversationInput.setInputListener(this);
        conversationInput.setAttachmentsListener(this);
        conversationInput.setTypingListener(this);
        conversationList.setDialogMenuListener(this);
        swipyRefreshLayout.setOnRefreshListener(this);

    }

    public void setConversationViewListener(ConversationViewListener conversationViewListener) {
        this.conversationViewListener = conversationViewListener;
    }

    public void setConversationListViewModel(ConversationListViewModel conversationListViewModel) {
        conversationList.setConversationListViewModel(conversationListViewModel);
    }

    @Override
    public void onAddAttachments() {
        if (conversationViewListener != null)
            conversationViewListener.onAddAttachments();
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        if (conversationViewListener != null) {
            conversationViewListener.onSubmit(input);
            return true;
        }
        return false;
    }

    @Override
    public void onStartTyping() {
        if (conversationViewListener != null)
            conversationViewListener.onStartTyping();
    }

    @Override
    public void onStopTyping() {
        if (conversationViewListener != null)
            conversationViewListener.onStopTyping();
    }

    @Override
    public void onCopyMessageClicked(Object object) {
        if (conversationViewListener != null)
            conversationViewListener.onCopyMessageClicked(object);
    }

    @Override
    public void onResendMessageClicked(Object object) {
        if (conversationViewListener != null)
            conversationViewListener.onResendMessageClicked(object);
    }

    @Override
    public void onDeleteMessageClicked(Object object) {
        if (conversationViewListener != null)
            conversationViewListener.onDeleteMessageClicked(object);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (conversationViewListener != null)
            conversationViewListener.onSwipeRefresh();
    }

    public void hideSwipeRefresh() {
        swipyRefreshLayout.setRefreshing(false);
    }

    public void showSwipeRefresh() {
        swipyRefreshLayout.setRefreshing(true);
    }
}
