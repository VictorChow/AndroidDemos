package pers.victor.androiddemos.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import pers.victor.androiddemos.util.DisplayUtil;

/**
 * Created by Victor on 2017/4/28. (ง •̀_•́)ง
 */

public class RadarView extends View {
    private Paint paintRadar;
    private Paint paintBorder;
    private Paint paintLine;
    private RectF rectF;
    private int[] colors = new int[]{Color.parseColor("#1F558A"), Color.parseColor("#549DDA")};
    private ValueAnimator animator;
    private float rotateAngle;
    private int borderSize = DisplayUtil.dp2px(15);
    private int borderColor = Color.parseColor("#B7DDFC");
    private int lineSize = 1;

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w * h != 0) {
            paintRadar.setShader(new SweepGradient(w / 2, h / 2, colors, null));
            rectF.set(borderSize, borderSize, w - borderSize, h - borderSize);
        }
    }

    private void init() {
        paintRadar = new Paint();
        paintRadar.setAntiAlias(true);
        paintRadar.setStyle(Paint.Style.FILL);
        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(borderSize);
        paintBorder.setColor(borderColor);
        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(lineSize);
        paintLine.setColor(borderColor);
        rectF = new RectF();
        animator = new ValueAnimator();
        animator.setFloatValues(0, 359);
        animator.setDuration(8000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(animation -> {
            rotateAngle = (float) animation.getAnimatedValue();
            postInvalidate();
        });
    }

    public void startAnim() {
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        int cHeight = height / 2;
        int cWidth = width / 2;
        int radius = width / 2 - borderSize;
        canvas.save();
        canvas.rotate(rotateAngle, cWidth, cHeight);
        canvas.drawArc(rectF, 0, 360, true, paintRadar);
        canvas.restore();
        canvas.drawCircle(cWidth, cHeight, radius, paintBorder);
        canvas.drawCircle(cWidth, cHeight, radius / 2, paintLine);
        canvas.drawCircle(cWidth, cHeight, radius * 3 / 4, paintLine);
        canvas.drawCircle(cWidth, cHeight, radius / 4, paintLine);
        canvas.drawLine(borderSize / 2 + 5, cHeight, width - borderSize / 2 - 5, cHeight, paintLine);
        canvas.drawLine(cWidth, borderSize / 2 + 5, cWidth, height - borderSize / 2 - 5, paintLine);
        canvas.drawLine(cWidth - radius * cos(45), cHeight - radius * sin(45), cWidth + radius * cos(45), cHeight + radius * sin(45), paintLine);
        canvas.drawLine(cWidth + radius * cos(45), cHeight - radius * sin(45), cWidth - radius * cos(45), cHeight + radius * sin(45), paintLine);
    }

    private float cos(int degree) {
        return (float) Math.cos(Math.toRadians(degree));
    }

    private float sin(int degree) {
        return (float) Math.sin(Math.toRadians(degree));
    }

}
