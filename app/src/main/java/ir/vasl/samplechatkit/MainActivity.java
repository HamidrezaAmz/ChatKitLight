package ir.vasl.samplechatkit;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.UUID;

import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.callback.AttachmentsListener;
import ir.vasl.chatkitlight.ui.callback.InputListener;
import ir.vasl.chatkitlight.ui.callback.TypingListener;
import ir.vasl.chatkitlight.ui.view.ConversationInput;
import ir.vasl.chatkitlight.ui.view.ConversationList;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;
import ir.vasl.chatkitlight.viewmodel.ConversationListViewModel;
import ir.vasl.chatkitlight.viewmodel.factory.ConversationListViewModelFactory;

public class MainActivity
        extends AppCompatActivity
        implements TypingListener, AttachmentsListener, InputListener {

    private ConversationInput conversationInput;
    private ConversationList conversationList;
    private ConversationListViewModel conversationListViewModel;

    private String chatID = "tempChatId";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        conversationListViewModel = new ViewModelProvider(this, new ConversationListViewModelFactory(getApplication(), chatID)).get(ConversationListViewModel.class);
        conversationList.setConversationListViewModel(conversationListViewModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_delete:
                conversationListViewModel.removeAllConversationModel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public boolean onSubmit(CharSequence input) {
        createNewConversation(input);
        return true;
    }

    private void createNewConversation(CharSequence input) {

        ConversationModel conversationModel = new ConversationModel(chatID, UUID.randomUUID().toString());
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
                conversationListViewModel.updateConversationStatus(conversationModel.getConversationId(), ConversationStatus.SENT);
            }

        }.start();

        new CountDownTimer(7000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("OnTick", "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                conversationListViewModel.updateConversationStatus(conversationModel.getConversationId(), ConversationStatus.DELIVERED);
            }

        }.start();

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("OnTick", "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                conversationListViewModel.updateConversationStatus(conversationModel.getConversationId(), ConversationStatus.SEEN);
            }

        }.start();

    }

}
