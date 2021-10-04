package ir.vasl.chatkitlight.model;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.utils.Constants;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;
import ir.vasl.chatkitlight.utils.globalEnums.FileType;

@Entity(tableName = Constants.TABLE_NAME)
@TypeConverters({ConversationStatus.class, ConversationType.class, FileType.class})
public class ConversationModel {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "serverSideId")
    private String serverSideId = "";

    @NonNull
    @ColumnInfo(name = "chatId")
    private String chatId = "";

    @NonNull
    @ColumnInfo(name = "conversationId")
    private String conversationId = "";

    @ColumnInfo(name = "title")
    private String title = "";

    @ColumnInfo(name = "message")
    private String message = "";

    @ColumnInfo(name = "time")
    private String time = "";

    @ColumnInfo(name = "conversationStatus")
    private ConversationStatus conversationStatus = ConversationStatus.UNDEFINE;

    @ColumnInfo(name = "conversationType")
    private ConversationType conversationType = ConversationType.UNDEFINE;

    @ColumnInfo(name = "fileType")
    private FileType fileType = FileType.NONE;

    @ColumnInfo(name = "fileAddress")
    private String fileAddress = "";

    @ColumnInfo(name = "imageUrl")
    private String imageUrl = "";

    @ColumnInfo(name = "imageRes")
    private String imageRes = "";

    @Ignore
    private boolean isPlaying;

    public ConversationModel() {
    }

    @Ignore
    public ConversationModel(ConversationModel model) {
        this.id = model.id;
        this.chatId = model.chatId;
        this.conversationId = model.conversationId;
        this.message = model.message;
        this.title = model.title;
        this.time = model.time;
        this.conversationStatus = model.conversationStatus;
        this.conversationType = model.conversationType;
        this.fileAddress = model.fileAddress;
        this.fileType = model.fileType;
        this.imageUrl = model.imageUrl;
        this.imageRes = model.imageRes;
    }

    @Ignore
    public ConversationModel(@NonNull String chatId, @NonNull String conversationId) {
        this.chatId = chatId;
        this.conversationId = conversationId;
    }

    public String getImageRes() {
        return imageRes;
    }

    public void setImageRes(String imageRes) {
        this.imageRes = imageRes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ConversationStatus getConversationStatus() {
        return conversationStatus;
    }

    public void setConversationStatus(ConversationStatus conversationStatus) {
        this.conversationStatus = conversationStatus;
    }

    public ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getServerSideId() {
        return serverSideId;
    }

    public void setServerSideId(String serverSideId) {
        this.serverSideId = serverSideId;
    }

    public String getFileName() {
//        String path = getFileAddress();
//        return path.substring(path.lastIndexOf("/") + 1);
        return message;
    }

    @BindingAdapter("conversationStatusIcon")
    public static void loadConversationStatusIcon(AppCompatImageView view, ConversationStatus conversationStatus) {
        if (conversationStatus == null)
            return;
        switch (conversationStatus) {
            case FAILED:
                view.setImageResource(R.drawable.ic_info_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case SEEN:
                view.setImageResource(R.drawable.ic_done_all_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.blue_light), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case DELIVERED:
                view.setImageResource(R.drawable.ic_done_all_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case SENT:
                view.setImageResource(R.drawable.ic_check_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case SENDING:
            default:
                view.setImageResource(R.drawable.ic_access_time_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    @BindingAdapter("bannerImage")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_user)
                .transform(new CenterInside())
                .apply(RequestOptions.circleCropTransform())
                .into(view);
    }

    @BindingAdapter("loadImageIndexBase")
    public static void loadImageIndexBase(ImageView view, String imageRes) {

        int res = R.drawable.ic_user;

        if (imageRes == null || imageRes.isEmpty()) {
            view.setImageResource(res);
            return;
        }

        int index = Integer.parseInt(imageRes);

        switch (index) {
            case 0:
                res = R.drawable.ic_avatar_0;
                break;
            case 1:
                res = R.drawable.ic_avatar_1;
                break;
            case 2:
                res = R.drawable.ic_avatar_2;
                break;
            case 3:
                res = R.drawable.ic_avatar_3;
                break;
            case 4:
                res = R.drawable.ic_avatar_4;
                break;
            case 5:
                res = R.drawable.ic_avatar_5;
                break;
            default:
                res = R.drawable.ic_user;
                break;
        }

        view.setImageResource(res);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !obj.getClass().equals(getClass()))
            return false;
        ConversationModel model = ((ConversationModel) obj);
        return model.getConversationId().equals(conversationId) &&
                model.getMessage().equals(message) &&
                model.getTitle().equals(title) &&
                model.conversationStatus.getValue().equals(conversationStatus.getValue()) &&
                ((model.fileAddress == null && fileAddress == null) || (model.fileAddress != null && model.fileAddress.equals(fileAddress))) &&
                ((model.imageUrl == null && imageUrl == null) || model.getImageUrl().equals(imageUrl));

    }

}
