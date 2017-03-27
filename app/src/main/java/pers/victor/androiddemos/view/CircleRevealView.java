package pers.victor.androiddemos.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by Victor on 2017/3/5. (ง •̀_•́)ง
 */

public class CircleRevealView extends ImageView {
    private Path circlePath = new Path();
    private float radius = 0f;
    private ValueAnimator anim = new ValueAnimator();

    public CircleRevealView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        anim.setFloatValues(0f, w > h ? w : h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        circlePath.reset();
        circlePath.addCircle((getWidth() / 2), (getHeight() / 2), radius, Path.Direction.CW);
        canvas.save();
        canvas.clipPath(circlePath);
        super.onDraw(canvas);
        canvas.restore();
    }

    private void init() {
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(1500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    public void circleReveal() {
        if (!anim.isRunning()) {
            anim.start();
        }
    }
}
