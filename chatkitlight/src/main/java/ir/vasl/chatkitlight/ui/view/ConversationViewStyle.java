package ir.vasl.chatkitlight.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import ir.vasl.chatkitlight.R;

public class ConversationViewStyle extends Style {

    private static boolean DEFAULT_CAN_SHOW_DIALOG = false;

    private boolean canShowDialog;

    static ConversationViewStyle parse(Context context, AttributeSet attrs) {

        ConversationViewStyle style = new ConversationViewStyle(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConversationView);

        style.canShowDialog = typedArray.getBoolean(R.styleable.ConversationView_canShowDialog, DEFAULT_CAN_SHOW_DIALOG);

        typedArray.recycle();

        return style;
    }

    private ConversationViewStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean canShowDialog() {
        return canShowDialog;
    }

}
