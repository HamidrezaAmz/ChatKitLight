package ir.vasl.chatkitlight.ui.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.record_view.OnRecordListener;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.callback.AttachmentsListener;
import ir.vasl.chatkitlight.ui.callback.ConversationViewListener;
import ir.vasl.chatkitlight.ui.callback.DialogMenuListener;
import ir.vasl.chatkitlight.ui.callback.InputListener;
import ir.vasl.chatkitlight.ui.callback.TypingListener;
import ir.vasl.chatkitlight.utils.Constants;
import ir.vasl.chatkitlight.utils.globalEnums.ChatStyleEnum;
import ir.vasl.chatkitlight.viewmodel.ConversationListViewModel;

import static androidx.core.content.ContextCompat.getSystemService;

public class ConversationView
        extends LinearLayout
        implements TypingListener,
        AttachmentsListener,
        InputListener,
        DialogMenuListener,
        SwipyRefreshLayout.OnRefreshListener,
        OnRecordListener, View.OnClickListener {

    private ConversationViewListener conversationViewListener;
    public ConversationList conversationList;
    private ConversationInput conversationInput;
    private SwipyRefreshLayout swipyRefreshLayout;
    private CardView cardViewHintViewHolder;
    private TextView textViewHintView;
    private Button buttonHintView;
    private ChatStyleEnum chatStyleEnum = ChatStyleEnum.DEFAULT;
    private boolean canShowExtraOptionButton = false;
    private boolean canShowVoiceRecording = true;
    private boolean canShowAttachment = true;
    private boolean showBlockerView = true;

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
            case Constants.THEME_ARMAN_VARZESH_ORDINAL:
                chatStyleEnum = ChatStyleEnum.ARMAN_VARZESH;
                break;
            case Constants.THEME_LAWONE_ORDINAL:
                chatStyleEnum = ChatStyleEnum.LAWONE;
                break;
        }
        canShowExtraOptionButton = style.canShowExtraOptionButton();
        canShowVoiceRecording = style.canShowVoiceRecording();
        canShowAttachment = style.canShowAttachment();

        init(context);
        conversationList.setCanShowDialog(style.canShowDialog());
        conversationInput.setCanShowAttachment(style.canShowAttachment());
        conversationInput.setCanShowExtraOption(style.canShowExtraOptionButton());
        conversationInput.setCanShowVoiceRecording(style.canShowVoiceRecording());
        conversationList.setClientBubbleColor(style.getClientBubbleColor());
        conversationList.setServerBubbleColor(style.getServerBubbleColor());
        conversationList.setChatStyle(chatStyleEnum);
        conversationList.initialize();
    }

    private void init(Context context) {

        View conversationView = LayoutInflater.from(context).inflate(R.layout.layout_conversation_view, this, true);

        // conversation view items
        conversationList = conversationView.findViewById(R.id.conversationList);
        conversationInput = conversationView.findViewById(R.id.conversationInput);
        cardViewHintViewHolder = conversationView.findViewById(R.id.cardView_hint_view_holder);
        textViewHintView = conversationView.findViewById(R.id.textView_hint_view);
        buttonHintView = conversationView.findViewById(R.id.button_hint_view);

        if (canShowExtraOptionButton) {
            conversationInput.findViewById(R.id.imageView_extra_option).setVisibility(VISIBLE);
        }
        if (!canShowAttachment)
            conversationInput.findViewById(R.id.attachmentButton).setVisibility(GONE);

        swipyRefreshLayout = conversationView.findViewById(R.id.swipyRefreshLayout);

        // listeners
        conversationInput.setInputListener(this);
        conversationList.setDialogMenuListener(this);
        swipyRefreshLayout.setOnRefreshListener(this);
        conversationInput.setAttachmentsListener(this);
        conversationInput.setOnRecordListener(this);
        buttonHintView.setOnClickListener(this);

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
    public void extraOptionClicked() {
        if (conversationViewListener != null) {
            conversationViewListener.extraOptionClicked();
        }
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

    @SuppressWarnings("unchecked")
    @Override
    public void onCopyMessageClicked(Object object) {
        ClipboardManager clipboard = getSystemService(getContext(), ClipboardManager.class);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("message", ((ConversationModel) object).getMessage());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "متن پیام کپی شد!", Toast.LENGTH_SHORT).show();
        }
        if (conversationViewListener != null)
            conversationViewListener.onCopyMessageClicked(object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResendMessageClicked(Object object) {
        if (conversationViewListener != null)
            conversationViewListener.onResendMessageClicked(object);
    }

    @SuppressWarnings("unchecked")
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
                if (manager.findFirstCompletelyVisibleItemPosition() == 0) {
                    swipyRefreshLayout.setEnabled(true);
                } else {
                    swipyRefreshLayout.setEnabled((recyclerView.canScrollVertically(DIRECTION_UP)));
                }
            }
        }
    };

    @Override
    public void requestStoragePermission() {
        if (conversationViewListener != null)
            conversationViewListener.requestStoragePermission();
    }

    @Override
    public void pdfFileClicked(Uri pdfUri) {
        if (conversationViewListener != null)
            conversationViewListener.pdfFileClicked(pdfUri);
    }

    @Override
    public void onAddAttachments() {
        if (conversationViewListener != null)
            conversationViewListener.onAddAttachments(null);
    }

    @Override
    public void shouldPaginateNow() {
        if (conversationViewListener != null)
            conversationViewListener.shouldPaginate();
    }

    @Override
    public void onStart() {
        if (conversationViewListener != null)
            conversationViewListener.onVoiceRecordStarted();
    }

    @Override
    public void onCancel() {
        if (conversationViewListener != null)
            conversationViewListener.onVoiceRecordCanceled();
    }

    @Override
    public void onFinish(long recordTime) {
        if (conversationViewListener != null)
            conversationViewListener.onVoiceRecordStopped(recordTime);
    }

    @Override
    public void onLessThanSecond() {
        if (conversationViewListener != null)
            conversationViewListener.onVoiceRecordCanceled();
    }

    public void stopMediaPlayer() {
        conversationList.stopMediaPlayer();
    }

    public void setShowBlockerView(boolean showBlockerView) {
        conversationInput.setShowBlockerView(showBlockerView);
    }

    public void showHintView(String title) {
        textViewHintView.setText(title);
        cardViewHintViewHolder.setVisibility(VISIBLE);
    }

    public void hideHintView() {
        textViewHintView.setText("");
        cardViewHintViewHolder.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.button_hint_view)
            hideHintView();
    }

}
