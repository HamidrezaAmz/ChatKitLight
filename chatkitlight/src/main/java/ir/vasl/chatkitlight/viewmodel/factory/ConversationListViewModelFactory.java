package ir.vasl.chatkitlight.viewmodel.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.vasl.chatkitlight.viewmodel.ConversationListViewModel;

public class ConversationListViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private String mChatID;

    public ConversationListViewModelFactory(Application application, String chatID) {
        mApplication = application;
        mChatID = chatID;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ConversationListViewModel(mApplication, mChatID, false);
    }
}