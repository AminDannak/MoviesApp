package ir.amindannak.movies.ui.shared;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class Animations {

    public static ObjectAnimator fadeIn(View view) {
        long duration = 500;

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        animator.setDuration(duration);
        return animator;
    }

    public static ObjectAnimator fadeOut(View view) {
        long duration = 500;

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        animator.setDuration(duration);
        return animator;
    }

    public static void expandThenShrinkAnimation(View view) {
        long duration = 500;
        float expandMultiplier = 1.5f;

        ObjectAnimator xExpand = ObjectAnimator.ofFloat(view, "scaleX", 0f, expandMultiplier);
        ObjectAnimator yExpand = ObjectAnimator.ofFloat(view, "scaleY", 0f, expandMultiplier);
        ObjectAnimator xShrink = ObjectAnimator.ofFloat(view, "scaleX", expandMultiplier, 1f);
        ObjectAnimator yShrink = ObjectAnimator.ofFloat(view, "scaleY", expandMultiplier, 1f);

        AnimatorSet expandAnimationSet = new AnimatorSet().setDuration(duration);
        AnimatorSet shrinkAnimationSet = new AnimatorSet().setDuration(duration);

        expandAnimationSet.playTogether(xExpand, yExpand);
        shrinkAnimationSet.playTogether(xShrink, yShrink);

    }


}
