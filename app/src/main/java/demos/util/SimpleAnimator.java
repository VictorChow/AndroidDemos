package demos.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by victor on 16-7-7
 */
public final class SimpleAnimator {
    private View targetView;
    private long duration = 300L;
    private String property = "x";
    private Number[] values;
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    private AnimatorListener listener;
    private ValueAnimator.AnimatorUpdateListener updateListener;

    private SimpleAnimator() {
    }

    private SimpleAnimator(View targetView) {
        this.targetView = targetView;
    }

    public static SimpleAnimator create(View targetView) {
        return new SimpleAnimator(targetView);
    }

    public SimpleAnimator values(Number... values) {
        this.values = values;
        return this;
    }

    public SimpleAnimator duration(long duration) {
        this.duration = duration;
        return this;
    }

    public SimpleAnimator property(String property) {
        this.property = property;
        return this;
    }

    public SimpleAnimator interpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    public SimpleAnimator listener(AnimatorListener listener) {
        this.listener = listener;
        return this;
    }

    public SimpleAnimator updateListener(ValueAnimator.AnimatorUpdateListener updateListener) {
        this.updateListener = updateListener;
        return this;
    }

    public void go() {
        if (targetView == null)
            return;
        if (values.length < 2) {
            throw new IllegalArgumentException("values起码得两个值啊！");
        }
        ValueAnimator animator;
        if (property.contentEquals("height") || property.contentEquals("width")) {
            final int[] intValues = new int[this.values.length];
            for (int i = 0; i < this.values.length; i++) {
                intValues[i] = (int) this.values[i];
            }
            final ViewGroup.LayoutParams lp = targetView.getLayoutParams();
            animator = new ValueAnimator();
            animator.setIntValues(intValues);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    if (property.contentEquals("width")) {
                        lp.width = value;
                    }
                    if (property.contentEquals("height")) {
                        lp.height = value;
                    }
                    targetView.setLayoutParams(lp);
                }
            });
            animator.addListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    targetView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (intValues[intValues.length - 1] == 0) {
                        targetView.setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {
            final float[] floatValues = new float[this.values.length];
            for (int i = 0; i < this.values.length; i++) {
                floatValues[i] = (float) this.values[i];
            }
            animator = ObjectAnimator.ofFloat(this.targetView, property, floatValues);

            animator.addListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    targetView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (property.contentEquals("alpha") && floatValues[floatValues.length - 1] == 0f) {
                        targetView.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        animator.setDuration(duration);
        animator.setInterpolator(interpolator);
        if (listener != null) {
            animator.addListener(listener);
        }
        if (updateListener != null) {
            animator.addUpdateListener(updateListener);
        }
        animator.start();
    }

    public static abstract class AnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}