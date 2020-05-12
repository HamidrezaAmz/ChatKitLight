package ir.vasl.chatkitlight.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AppCompatDialog;

import java.util.Objects;

import ir.vasl.chatkitlight.R;

public abstract class BaseDialog extends AppCompatDialog {

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected abstract int getDialogLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(getDialogLayout());
        initDialog();
    }

    private void initDialog() {
        int width = getWidthOfDialog();
        Objects.requireNonNull(getWindow()).setLayout(
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private int getWidthOfDialog() {
        float width = Float.valueOf(getContext().getResources().getString(R.string.dialog_default_width_percentage));
        return (int) (getContext().getResources().getDisplayMetrics().widthPixels * width);
    }

}

