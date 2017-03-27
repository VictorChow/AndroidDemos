package pers.victor.androiddemos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Victor on 16/1/8.
 * 滑动闪动的TextView
 */
public class ShaderTextView extends TextView {
    private int mViewWidth;
    private LinearGradient mLinearGradient;
    private Matrix mMatrix;
    private int mTranslate;

    public ShaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0) {
                mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                        new int[]{Color.parseColor("#f4f4f4"), Color.parseColor("#A020F0"), Color.parseColor("#f4f4f4")},
                        null, Shader.TileMode.CLAMP);
                getPaint().setShader(mLinearGradient);
                mMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMatrix != null) {
            mTranslate += mViewWidth / 10;
            if (mTranslate > mViewWidth * 2) {
                mTranslate = -mViewWidth;
            }
            mMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mMatrix);
            postInvalidateDelayed(50);
        }
    }
}
