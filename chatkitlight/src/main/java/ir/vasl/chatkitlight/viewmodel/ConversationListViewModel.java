package ir.vasl.chatkitlight.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.ArrayList;

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

    public LiveData<PagedList<ConversationModel>> getLiveData() {
        return liveData;
    }

    public void addNewConversation(ConversationModel conversationModel) {
        DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().insert(conversationModel);
    }

    public void addNewConversationToTop(ConversationModel conversationModel) {
        ArrayList<ConversationModel> convs = (ArrayList<ConversationModel>) DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().getAllSimple(conversationModel.getChatId());
        if(convs == null){
            DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().insert(conversationModel);
        } else {
            DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().deleteAll();
            ArrayList<ConversationModel> convs2 = new ArrayList<>();
            convs2.add(conversationModel);
            convs2.addAll(convs);
            DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().insertAll(convs2);
        }
    }

    public void addNewConversation(ArrayList<ConversationModel> conversationModels) {
        DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().insertAll(conversationModels);
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
                DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().update(model);
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

                DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().update(model);
                DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().swapId(conversationIdOld, conversationIdNew);
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

                DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().update(model);
                DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().swapId(conversationIdOld, conversationIdNew, ChatID);
            }
        }
    }

    public void removeConversationModel(ConversationModel conversationModel) {
        if (conversationModel != null) {
            DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().delete(conversationModel);
        }
    }

    public void removeAllConversationModel() {
        DatabaseLayer.getInstance(application).getChatKitDatabase().getChatDao().deleteAll();
    }

}
