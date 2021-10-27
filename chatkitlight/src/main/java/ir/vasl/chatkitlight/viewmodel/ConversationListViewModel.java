package ir.vasl.chatkitlight.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.List;

import ir.vasl.chatkitlight.database.DatabaseLayer;
import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.utils.Constants;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus;

public class ConversationListViewModel extends AndroidViewModel {

    private LiveData<PagedList<ConversationModel>> liveData;
    private Application application;

    public ConversationListViewModel(@NonNull Application application, String chatID) {
        super(application);
        this.application = application;
        this.liveData = new LivePagedListBuilder<>(DatabaseLayer.getInstance(application)
                .getChatKitDatabase()
                .getChatDao()
                .getAll(chatID), Constants.PAGINATE_OFFSET)
                .build();
    }

    public ConversationListViewModel(@NonNull Application application, String chatID, boolean timeAsc) {
        super(application);
        this.application = application;
        if(timeAsc) {
            this.liveData = new LivePagedListBuilder<>(DatabaseLayer.getInstance(application)
                    .getChatKitDatabase()
                    .getChatDao()
                    .getAllByTimeAsc(chatID), Constants.PAGINATE_OFFSET)
                    .build();
        } else {
            this.liveData = new LivePagedListBuilder<>(DatabaseLayer.getInstance(application)
                    .getChatKitDatabase()
                    .getChatDao()
                    .getAllByTime(chatID), Constants.PAGINATE_OFFSET)
                    .build();
        }
    }

    public LiveData<PagedList<ConversationModel>> getLiveData() {
        return liveData;
    }

    public void addNewConversation(ConversationModel conversationModel) {
        DatabaseLayer.getInstance(application)
                .getChatKitDatabase()
                .getChatDao()
                .insert(conversationModel);
    }

    public void addNewConversation(ArrayList<ConversationModel> conversationModels) {
        DatabaseLayer.getInstance(application)
                .getChatKitDatabase()
                .getChatDao()
                .insertAll(conversationModels);
    }

    public void updateConversationStatus(String conversationId, ConversationStatus conversationStatus) {
        if (liveData.getValue() == null)
            return;
        for (ConversationModel conversationModel : liveData.getValue().snapshot()) {
            if (conversationModel == null || conversationModel.getConversationId() == null)
                continue;
            if (conversationModel.getConversationId().equals(conversationId)) {
                ConversationModel model = new ConversationModel(conversationModel);
                model.setConversationStatus(conversationStatus);
                DatabaseLayer.getInstance(application)
                        .getChatKitDatabase()
                        .getChatDao()
                        .update(model);
            }
        }
    }

    public void updateConversationStatusWithIDSwap(String conversationIdOld, String conversationIdNew, ConversationStatus conversationStatus) {
        if (liveData.getValue() == null)
            return;
        for (ConversationModel conversationModel : liveData.getValue().snapshot()) {
            if (conversationModel == null || conversationModel.getConversationId() == null)
                continue;
            if (conversationModel.getConversationId().equals(conversationIdOld)) {
                ConversationModel model = new ConversationModel(conversationModel);
                model.setConversationStatus(conversationStatus);

                DatabaseLayer.getInstance(application)
                        .getChatKitDatabase()
                        .getChatDao()
                        .update(model);
                DatabaseLayer.getInstance(application)
                        .getChatKitDatabase()
                        .getChatDao()
                        .swapId(conversationIdOld, conversationIdNew);
            }
        }
    }

    public void updateConversationStatusWithIDSwap(String conversationIdOld, String conversationIdNew, String ChatID, ConversationStatus conversationStatus) {
        if (liveData.getValue() == null)
            return;
        for (ConversationModel conversationModel : liveData.getValue().snapshot()) {
            if (conversationModel == null || conversationModel.getConversationId() == null)
                continue;
            if (conversationModel.getConversationId().equals(conversationIdOld)) {
                ConversationModel model = new ConversationModel(conversationModel);
                model.setConversationStatus(conversationStatus);

                DatabaseLayer.getInstance(application)
                        .getChatKitDatabase()
                        .getChatDao()
                        .update(model);
                DatabaseLayer.getInstance(application)
                        .getChatKitDatabase()
                        .getChatDao()
                        .swapId(conversationIdOld, conversationIdNew, ChatID);
            }
        }
    }

    public void removeConversationModel(ConversationModel conversationModel) {
        if (conversationModel != null) {
            DatabaseLayer.getInstance(application)
                    .getChatKitDatabase()
                    .getChatDao()
                    .delete(conversationModel);
        }
    }

    public void removeAllConversationModel() {
        DatabaseLayer.getInstance(application)
                .getChatKitDatabase()
                .getChatDao()
                .deleteAll();
    }

    public int getConversationSize(String chatId) {
        return DatabaseLayer.getInstance(application)
                .getChatKitDatabase()
                .getChatDao()
                .getAllSimple(chatId)
                .size();
    }

    public List<ConversationModel> getAllDataSimple (String chatId){
        return DatabaseLayer.getInstance(application)
                .getChatKitDatabase()
                .getChatDao()
                .getAllSimple(chatId);
    }

}
