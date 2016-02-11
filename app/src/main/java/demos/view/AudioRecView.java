package demos.view;

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
    private int mRectCount = 15;
    private int mWidth;
    private int mRectHeight;
    private int mRectWidth;
    private int offset = 1;
    private Paint mPaint;
    private LinearGradient mLinearGradient;

    public AudioRecView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = getWidth();
        mRectHeight = getHeight();
        mRectWidth = (int) (mWidth * 0.6 / mRectCount);
        mPaint = new Paint();
        mLinearGradient = new LinearGradient(0, 0, mRectWidth, mRectHeight, Color.YELLOW, Color.BLUE, Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
        mPaint.setColor(Color.DKGRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mRectCount; i++) {
            canvas.drawRect((float) (mWidth * 0.4 / 2 + mRectWidth * i + offset),
                    (float) (mRectHeight * Math.random()),
                    (float) (mWidth * 0.4 / 2 + mRectWidth * (i + 1)),
                    (float) mRectHeight,
                    mPaint);
        }
        postInvalidateDelayed(300);
    }
}
