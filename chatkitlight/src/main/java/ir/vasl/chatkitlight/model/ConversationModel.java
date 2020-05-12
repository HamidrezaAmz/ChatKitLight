package ir.vasl.chatkitlight.model;

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

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.utils.Constants;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;

@Entity(tableName = Constants.TABLE_NAME)
@TypeConverters({ConversationStatus.class, ConversationType.class})
public class ConversationModel {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

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
    }

    @Ignore
    public ConversationModel(@NonNull String chatId, @NonNull String conversationId) {
        this.chatId = chatId;
        this.conversationId = conversationId;
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

    @BindingAdapter("conversationStatusIcon")
    public static void loadConversationStatusIcon(AppCompatImageView view, ConversationStatus conversationStatus) {
        if (conversationStatus == null)
            return;
        switch (conversationStatus) {
            case FAILED:
                view.setImageResource(R.drawable.ic_info_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.failed), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case SEEN:
                view.setImageResource(R.drawable.ic_done_all_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.blue_light), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case DELIVERED:
                view.setImageResource(R.drawable.ic_done_all_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case SENT:
                view.setImageResource(R.drawable.ic_check_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
            case SENDING:
            default:
                view.setImageResource(R.drawable.ic_access_time_black_24dp);
                view.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !obj.getClass().equals(getClass()))
            return false;
        ConversationModel model = ((ConversationModel) obj);
        return model.getId() == id &&
                model.getConversationId().equals(conversationId) &&
                model.getMessage().equals(message) &&
                model.getTitle().equals(title) &&
                model.getTime().equals(time) &&
                model.conversationStatus.getValue().equals(conversationStatus.getValue());

    }

}
