package ir.vasl.chatkitlight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.logging.Handler;

import ir.vasl.chatkitlight.utils.globalEnums.ChatStyleEnum;
import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.adapter.ConversationAdapter;
import ir.vasl.chatkitlight.ui.callback.ConversationListListener;
import ir.vasl.chatkitlight.ui.callback.DialogMenuListener;
import ir.vasl.chatkitlight.ui.dialogs.DialogChatMenu;
import ir.vasl.chatkitlight.viewmodel.ConversationListViewModel;

public class ConversationList extends RecyclerView implements ConversationListListener, DialogMenuListener {

    private ConversationAdapter adapter;
    private ConversationListViewModel conversationListViewModel;
    private DialogMenuListener dialogMenuListener;

    private int currItemSize = 0;
    private boolean canShowDialog = false;
    private int clientBubbleColor = -1;
    private int serverBubbleColor = -1;

    private ChatStyleEnum chatStyle = ChatStyleEnum.DEFAULT;

    public ConversationList(@NonNull Context context) {
        super(context);
    }

    public ConversationList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ConversationList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initialize() {

        this.setHasFixedSize(false);
        this.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(false);

        this.adapter = new ConversationAdapter(this, chatStyle);
        this.setLayoutManager(layoutManager);
        this.addOnScrollListener(scrollListener);
        this.setItemAnimator(null);
        this.setAdapter(adapter);
        this.adapter.notifyDataSetChanged();
    }

    public void setConversationListViewModel(ConversationListViewModel conversationListViewModel) {
        this.conversationListViewModel = conversationListViewModel;
        initViewModel();
    }

    private void initViewModel() {

        if (conversationListViewModel == null)
            return;

        conversationListViewModel.getLiveData().observeForever(new Observer<PagedList<ConversationModel>>() {
            @Override
            public void onChanged(PagedList<ConversationModel> conversationModels) {
                adapter.submitList(conversationModels);
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollToPosition(0);
                    }
                }, 100);
                currItemSize = conversationModels.size();
            }
        });
    }

    public void setDialogMenuListener(DialogMenuListener dialogMenuListener) {
        this.dialogMenuListener = dialogMenuListener;
    }

    public void setCanShowDialog(boolean canShowDialog) {
        this.canShowDialog = canShowDialog;
    }

    public void setClientBubbleColor(int clientBubbleColor) {
        this.clientBubbleColor = clientBubbleColor;
    }

    public ChatStyleEnum getChatStyle() {
        return chatStyle;
    }

    public void setChatStyle(ChatStyleEnum chatStyle) {
        this.chatStyle = chatStyle;
    }

    public void setServerBubbleColor(int serverBubbleColor) {
        this.serverBubbleColor = serverBubbleColor;
    }

    @Override
    public void onConversationItemClicked(Object object) {
        if (!canShowDialog) return;

        DialogChatMenu dialogChatMenu = new DialogChatMenu(getContext());
        dialogChatMenu.setMenuItem(object);
        dialogChatMenu.setDialogMenuListener(this);
        dialogChatMenu.show();
    }

    @Override
    public void requestStoragePermission() {
        if (dialogMenuListener != null)
            dialogMenuListener.requestStoragePermission();
    }

    @Override
    public void onCopyMessageClicked(Object object) {
        if (dialogMenuListener != null)
            dialogMenuListener.onCopyMessageClicked(object);
    }

    @Override
    public void onResendMessageClicked(Object object) {
        if (dialogMenuListener != null)
            dialogMenuListener.onResendMessageClicked(object);
    }

    @Override
    public void onDeleteMessageClicked(Object object) {
        if (dialogMenuListener != null)
            dialogMenuListener.onDeleteMessageClicked(object);
    }

    RecyclerView.OnScrollListener scrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if(recyclerView.getLayoutManager() == null)
                return;
            int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                dialogMenuListener.shouldPaginateNow();
            }
        }
    };

}
