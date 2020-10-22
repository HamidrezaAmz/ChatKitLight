package ir.vasl.chatkitlight.utils;

import android.view.View;
import android.view.animation.DecelerateInterpolator;

import static ir.vasl.chatkitlight.utils.Constants.ANIMATION_ALPHA_DURATION;

public class AnimView {

    public static void animate(View view, int delay, int yDelta) {

        view.animate().alpha(0f)
                .setDuration(0)
                .setInterpolator(new DecelerateInterpolator())
                .translationYBy(AndroidUtils.convertDpToPixel(yDelta, view.getContext()))
                .setStartDelay(0)
                .start();

        view.animate().alphaBy(1f)
                .setDuration(ANIMATION_ALPHA_DURATION)
                .setInterpolator(new DecelerateInterpolator())
                .translationYBy(AndroidUtils.convertDpToPixel(-1 * yDelta, view.getContext()))
                .setStartDelay(delay)
                .start();
    }
}
