package ir.vasl.chatkitlight.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.ui.callback.ClickCallback;

public class PermissionDialog extends Dialog {

    private AppCompatButton buttonAccept;
    private ClickCallback callback;

    public PermissionDialog(@NonNull Context context, ClickCallback callback) {
        super(context);
        setCancelable(true);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permission);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        buttonAccept = findViewById(R.id.button_accept);
        buttonAccept.setOnClickListener(v -> {
            if (callback != null)
                callback.acceptClicked();
            dismiss();
        });
    }
}
