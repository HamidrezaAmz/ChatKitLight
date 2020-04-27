package ir.vasl.chatkitlight.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.graphics.drawable.DrawableCompat;

import ir.vasl.chatkitlight.R;

/**
 * Style for MessageInputStyle customization by xml attributes
 */
@SuppressWarnings("WeakerAccess")
class MessageInputStyle extends Style {

    private static final int DEFAULT_MAX_LINES = 5;
    private static final int DEFAULT_DELAY_TYPING_STATUS = 1500;

    private boolean showAttachmentButton;

    private int attachmentButtonBackground;
    private int attachmentButtonDefaultBgColor;
    private int attachmentButtonDefaultBgPressedColor;
    private int attachmentButtonDefaultBgDisabledColor;

    private int attachmentButtonIcon;
    private int attachmentButtonDefaultIconColor;
    private int attachmentButtonDefaultIconPressedColor;
    private int attachmentButtonDefaultIconDisabledColor;

    private int attachmentButtonWidth;
    private int attachmentButtonHeight;
    private int attachmentButtonMargin;

    private int inputButtonBackground;
    private int inputButtonDefaultBgColor;
    private int inputButtonDefaultBgPressedColor;
    private int inputButtonDefaultBgDisabledColor;

    private int inputButtonIcon;
    private int inputButtonDefaultIconColor;
    private int inputButtonDefaultIconPressedColor;
    private int inputButtonDefaultIconDisabledColor;

    private int inputButtonWidth;
    private int inputButtonHeight;
    private int inputButtonMargin;

    private int inputMaxLines;
    private String inputHint;
    private String inputText;

    private int inputTextSize;
    private int inputTextColor;
    private int inputHintColor;

    private Drawable inputBackground;
    private Drawable inputCursorDrawable;

    private int inputDefaultPaddingLeft;
    private int inputDefaultPaddingRight;
    private int inputDefaultPaddingTop;
    private int inputDefaultPaddingBottom;

    private int delayTypingStatus;

    static MessageInputStyle parse(Context context, AttributeSet attrs) {
        MessageInputStyle style = new MessageInputStyle(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConversationInput);

        style.showAttachmentButton = typedArray.getBoolean(R.styleable.ConversationInput_showAttachmentButton, false);

        style.attachmentButtonBackground = typedArray.getResourceId(R.styleable.ConversationInput_attachmentButtonBackground, -1);
        style.attachmentButtonDefaultBgColor = typedArray.getColor(R.styleable.ConversationInput_attachmentButtonDefaultBgColor,
                style.getColor(R.color.white_four));
        style.attachmentButtonDefaultBgPressedColor = typedArray.getColor(R.styleable.ConversationInput_attachmentButtonDefaultBgPressedColor,
                style.getColor(R.color.white_five));
        style.attachmentButtonDefaultBgDisabledColor = typedArray.getColor(R.styleable.ConversationInput_attachmentButtonDefaultBgDisabledColor,
                style.getColor(R.color.transparent));

        style.attachmentButtonIcon = typedArray.getResourceId(R.styleable.ConversationInput_attachmentButtonIcon, -1);
        style.attachmentButtonDefaultIconColor = typedArray.getColor(R.styleable.ConversationInput_attachmentButtonDefaultIconColor,
                style.getColor(R.color.cornflower_blue_two));
        style.attachmentButtonDefaultIconPressedColor = typedArray.getColor(R.styleable.ConversationInput_attachmentButtonDefaultIconPressedColor,
                style.getColor(R.color.cornflower_blue_two_dark));
        style.attachmentButtonDefaultIconDisabledColor = typedArray.getColor(R.styleable.ConversationInput_attachmentButtonDefaultIconDisabledColor,
                style.getColor(R.color.cornflower_blue_light_40));

        style.attachmentButtonWidth = typedArray.getDimensionPixelSize(R.styleable.ConversationInput_attachmentButtonWidth, style.getDimension(R.dimen.input_button_width));
        style.attachmentButtonHeight = typedArray.getDimensionPixelSize(R.styleable.ConversationInput_attachmentButtonHeight, style.getDimension(R.dimen.input_button_height));
        style.attachmentButtonMargin = typedArray.getDimensionPixelSize(R.styleable.ConversationInput_attachmentButtonMargin, style.getDimension(R.dimen.input_button_margin));

        style.inputButtonBackground = typedArray.getResourceId(R.styleable.ConversationInput_inputButtonBackground, -1);
        style.inputButtonDefaultBgColor = typedArray.getColor(R.styleable.ConversationInput_inputButtonDefaultBgColor,
                style.getColor(R.color.cornflower_blue_two));
        style.inputButtonDefaultBgPressedColor = typedArray.getColor(R.styleable.ConversationInput_inputButtonDefaultBgPressedColor,
                style.getColor(R.color.cornflower_blue_two_dark));
        style.inputButtonDefaultBgDisabledColor = typedArray.getColor(R.styleable.ConversationInput_inputButtonDefaultBgDisabledColor,
                style.getColor(R.color.white_four));

        style.inputButtonIcon = typedArray.getResourceId(R.styleable.ConversationInput_inputButtonIcon, -1);
        style.inputButtonDefaultIconColor = typedArray.getColor(R.styleable.ConversationInput_inputButtonDefaultIconColor,
                style.getColor(R.color.white));
        style.inputButtonDefaultIconPressedColor = typedArray.getColor(R.styleable.ConversationInput_inputButtonDefaultIconPressedColor,
                style.getColor(R.color.white));
        style.inputButtonDefaultIconDisabledColor = typedArray.getColor(R.styleable.ConversationInput_inputButtonDefaultIconDisabledColor,
                style.getColor(R.color.warm_grey));

        style.inputButtonWidth = typedArray.getDimensionPixelSize(R.styleable.ConversationInput_inputButtonWidth, style.getDimension(R.dimen.input_button_width));
        style.inputButtonHeight = typedArray.getDimensionPixelSize(R.styleable.ConversationInput_inputButtonHeight, style.getDimension(R.dimen.input_button_height));
        style.inputButtonMargin = typedArray.getDimensionPixelSize(R.styleable.ConversationInput_inputButtonMargin, style.getDimension(R.dimen.input_button_margin));

        style.inputMaxLines = typedArray.getInt(R.styleable.ConversationInput_inputMaxLines, DEFAULT_MAX_LINES);
        style.inputHint = typedArray.getString(R.styleable.ConversationInput_inputHint);
        style.inputText = typedArray.getString(R.styleable.ConversationInput_inputText);

        style.inputTextSize = typedArray.getDimensionPixelSize(R.styleable.ConversationInput_inputTextSize, style.getDimension(R.dimen.input_text_size));
        style.inputTextColor = typedArray.getColor(R.styleable.ConversationInput_inputTextColor, style.getColor(R.color.white));
        style.inputHintColor = typedArray.getColor(R.styleable.ConversationInput_inputHintColor, style.getColor(R.color.warm_grey_three));

        style.inputBackground = typedArray.getDrawable(R.styleable.ConversationInput_inputBackground);
        style.inputCursorDrawable = typedArray.getDrawable(R.styleable.ConversationInput_inputCursorDrawable);

        style.delayTypingStatus = typedArray.getInt(R.styleable.ConversationInput_delayTypingStatus, DEFAULT_DELAY_TYPING_STATUS);

        typedArray.recycle();

        style.inputDefaultPaddingLeft = style.getDimension(R.dimen.input_padding_left);
        style.inputDefaultPaddingRight = style.getDimension(R.dimen.input_padding_right);
        style.inputDefaultPaddingTop = style.getDimension(R.dimen.input_padding_top);
        style.inputDefaultPaddingBottom = style.getDimension(R.dimen.input_padding_bottom);

        return style;
    }

    private MessageInputStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Drawable getSelector(@ColorInt int normalColor, @ColorInt int pressedColor,
                                 @ColorInt int disabledColor, @DrawableRes int shape) {

        Drawable drawable = DrawableCompat.wrap(getVectorDrawable(shape)).mutate();
        DrawableCompat.setTintList(
                drawable,
                new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_enabled, -android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed},
                                new int[]{-android.R.attr.state_enabled}
                        },
                        new int[]{normalColor, pressedColor, disabledColor}
                ));
        return drawable;
    }

    protected boolean showAttachmentButton() {
        return showAttachmentButton;
    }

    protected Drawable getAttachmentButtonBackground() {
        if (attachmentButtonBackground == -1) {
            return getSelector(attachmentButtonDefaultBgColor, attachmentButtonDefaultBgPressedColor,
                    attachmentButtonDefaultBgDisabledColor, R.drawable.mask);
        } else {
            return getDrawable(attachmentButtonBackground);
        }
    }

    protected Drawable getAttachmentButtonIcon() {
        if (attachmentButtonIcon == -1) {
            return getSelector(attachmentButtonDefaultIconColor, attachmentButtonDefaultIconPressedColor,
                    attachmentButtonDefaultIconDisabledColor, R.drawable.ic_add_attachment);
        } else {
            return getDrawable(attachmentButtonIcon);
        }
    }

    protected int getAttachmentButtonWidth() {
        return attachmentButtonWidth;
    }

    protected int getAttachmentButtonHeight() {
        return attachmentButtonHeight;
    }

    protected int getAttachmentButtonMargin() {
        return attachmentButtonMargin;
    }

    protected Drawable getInputButtonBackground() {
        if (inputButtonBackground == -1) {
            return getSelector(inputButtonDefaultBgColor, inputButtonDefaultBgPressedColor,
                    inputButtonDefaultBgDisabledColor, R.drawable.mask);
        } else {
            return getDrawable(inputButtonBackground);
        }
    }

    protected Drawable getInputButtonIcon() {
        if (inputButtonIcon == -1) {
            return getSelector(inputButtonDefaultIconColor, inputButtonDefaultIconPressedColor,
                    inputButtonDefaultIconDisabledColor, R.drawable.ic_send);
        } else {
            return getDrawable(inputButtonIcon);
        }
    }

    protected int getInputButtonMargin() {
        return inputButtonMargin;
    }

    protected int getInputButtonWidth() {
        return inputButtonWidth;
    }

    protected int getInputButtonHeight() {
        return inputButtonHeight;
    }

    protected int getInputMaxLines() {
        return inputMaxLines;
    }

    protected String getInputHint() {
        return inputHint;
    }

    protected String getInputText() {
        return inputText;
    }

    protected int getInputTextSize() {
        return inputTextSize;
    }

    protected int getInputTextColor() {
        return inputTextColor;
    }

    protected int getInputHintColor() {
        return inputHintColor;
    }

    protected Drawable getInputBackground() {
        return inputBackground;
    }

    protected Drawable getInputCursorDrawable() {
        return inputCursorDrawable;
    }

    protected int getInputDefaultPaddingLeft() {
        return inputDefaultPaddingLeft;
    }

    protected int getInputDefaultPaddingRight() {
        return inputDefaultPaddingRight;
    }

    protected int getInputDefaultPaddingTop() {
        return inputDefaultPaddingTop;
    }

    protected int getInputDefaultPaddingBottom() {
        return inputDefaultPaddingBottom;
    }

    int getDelayTypingStatus() {
        return delayTypingStatus;
    }

}
