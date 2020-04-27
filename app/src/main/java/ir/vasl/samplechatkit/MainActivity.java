package ir.vasl.samplechatkit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.UUID;

import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.callback.AttachmentsListener;
import ir.vasl.chatkitlight.ui.callback.InputListener;
import ir.vasl.chatkitlight.ui.callback.TypingListener;
import ir.vasl.chatkitlight.ui.view.ConversationInput;
import ir.vasl.chatkitlight.ui.view.ConversationList;
import ir.vasl.chatkitlight.utils.DataGenerator;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;
import ir.vasl.chatkitlight.viewmodel.ConversationListViewModel;

public class MainActivity
        extends AppCompatActivity
        implements TypingListener, AttachmentsListener, InputListener {

    private ConversationInput conversationInput;

    private ConversationList conversationList;

    private ConversationListViewModel conversationListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conversationInput = findViewById(R.id.conversationInput);
        conversationList = findViewById(R.id.conversationList);

        conversationInput.setInputListener(this);
        conversationInput.setAttachmentsListener(this);
        conversationInput.setTypingListener(this);

        initViewModel();
    }

    private void initViewModel() {

        conversationListViewModel = new ViewModelProvider(this).get(ConversationListViewModel.class);
        conversationList.setConversationListViewModel(conversationListViewModel);

        conversationListViewModel.setLiveData(DataGenerator.getConversationList());

    }

    @Override
    public boolean onSubmit(CharSequence input) {

        ConversationModel conversationModel = new ConversationModel();
        conversationModel.setId(UUID.randomUUID().toString());
        conversationModel.setTitle("TITLE");
        conversationModel.setMessage(input.toString());
        conversationModel.setTime("00:00");
        conversationModel.setConversationType(ConversationType.CLIENT);

        conversationListViewModel.addNewConversation(conversationModel);
        return true;
    }

    @Override
    public void onAddAttachments() {
        // Toast.makeText(this, "onAddAttachments", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartTyping() {
        // Toast.makeText(this, "onStartTyping", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStopTyping() {
        // Toast.makeText(this, "onStopTyping", Toast.LENGTH_SHORT).show();
    }
}
