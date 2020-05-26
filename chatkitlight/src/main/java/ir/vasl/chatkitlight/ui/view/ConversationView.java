package ir.vasl.chatkitlight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
    private ConversationInput conversationInput;
    private ConversationList conversationList;
    private SwipyRefreshLayout swipyRefreshLayout;

    public ConversationView(Context context) {
        super(context);
        init(context);
    }

    public ConversationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ConversationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
        ConversationViewStyle style = ConversationViewStyle.parse(context, attrs);

        conversationList.setCanShowDialog(style.canShowDialog());
    }

    private void init(Context context) {
        View viewConversation = inflate(context, R.layout.layout_conversation_view, this);

        // conversation view items
        swipyRefreshLayout = viewConversation.findViewById(R.id.swipyRefreshLayout);
        conversationList = viewConversation.findViewById(R.id.conversationList);
        conversationInput = viewConversation.findViewById(R.id.conversationInput);

        // conversation view listeners
        conversationInput.setInputListener(this);
        conversationInput.setAttachmentsListener(this);
        conversationInput.setTypingListener(this);
        swipyRefreshLayout.setOnRefreshListener(this);
        conversationList.setDialogMenuListener(this);

        // fix recyclerview conflict with swipe refresh
        conversationList.addOnScrollListener(scrollListener);
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

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        private static final int DIRECTION_UP = -1;

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            swipyRefreshLayout.setEnabled((recyclerView.canScrollVertically(DIRECTION_UP)));
        }
    };
}
