package pers.victor.androiddemos.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import pers.victor.androiddemos.util.DisplayUtil;

/**
 * Created by Victor on 2017/4/28. (ง •̀_•́)ง
 */

public class PathAnimView extends View {
    private PathMeasure pathMeasure;
    private ValueAnimator animator;
    private Path pathCircle;
    private Path dstCircle;
    private Path pathSuc;
    private Path dstSuc;
    private Path pathFail;
    private Path dstFail;
    private Paint paint;
    private Paint paintFail;
    private int strokeWidth = DisplayUtil.dp2px(10);
    private int colorGreen = Color.parseColor("#5EA35F");
    private int colorRed = Color.parseColor("#CB393A");
    private boolean isChangePath;
    private boolean isSuccess;

    public PathAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w * h != 0) {
            pathCircle.reset();
            pathCircle.addCircle(w / 2, h / 2, w / 2 - strokeWidth / 2, Path.Direction.CW);
            pathSuc.reset();
            pathSuc.moveTo(w / 2 - w / 20 - w / 5, h * 3 / 5 - h / 6);
            pathSuc.lineTo(w / 2 - w / 20, h * 3 / 5);
            pathSuc.lineTo(w / 2 - w / 20 + w / 3, h * 3 / 5 - h / 4);
            pathFail.reset();
            pathFail.moveTo(w / 2 + (w / 3) * cos(45), h / 2 - (h / 3) * sin(45));
            pathFail.lineTo(w / 2 - (w / 3) * cos(45), h / 2 + (h / 3) * sin(45));
            pathFail.moveTo(w / 2 - (w / 3) * cos(45), h / 2 - (h / 3) * sin(45));
            pathFail.lineTo(w / 2 + (w / 3) * cos(45), h / 2 + (h / 3) * sin(45));
        }
    }

    private void init() {
        pathCircle = new Path();
        dstCircle = new Path();
        dstFail = new Path();
        pathSuc = new Path();
        dstSuc = new Path();
        pathFail = new Path();
        dstFail = new Path();
        pathMeasure = new PathMeasure();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(colorGreen);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paintFail = new Paint();
        paintFail.setAntiAlias(true);
        paintFail.setDither(true);
        paintFail.setStrokeCap(Paint.Cap.ROUND);
        paintFail.setStrokeJoin(Paint.Join.ROUND);
        paintFail.setColor(colorRed);
        paintFail.setStrokeWidth(strokeWidth);
        paintFail.setStyle(Paint.Style.STROKE);
        animator = new ValueAnimator();
        animator.setFloatValues(0, 2);
        animator.setDuration(2000);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            if (value <= 1) {
                pathMeasure.getSegment(0, value * pathMeasure.getLength(), dstCircle, true);
            } else {
                if (!isChangePath) {
                    pathMeasure.getSegment(0, pathMeasure.getLength(), dstCircle, true);
                    pathMeasure.setPath(isSuccess ? pathSuc : pathFail, false);
                    isChangePath = true;
                }
                pathMeasure.getSegment(0, (value - 1) * pathMeasure.getLength(), isSuccess ? dstSuc : dstFail, true);
            }
            invalidate();
        });
    }

    public void startAnim() {
        pathMeasure.setPath(pathCircle, false);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(dstCircle, paint);
        canvas.drawPath(dstSuc, paint);
        canvas.drawPath(dstFail, paint);
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    private float cos(int degree) {
        return (float) Math.cos(Math.toRadians(degree));
    }

    private float sin(int degree) {
        return (float) Math.sin(Math.toRadians(degree));
    }
}
