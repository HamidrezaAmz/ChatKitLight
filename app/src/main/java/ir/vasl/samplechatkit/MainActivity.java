package ir.vasl.samplechatkit;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.UUID;

import ir.vasl.chatkitlight.data.DBLayer;
import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.callback.AttachmentsListener;
import ir.vasl.chatkitlight.ui.callback.InputListener;
import ir.vasl.chatkitlight.ui.callback.TypingListener;
import ir.vasl.chatkitlight.ui.view.ConversationInput;
import ir.vasl.chatkitlight.ui.view.ConversationList;
import ir.vasl.chatkitlight.utils.DataGenerator;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus;
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

        LocaleHelper.setApplicationLanguage(this);

        conversationInput = findViewById(R.id.conversationInput);
        conversationList = findViewById(R.id.conversationList);

        conversationInput.setInputListener(this);
        conversationInput.setAttachmentsListener(this);
        conversationInput.setTypingListener(this);

        initViewModel();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        LocaleHelper.onAttach(newBase);
    }

    private void initViewModel() {
        if(DBLayer.getInstance(MyApplication.getApp()).getDb().chat().getAllSimple().size() == 0) //todo -> this is temp
            DBLayer.getInstance(MyApplication.getApp()).getDb().chat().insertAll(DataGenerator.getConversationList());

        conversationListViewModel = new ViewModelProvider(this).get(ConversationListViewModel.class);
        conversationList.setConversationListViewModel(conversationListViewModel);
    }

    @Override
    public boolean onSubmit(CharSequence input) {

        ConversationModel conversationModel = new ConversationModel();
        conversationModel.setId(UUID.randomUUID().toString());
        conversationModel.setTitle("");
        conversationModel.setMessage(input.toString());
        conversationModel.setTime("03:19");
        conversationModel.setConversationType(ConversationType.CLIENT);
        conversationModel.setConversationStatus(ConversationStatus.SENDING);

        conversationListViewModel.addNewConversation(conversationModel);

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("OnTick", "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                conversationListViewModel.updateConversationStatus(conversationModel.getId(), ConversationStatus.SENT);
            }

        }.start();

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
