package ir.vasl.chatkitlight.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.base.BaseDialog;
import ir.vasl.chatkitlight.ui.callback.DialogMenuListener;

@SuppressWarnings("rawtypes")
public class DialogChatMenu extends BaseDialog implements View.OnClickListener {

    @SuppressWarnings("FieldCanBeLocal")
    private AppCompatButton buttonCopy, buttonResend, buttonDelete;

    private DialogMenuListener dialogMenuListener;

    private Object menuItem;

    public DialogChatMenu(Context context) {
        super(context);
    }

    public DialogChatMenu(Context context, int theme) {
        super(context, theme);
    }

    protected DialogChatMenu(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setDialogMenuListener(DialogMenuListener dialogMenuListener) {
        this.dialogMenuListener = dialogMenuListener;
    }

    public void setMenuItem(Object menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.layout_dialog_chat_menu;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttonCopy = findViewById(R.id.button_copy);
        buttonResend = findViewById(R.id.button_resend);
        buttonDelete = findViewById(R.id.button_delete);

        buttonCopy.setOnClickListener(this);
        buttonResend.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {

        if (dialogMenuListener == null)
            return;

        if (v.getId() == R.id.button_copy) {
            dialogMenuListener.onCopyMessageClicked(menuItem);
            dismiss();
        } else if (v.getId() == R.id.button_resend) {
            dialogMenuListener.onResendMessageClicked(menuItem);
            dismiss();
        } else if (v.getId() == R.id.button_delete) {
            dialogMenuListener.onDeleteMessageClicked(menuItem);
            dismiss();
        }
    }
}

