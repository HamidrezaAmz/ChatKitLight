package ir.vasl.chatkitlight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.adapter.ConversationAdapter;
import ir.vasl.chatkitlight.ui.callback.ConversationListListener;
import ir.vasl.chatkitlight.viewmodel.ConversationListViewModel;

public class ConversationList extends RecyclerView implements ConversationListListener {

    private ConversationAdapter adapter;
    private ConversationListViewModel conversationListViewModel;

    private int currItemSize = 0;

    private boolean reverseLayout = true;

    public ConversationList(@NonNull Context context) {
        super(context);
        initialize();
    }

    public ConversationList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ConversationList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        initAdapter();
    }

    private void initAdapter() {

        this.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(true);

        this.adapter = new ConversationAdapter(this);
        this.setLayoutManager(layoutManager);
//        this.setItemAnimator(new DefaultItemAnimator());
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
//                adapter.setConversationModels(conversationModels);
                adapter.submitList(conversationModels);
//                adapter.notifyDataSetChanged();
                if (currItemSize != 0 && currItemSize < conversationModels.size())
                    smoothScrollToPosition(conversationModels.size());

                currItemSize = conversationModels.size();
            }
        });
    }

    @Override
    public void onConversationItemClicked(Object object) {
        Toast.makeText(getContext(), "CLICK YO YOOO...", Toast.LENGTH_SHORT).show();
    }

}
