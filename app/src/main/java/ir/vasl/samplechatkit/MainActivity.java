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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import droidninja.filepicker.FilePickerBuilder;
import ir.vasl.chatkitlight.database.DatabaseLayer;
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
    private PermissionHelper permissionHelper;

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

    int tester = 0;

    @Override
    public void onSubmit(CharSequence input) {
        tester ++;

        List<ConversationModel> allModels = DatabaseLayer.getInstance(MyApplication.getApp()).getChatKitDatabase().getChatDao().getAllSimple(chatID);

        ConversationModel conversationModel = new ConversationModel(chatID, UUID.randomUUID().toString());
        conversationModel.setTitle("");
        conversationModel.setMessage(input.toString());
        conversationModel.setTime(TimeUtils.getCurrTime());
        if(tester % 3 == 0){
            conversationModel.setConversationType(ConversationType.CLIENT);
        } else if (tester % 3 == 1){
            conversationModel.setConversationType(ConversationType.SERVER);
        } else if (tester % 3 == 2){
            conversationModel.setConversationType(ConversationType.SYSTEM);
        }
        if(tester % 5 == 0){
            conversationModel.setFileType(FileType.NONE);
        }
        else if(tester % 5 == 1){
            conversationModel.setFileType(FileType.AUDIO);
        }
        else if(tester % 5 == 2){
            conversationModel.setFileType(FileType.DOCUMENT);
        }
        else if(tester % 5 == 3){
            conversationModel.setFileType(FileType.IMAGE);
        }
        else if(tester % 5 == 4){
            conversationModel.setFileType(FileType.DOCUMENT);
        }
        conversationModel.setConversationStatus(ConversationStatus.DELIVERED);
        conversationModel.setFileAddress("https://www.w3schools.com/howto/img_avatar.png");
        conversationModel.setImageRes(String.valueOf(R.drawable.ic_avatar_2));
//        if (imageUri != null) {
//            conversationModel.setFileAddress(imageUri.toString());
//            conversationModel.setFileAddress("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf");
//        conversationModel.setFileAddress("https://www.kozco.com/tech/piano2.wav");
//        conversationModel.setFileAddress("https://www.kozco.com/tech/organfinale.wav");
        findViewById(R.id.image2).setVisibility(View.GONE);
//        }

        conversationListViewModel.addNewConversation(conversationModel);

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("OnTick", "seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                List<ConversationModel> allModels = DatabaseLayer.getInstance(MyApplication.getApp()).getChatKitDatabase().getChatDao().getAllSimple(chatID);

                for (int i = 0; i < allModels.size(); i++) {
                    Log.e(TAG, "AFTER SUBMIT: " + allModels.get(i).getId() + " " + allModels.get(i).getMessage());
                }
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
        Log.e(TAG, "onSwipeRefresh: " );
    }

    @Override
    public void onAddAttachments(AttachmentOption option) {
        if (!checkPermission())
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
        image.setVisibility(View.VISIBLE);
        ArrayList<Parcelable> uri = data.getParcelableArrayListExtra("SELECTED_PHOTOS");
        image.setImageUrl(((Uri) uri.get(0)));
        imageUri = ((Uri) uri.get(0));
        Log.e(TAG, "onActivityResult: " + uri);

    }

    @Override
    public void pdfFileClicked(Uri pdfUri) {
        Log.e(TAG, "pdfFileClicked: " + pdfUri);
    }

    @Override
    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                199);
    }

    @Override
    public void onVoiceRecordCanceled() {
        Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVoiceRecordStarted() {
        Toast.makeText(this, "START", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVoiceRecordStopped(long recordTime) {
        Toast.makeText(this, "STOP", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        conversationView.stopMediaPlayer();
    }
}
