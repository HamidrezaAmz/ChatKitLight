package ir.vasl.chatkitlight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.utils.globalEnums.ChatStyleEnum;
import ir.vasl.chatkitlight.ui.audio.AttachmentOption;
import ir.vasl.chatkitlight.ui.audio.AttachmentOptionsListener;
import ir.vasl.chatkitlight.ui.audio.AudioRecordView;
import ir.vasl.chatkitlight.ui.callback.AttachmentsListener;
import ir.vasl.chatkitlight.ui.callback.ConversationViewListener;
import ir.vasl.chatkitlight.ui.callback.DialogMenuListener;
import ir.vasl.chatkitlight.ui.callback.InputListener;
import ir.vasl.chatkitlight.ui.callback.TypingListener;
import ir.vasl.chatkitlight.viewmodel.ConversationListViewModel;

public class ConversationView
        extends FrameLayout
        implements TypingListener,
        AttachmentsListener,
        InputListener,
        DialogMenuListener,
        SwipyRefreshLayout.OnRefreshListener,
        AudioRecordView.RecordingListener {

    private ConversationViewListener conversationViewListener;
    private AudioRecordView conversationInput;
    private ConversationList conversationList;
    private SwipyRefreshLayout swipyRefreshLayout;

    private ChatStyleEnum chatStyleEnum = ChatStyleEnum.DEFAULT;

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
        ConversationViewStyle style = ConversationViewStyle.parse(context, attrs);
        switch (style.getChatStyle()) {
            case 1:
                chatStyleEnum = ChatStyleEnum.ARMAN_VARZESH;
                break;
            case 2:
                chatStyleEnum = ChatStyleEnum.LAWONE;
                break;
        }
        init(context);
        conversationInput.setCanShowVoiceRecording(style.canShowVoiceRecording());
        conversationInput.setCanShowAttachment(style.canShowAttachment());
        conversationList.setCanShowDialog(style.canShowDialog());
        conversationList.setClientBubbleColor(style.getClientBubbleColor());
        conversationList.setServerBubbleColor(style.getServerBubbleColor());
        conversationList.setChatStyle(chatStyleEnum);
        conversationList.initialize();
    }

    private void init(Context context) {
        ViewGroup viewConversation = (ViewGroup) inflate(context, R.layout.layout_conversation_view, this);
        // conversation view items
        swipyRefreshLayout = viewConversation.findViewById(R.id.swipyRefreshLayout);
        conversationList = viewConversation.findViewById(R.id.conversationList);
        conversationInput = new AudioRecordView();
        conversationInput.setChatStyle(chatStyleEnum);
        conversationInput.initView(viewConversation);
        conversationInput.showCameraIcon(false);
        conversationInput.getAttachmentView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddAttachments();
            }
        });
        conversationInput.getSendView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = conversationInput.getMessageView().getText().toString();
                conversationInput.getMessageView().setText("");
                onSubmit(msg);
            }
        });
        swipyRefreshLayout.setOnRefreshListener(this);
        conversationList.setDialogMenuListener(this);
        conversationInput.setRecordingListener(this);
        // fix recyclerview conflict with swipe refresh
        conversationList.addOnScrollListener(scrollListener);
        switch (chatStyleEnum) {
            case DEFAULT:
            case ARMAN_VARZESH:
                viewConversation.findViewById(R.id.frameLayout_root).setBackgroundColor(context.getResources().getColor(R.color.black));
                break;
            case LAWONE:
                viewConversation.findViewById(R.id.frameLayout_root).setBackgroundColor(context.getResources().getColor(R.color.white));
                break;
        }
    }

    public void setConversationViewListener(ConversationViewListener conversationViewListener) {
        this.conversationViewListener = conversationViewListener;
    }

    public void setConversationListViewModel(ConversationListViewModel conversationListViewModel) {
        conversationList.setConversationListViewModel(conversationListViewModel);
    }

    @Override
    public boolean onSubmit(CharSequence input) {

        String message = input.toString().trim();
        if (message.isEmpty())
            return false;

        if (conversationViewListener != null) {
            conversationViewListener.onSubmit(message);
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

            LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
            if (manager != null) {
                if (manager.findFirstCompletelyVisibleItemPosition() == 0)
                    swipyRefreshLayout.setEnabled(true);
                else
                    swipyRefreshLayout.setEnabled((recyclerView.canScrollVertically(DIRECTION_UP)));
            }
        }
    };

    @Override
    public void requestStoragePermission() {
        if (conversationViewListener != null)
            conversationViewListener.requestStoragePermission();
    }

    @Override
    public void onAddAttachments(AttachmentOption option) {
        if (conversationViewListener != null)
            conversationViewListener.onAddAttachments(option);
    }

    @Override
    public void onAddAttachments() {
        if (conversationViewListener != null)
            conversationViewListener.onAddAttachments();
    }

    @Override
    public void onRecordingStarted() {
        if (conversationViewListener != null)
            conversationViewListener.onVoiceRecordStarted();
    }

    @Override
    public void onRecordingLocked() {

    }

    @Override
    public void onRecordingCompleted() {
        if (conversationViewListener != null)
            conversationViewListener.onVoiceRecordStopped();
    }

    @Override
    public void onRecordingCanceled() {
        if (conversationViewListener != null)
            conversationViewListener.onVoiceRecordCanceled();
    }

    @Override
    public void shouldPaginateNow() {
        if (conversationViewListener != null)
            conversationViewListener.shouldPaginate();
    }

    @Override
    public void onImageClicked(String url) {
        if (conversationViewListener != null)
            conversationViewListener.onImageClicked(url);
    }

    @Override
    public void onVideoClicked(String url) {
        if (conversationViewListener != null)
            conversationViewListener.onVideoClicked(url);
    }
}
