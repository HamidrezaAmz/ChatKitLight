package ir.vasl.chatkitlight.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;

import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordView;

import java.lang.reflect.Field;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.ui.callback.AttachmentsListener;
import ir.vasl.chatkitlight.ui.callback.InputListener;
import ir.vasl.chatkitlight.ui.callback.TypingListener;
import ir.vasl.chatkitlight.ui.customview.AnimButton;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ConversationInput
        extends FrameLayout
        implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener, OnRecordListener, OnRecordClickListener {

    protected EditText conversationInput;
    protected ImageButton attachmentButton;
    protected RecordView recordView;
    protected AnimButton animButton;
    protected AppCompatImageView extraOptionButton;
    protected View blockerView;

    private CharSequence input;
    private InputListener inputListener;
    private AttachmentsListener attachmentsListener;
    private OnRecordListener onRecordListener;
    private TypingListener typingListener;
    private Runnable typingTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTyping) {
                isTyping = false;
                if (typingListener != null) typingListener.onStopTyping();
            }
        }
    };
    private int delayTypingStatusMillis;
    private boolean isTyping;
    private boolean lastFocus;
    private boolean canShowAttachment = true;
    private boolean canShowExtraOption = true;
    private boolean canShowVoiceRecording = true;

    public ConversationInput(Context context) {
        super(context);
        init(context);
    }

    public ConversationInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ConversationInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    public void setAttachmentsListener(AttachmentsListener attachmentsListener) {
        this.attachmentsListener = attachmentsListener;
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    public boolean canShowExtraOption() {
        return canShowExtraOption;
    }

    public void setCanShowExtraOption(boolean canShowExtraOption) {
        this.canShowExtraOption = canShowExtraOption;
    }

    public boolean canShowAttachment() {
        return canShowAttachment;
    }

    public void setCanShowAttachment(boolean canShowAttachment) {
        this.canShowAttachment = canShowAttachment;
    }

    public boolean canShowVoiceRecording() {
        return canShowVoiceRecording;
    }

    public void setCanShowVoiceRecording(boolean canShowVoiceRecording) {
        this.canShowVoiceRecording = canShowVoiceRecording;
        if (!canShowVoiceRecording) {
            animButton.setCurrAnimButtonState(AnimButton.AnimButtonState.TYPING);
            animButton.setListenForRecord(false);
        }
    }

    public EditText getInputEditText() {
        return conversationInput;
    }

    public ImageButton getAttachmentButton() {
        return attachmentButton;
    }

    public AppCompatImageView getExtraOptionButton() {
        return extraOptionButton;
    }

    public View getShowBlocker() {
        return blockerView;
    }

    public void setShowBlockerView(boolean showBlockerView) {
        this.blockerView.setVisibility(showBlockerView ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.anim_button) {
            boolean isSubmitted = onSubmit();
            if (isSubmitted) {
                conversationInput.setText("");
            }
            removeCallbacks(typingTimerRunnable);
            post(typingTimerRunnable);
        } else if (id == R.id.attachmentButton) {
            onAddAttachments();
        } else if (id == R.id.imageView_extra_option) {
            if (inputListener != null) {
                inputListener.extraOptionClicked();
            }
        }
    }

    /**
     * This method is called to notify you that, within s,
     * the count characters beginning at start have just replaced old text that had length before
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (!canShowVoiceRecording) {
            animButton.setCurrAnimButtonState(AnimButton.AnimButtonState.TYPING);
            animButton.setListenForRecord(false);
            return;
        }
        input = s;
        animButton.setCurrAnimButtonState(input.length() > 0 ? AnimButton.AnimButtonState.TYPING : AnimButton.AnimButtonState.RECORDING);
        animButton.setListenForRecord(input.length() <= 0);

        if (s.length() > 0) {
            if (!isTyping) {
                isTyping = true;
                if (typingListener != null) typingListener.onStartTyping();
            }
            removeCallbacks(typingTimerRunnable);
            postDelayed(typingTimerRunnable, delayTypingStatusMillis);
        }
    }

    /**
     * This method is called to notify you that, within s,
     * the count characters beginning at start are about to be replaced by new text with length after.
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    /**
     * This method is called to notify you that, somewhere within s, the text has been changed.
     */
    @Override
    public void afterTextChanged(Editable editable) {
        //do nothing
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (lastFocus && !hasFocus && typingListener != null) {
            typingListener.onStopTyping();
        }
        lastFocus = hasFocus;
    }

    private boolean onSubmit() {
        return inputListener != null && inputListener.onSubmit(input);
    }

    private void onAddAttachments() {
        if (attachmentsListener != null)
            attachmentsListener.onAddAttachments();
    }

    @Override
    public void onStart() {
        if (onRecordListener != null)
            onRecordListener.onStart();

        getInputEditText().setVisibility(GONE);
        getAttachmentButton().setVisibility(GONE);
        getExtraOptionButton().setVisibility(GONE);

    }

    @Override
    public void onCancel() {
        if (onRecordListener != null)
            onRecordListener.onCancel();

        getInputEditText().setVisibility(VISIBLE);

        if (canShowAttachment)
            getAttachmentButton().setVisibility(VISIBLE);

        if (canShowExtraOption)
            getExtraOptionButton().setVisibility(VISIBLE);
    }

    @Override
    public void onFinish(long recordTime) {
        if (onRecordListener != null)
            onRecordListener.onFinish(recordTime);

        getInputEditText().setVisibility(VISIBLE);

        if (canShowAttachment)
            getAttachmentButton().setVisibility(VISIBLE);

        if (canShowExtraOption)
            getExtraOptionButton().setVisibility(VISIBLE);
    }

    @Override
    public void onLessThanSecond() {

        if (onRecordListener != null)
            onRecordListener.onLessThanSecond();

        getInputEditText().setVisibility(VISIBLE);
        getAttachmentButton().setVisibility(VISIBLE);

        if (!canShowAttachment)
            getAttachmentButton().setVisibility(GONE);
        else
            getAttachmentButton().setVisibility(VISIBLE);

    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
        MessageInputStyle style = MessageInputStyle.parse(context, attrs);

        this.conversationInput.setMaxLines(style.getInputMaxLines());
        this.conversationInput.setHint(style.getInputHint());
        this.conversationInput.setText(style.getInputText());
        this.conversationInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getInputTextSize());
        this.conversationInput.setTextColor(style.getInputTextColor());
        this.conversationInput.setHintTextColor(style.getInputHintColor());
        ViewCompat.setBackground(this.conversationInput, style.getInputBackground());
        setCursor(style.getInputCursorDrawable());

        this.attachmentButton.setVisibility(style.showAttachmentButton() ? VISIBLE : GONE);
        this.attachmentButton.setImageDrawable(style.getAttachmentButtonIcon());
        this.attachmentButton.getLayoutParams().width = style.getAttachmentButtonWidth();
        this.attachmentButton.getLayoutParams().height = style.getAttachmentButtonHeight();
        ViewCompat.setBackground(this.attachmentButton, style.getAttachmentButtonBackground());

        this.delayTypingStatusMillis = style.getDelayTypingStatus();
    }

    private void init(Context context) {
        inflate(context, R.layout.view_message_input, this);

        conversationInput = findViewById(R.id.messageInput);
        attachmentButton = findViewById(R.id.attachmentButton);
        recordView = findViewById(R.id.record_view);
        animButton = findViewById(R.id.anim_button);
        extraOptionButton = findViewById(R.id.imageView_extra_option);
        blockerView = findViewById(R.id.blocker_view);

        attachmentButton.setOnClickListener(this);
        extraOptionButton.setOnClickListener(this);
        conversationInput.addTextChangedListener(this);
        conversationInput.setText("");
        conversationInput.setOnFocusChangeListener(this);
        animButton.setRecordView(recordView);

        animButton.setOnRecordClickListener(this);
        recordView.setOnRecordListener(this);
    }

    private void setCursor(Drawable drawable) {
        if (drawable == null) return;

        try {
            final Field drawableResField = TextView.class.getDeclaredField("mCursorDrawableRes");
            drawableResField.setAccessible(true);

            final Object drawableFieldOwner;
            final Class<?> drawableFieldClass;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                drawableFieldOwner = this.conversationInput;
                drawableFieldClass = TextView.class;
            } else {
                final Field editorField = TextView.class.getDeclaredField("mEditor");
                editorField.setAccessible(true);
                drawableFieldOwner = editorField.get(this.conversationInput);
                drawableFieldClass = drawableFieldOwner.getClass();
            }
            final Field drawableField = drawableFieldClass.getDeclaredField("mCursorDrawable");
            drawableField.setAccessible(true);
            drawableField.set(drawableFieldOwner, new Drawable[]{drawable, drawable});
        } catch (Exception ignored) {
        }
    }

    public void setTypingListener(TypingListener typingListener) {
        this.typingListener = typingListener;
    }

}
