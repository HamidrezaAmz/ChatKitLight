package ir.vasl.samplechatkit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.UUID;

import droidninja.filepicker.FilePickerBuilder;
import ir.vasl.chatkitlight.database.ChatDao;
import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.audio.AttachmentOption;
import ir.vasl.chatkitlight.ui.callback.ConversationViewListener;
import ir.vasl.chatkitlight.ui.customview.ImageViewCustom;
import ir.vasl.chatkitlight.ui.view.ConversationView;
import ir.vasl.chatkitlight.utils.TimeUtils;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;
import ir.vasl.chatkitlight.utils.globalEnums.FileType;
import ir.vasl.chatkitlight.viewmodel.ConversationListViewModel;
import ir.vasl.chatkitlight.viewmodel.factory.ConversationListViewModelFactory;

public class MainActivity
        extends AppCompatActivity
        implements ConversationViewListener {

    private Toolbar toolbar;
    private ConversationListViewModel conversationListViewModel;
    private ConversationView conversationView;

    private static final String chatID = "tempChatId";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.setApplicationLanguage(this);

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        conversationView = findViewById(R.id.conversationView);

        setSupportActionBar(toolbar);

        initViewModel();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        LocaleHelper.onAttach(newBase);
    }

    private void initViewModel() {
        conversationListViewModel = new ViewModelProvider(this, new ConversationListViewModelFactory(getApplication(), chatID)).get(ConversationListViewModel.class);
        conversationView.setConversationListViewModel(conversationListViewModel);
        conversationView.setConversationViewListener(this);
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
    public void onSubmit(CharSequence input) {

        ConversationModel conversationModel = new ConversationModel(chatID, UUID.randomUUID().toString());
        conversationModel.setTitle("");
        conversationModel.setMessage(input.toString());
        conversationModel.setTime(TimeUtils.getCurrTime());
        conversationModel.setConversationType(ConversationType.CLIENT);
        conversationModel.setConversationStatus(ConversationStatus.SENDING);
        if(imageUri != null) {
            conversationModel.setFileType(FileType.IMAGE);
            conversationModel.setFileAddress(imageUri.toString());
        }

        conversationListViewModel.addNewConversation(conversationModel);

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("OnTick", "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                conversationListViewModel.updateConversationStatus(conversationModel.getConversationId(), ConversationStatus.SENT);
            }

        }.start();

        new CountDownTimer(3500, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("OnTick", "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                conversationListViewModel.updateConversationStatus(conversationModel.getConversationId(), ConversationStatus.DELIVERED);
            }

        }.start();

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("OnTick", "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                conversationListViewModel.updateConversationStatus(conversationModel.getConversationId(), ConversationStatus.SEEN);
            }

        }.start();
    }

    @Override
    public void onCopyMessageClicked(Object object) {

    }

    @Override
    public void onResendMessageClicked(Object object) {

    }

    @Override
    public void onDeleteMessageClicked(Object object) {
        if (object instanceof ConversationModel)
            conversationListViewModel.removeConversationModel((ConversationModel) object);
    }

    @Override
    public void onSwipeRefresh() {
        conversationView.hideSwipeRefresh();
    }

    PermissionHelper permissionHelper;

    @Override
    public void onAddAttachments(AttachmentOption option) {
        if(!checkPermission())
            return;
        pickGallery();
    }

    private void pickGallery() {
        FilePickerBuilder.getInstance()
                .setActivityTheme(R.style.AppTheme) //optional
                .enableVideoPicker(true)
                .pickPhoto(this, 400);
    }

    private boolean checkPermission() {
        permissionHelper = new PermissionHelper(this);
        return permissionHelper.checkAccessStoragePermissionRead();
    }

    Uri imageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageViewCustom image = findViewById(R.id.image2);
        ArrayList<Parcelable> uri = data.getParcelableArrayListExtra("SELECTED_PHOTOS");
        image.setImageUrl(((Uri) uri.get(0)));
        imageUri = ((Uri) uri.get(0));
        Log.e(TAG, "onActivityResult: " + uri );

    }
}
