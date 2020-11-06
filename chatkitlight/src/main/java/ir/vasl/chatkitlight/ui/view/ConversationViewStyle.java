package ir.vasl.chatkitlight.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.utils.globalEnums.ChatStyleEnum;

import static ir.vasl.chatkitlight.utils.Constants.DEFAULT_CAN_SHOW_DIALOG;
import static ir.vasl.chatkitlight.utils.Constants.DEFAULT_CLIENT_BUBBLE_COLOR;
import static ir.vasl.chatkitlight.utils.Constants.DEFAULT_SERVER_BUBBLE_COLOR;

public class ConversationViewStyle extends Style {

    private boolean canShowDialog;
    private int clientBubbleColor;
    private int serverBubbleColor;
    private int chatStyle;
    private boolean canShowExtraOptionButton;

    static ConversationViewStyle parse(Context context, AttributeSet attrs) {

        ConversationViewStyle style = new ConversationViewStyle(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConversationView);

        style.canShowDialog = typedArray.getBoolean(R.styleable.ConversationView_canShowDialog, DEFAULT_CAN_SHOW_DIALOG);
        style.clientBubbleColor = typedArray.getResourceId(R.styleable.ConversationView_clientBubbleColor, DEFAULT_CLIENT_BUBBLE_COLOR);
        style.serverBubbleColor = typedArray.getResourceId(R.styleable.ConversationView_serverBubbleColor, DEFAULT_SERVER_BUBBLE_COLOR);
        style.chatStyle = typedArray.getInteger(R.styleable.ConversationView_chatStyle, ChatStyleEnum.DEFAULT.getOrdinal());
        style.canShowExtraOptionButton = typedArray.getBoolean(R.styleable.ConversationView_canShowExtraOptionButton, false);
        typedArray.recycle();

        return style;
    }

    private ConversationViewStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean canShowDialog() {
        return canShowDialog;
    }

    public boolean canShowExtraOptionButton() {
        return canShowExtraOptionButton;
    }

    public int getClientBubbleColor() {
        return clientBubbleColor;
    }

    public int getServerBubbleColor() {
        return serverBubbleColor;
    }

    public int getChatStyle() {
        return chatStyle;
    }
}
