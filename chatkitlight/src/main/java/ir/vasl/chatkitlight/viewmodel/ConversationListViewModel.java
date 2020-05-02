package ir.vasl.chatkitlight.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ir.vasl.chatkitlight.data.DBLayer;
import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus;

public class ConversationListViewModel extends AndroidViewModel {

    private LiveData<PagedList<ConversationModel>> liveData;
    Application application;

    public ConversationListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        liveData = new LivePagedListBuilder<>(DBLayer.getInstance(application).getDb().chat().getAll(), 5).build();
        Log.e("tag", "ConversationListViewModel: " + " " + new Gson().toJson(liveData));
    }

    public LiveData<PagedList<ConversationModel>> getLiveData() {
        return liveData;
    }

//    public void setLiveData(List<ConversationModel> liveData) {
//        this.liveData.postValue(liveData);
//    }

    public void addNewConversation(ConversationModel conversationModel) {
        DBLayer.getInstance(application).getDb().chat().insert(conversationModel);
//        this.liveData.getValue().add(conversationModel);
//        this.liveData.postValue(liveData.getValue());
    }

    public void addNewConversation(ArrayList<ConversationModel> conversationModels) {
        DBLayer.getInstance(application).getDb().chat().insertAll(conversationModels);
//        this.liveData.getValue().addAll(conversationModels);
//        this.liveData.postValue(liveData.getValue());
    }

    public void updateConversationStatus(String conversationId, ConversationStatus conversationStatus) {

        if (liveData.getValue() == null)
            return;

        for (ConversationModel conversationModel : liveData.getValue().snapshot()) {
            if (conversationModel.getId().equals(conversationId)) {
                conversationModel.setConversationStatus(conversationStatus);
                DBLayer.getInstance(application).getDb().chat().insert(conversationModel);
            }
        }

//        this.liveData.postValue(liveData.getValue());
    }

    public void removeConversationModel(ConversationModel conversationModel) {
        this.liveData.getValue().remove(conversationModel);
        this.liveData.notifyAll();
    }

}
