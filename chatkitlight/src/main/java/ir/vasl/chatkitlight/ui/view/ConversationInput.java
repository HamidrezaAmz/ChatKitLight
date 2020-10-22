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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;

import java.lang.reflect.Field;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.ui.callback.AttachmentsListener;
import ir.vasl.chatkitlight.ui.callback.InputListener;
import ir.vasl.chatkitlight.ui.callback.TypingListener;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ConversationInput
        extends RelativeLayout
        implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener, OnRecordListener {

    protected EditText ConversationInput;
    protected ImageButton messageSendButton;
    protected ImageButton attachmentButton;
    protected RecordView recordView;
    protected RecordButton recordButton;

    private CharSequence input;
    private InputListener inputListener;
    private AttachmentsListener attachmentsListener;
    private boolean isTyping;
    private TypingListener typingListener;
    private int delayTypingStatusMillis;
    private Runnable typingTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTyping) {
                isTyping = false;
                if (typingListener != null) typingListener.onStopTyping();
            }
        }
    };
    private boolean lastFocus;

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

    public EditText getInputEditText() {
        return ConversationInput;
    }

    public ImageButton getMessageSendButton() {
        return messageSendButton;
    }

    public ImageButton getAttachmentButton() {
        return attachmentButton;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.messageSendButton) {
            boolean isSubmitted = onSubmit();
            if (isSubmitted) {
                ConversationInput.setText("");
            }
            removeCallbacks(typingTimerRunnable);
            post(typingTimerRunnable);
        } else if (id == R.id.attachmentButton) {
            onAddAttachments();
        }
    }

    /**
     * This method is called to notify you that, within s,
     * the count characters beginning at start have just replaced old text that had length before
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        input = s;
        messageSendButton.setEnabled(input.length() > 0);
        recordButton.setVisibility(input.length() > 0 ? INVISIBLE : VISIBLE);
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
//        if (attachmentsListener != null) attachmentsListener.onAddAttachments();
    }

    @Override
    public void onStart() {
        getInputEditText().setVisibility(INVISIBLE);
        getMessageSendButton().setVisibility(INVISIBLE);
        getAttachmentButton().setVisibility(INVISIBLE);
    }

    @Override
    public void onCancel() {
        getInputEditText().setVisibility(VISIBLE);
        getMessageSendButton().setVisibility(VISIBLE);
        getAttachmentButton().setVisibility(VISIBLE);
    }

    @Override
    public void onFinish(long recordTime) {
        getInputEditText().setVisibility(VISIBLE);
        getMessageSendButton().setVisibility(VISIBLE);
        getAttachmentButton().setVisibility(VISIBLE);
    }

    @Override
    public void onLessThanSecond() {
        getInputEditText().setVisibility(VISIBLE);
        getMessageSendButton().setVisibility(VISIBLE);
        getAttachmentButton().setVisibility(VISIBLE);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
        MessageInputStyle style = MessageInputStyle.parse(context, attrs);

        this.ConversationInput.setMaxLines(style.getInputMaxLines());
        this.ConversationInput.setHint(style.getInputHint());
        this.ConversationInput.setText(style.getInputText());
        this.ConversationInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, style.getInputTextSize());
        this.ConversationInput.setTextColor(style.getInputTextColor());
        this.ConversationInput.setHintTextColor(style.getInputHintColor());
        ViewCompat.setBackground(this.ConversationInput, style.getInputBackground());
        setCursor(style.getInputCursorDrawable());

        this.attachmentButton.setVisibility(style.showAttachmentButton() ? VISIBLE : GONE);
        this.attachmentButton.setImageDrawable(style.getAttachmentButtonIcon());
        this.attachmentButton.getLayoutParams().width = style.getAttachmentButtonWidth();
        this.attachmentButton.getLayoutParams().height = style.getAttachmentButtonHeight();
        ViewCompat.setBackground(this.attachmentButton, style.getAttachmentButtonBackground());

        // this.attachmentButtonSpace.setVisibility(style.showAttachmentButton() ? VISIBLE : GONE);
        // this.attachmentButtonSpace.getLayoutParams().width = style.getAttachmentButtonMargin();

        this.messageSendButton.setImageDrawable(style.getInputButtonIcon());
        this.messageSendButton.getLayoutParams().width = style.getInputButtonWidth();
        this.messageSendButton.getLayoutParams().height = style.getInputButtonHeight();
        // this.sendButtonSpace.getLayoutParams().width = style.getInputButtonMargin();
        ViewCompat.setBackground(messageSendButton, style.getInputButtonBackground());

        /*if (getPaddingLeft() == 0
                && getPaddingRight() == 0
                && getPaddingTop() == 0
                && getPaddingBottom() == 0) {
            setPadding(
                    style.getInputDefaultPaddingLeft(),
                    style.getInputDefaultPaddingTop(),
                    style.getInputDefaultPaddingRight(),
                    style.getInputDefaultPaddingBottom()
            );
        }*/
        this.delayTypingStatusMillis = style.getDelayTypingStatus();
    }

    private void init(Context context) {
        inflate(context, R.layout.view_message_input, this);

        ConversationInput = findViewById(R.id.messageInput);
        messageSendButton = findViewById(R.id.messageSendButton);
        attachmentButton = findViewById(R.id.attachmentButton);
        // sendButtonSpace = findViewById(R.id.sendButtonSpace);
        // attachmentButtonSpace = findViewById(R.id.attachmentButtonSpace);
        recordView = (RecordView) findViewById(R.id.record_view);
        recordButton = (RecordButton) findViewById(R.id.record_button);

        messageSendButton.setOnClickListener(this);
        attachmentButton.setOnClickListener(this);
        ConversationInput.addTextChangedListener(this);
        ConversationInput.setText("");
        ConversationInput.setOnFocusChangeListener(this);
        recordButton.setRecordView(recordView);
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
                drawableFieldOwner = this.ConversationInput;
                drawableFieldClass = TextView.class;
            } else {
                final Field editorField = TextView.class.getDeclaredField("mEditor");
                editorField.setAccessible(true);
                drawableFieldOwner = editorField.get(this.ConversationInput);
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
