package pers.victor.androiddemos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Victor on 16/1/8.
 * 模拟音乐播放器的波形
 */
public class AudioRecView extends View {
    private int mRectCount = 25;
    private int mRectHeight;
    private int mRectWidth;
    private Paint mPaint;

    public AudioRecView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        int mWidth = getWidth();
        mRectHeight = getHeight();
        mRectWidth = mWidth / mRectCount;
        mPaint = new Paint();
        LinearGradient mLinearGradient = new LinearGradient(0, 0, mRectWidth, mRectHeight, Color.parseColor("#e9d47b"), Color.parseColor("#8558d3"), Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
        mPaint.setColor(Color.DKGRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mRectCount; i++) {
            int offset = 1;
            canvas.drawRect((float) (mRectWidth * i + offset),
                    (float) (mRectHeight * Math.random()),
                    (float) (mRectWidth * (i + 1)),
                    (float) mRectHeight,
                    mPaint);
        }
        postInvalidateDelayed(250);
    }
}
