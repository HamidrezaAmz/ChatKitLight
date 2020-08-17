package ir.vasl.chatkitlight.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.ui.callback.ClickCallback;

public class PermissionDialog extends Dialog {

    private TextView textViewAccept;
    private ClickCallback callback;

    public PermissionDialog(@NonNull Context context, ClickCallback callback) {
        super(context);
        setCancelable(false);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permission);
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textViewAccept = findViewById(R.id.textView_accept);
        textViewAccept.setOnClickListener(v -> {
            if (callback != null)
                callback.acceptClicked();
            dismiss();
        });
    }
}
