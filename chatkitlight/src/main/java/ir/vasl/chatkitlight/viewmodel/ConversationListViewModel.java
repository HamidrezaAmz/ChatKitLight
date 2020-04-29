package ir.vasl.chatkitlight.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus;

public class ConversationListViewModel extends AndroidViewModel {

    private MutableLiveData<List<ConversationModel>> liveData = new MutableLiveData<>(new ArrayList<>());

    public ConversationListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<ConversationModel>> getLiveData() {
        return liveData;
    }

    public void setLiveData(List<ConversationModel> liveData) {
        this.liveData.postValue(liveData);
    }

    public void addNewConversation(ConversationModel conversationModel) {
        this.liveData.getValue().add(conversationModel);
        this.liveData.postValue(liveData.getValue());
    }

    public void addNewConversation(List<ConversationModel> conversationModels) {
        this.liveData.getValue().addAll(conversationModels);
        this.liveData.postValue(liveData.getValue());
    }

    public void updateConversationStatus(String conversationId, ConversationStatus conversationStatus) {

        if (liveData.getValue() == null)
            return;

        for (ConversationModel conversationModel : liveData.getValue()) {
            if (conversationModel.getId().equals(conversationId)) {
                conversationModel.setConversationStatus(conversationStatus);
            }
        }

        this.liveData.postValue(liveData.getValue());
    }

    public void removeConversationModel(ConversationModel conversationModel) {
        this.liveData.getValue().remove(conversationModel);
        this.liveData.notifyAll();
    }

}
