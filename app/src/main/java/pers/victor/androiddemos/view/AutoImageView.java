package pers.victor.androiddemos.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import pers.victor.androiddemos.R;

import pers.victor.androiddemos.util.DisplayUtil;


/**
 * Created by Victor on 15/11/3.
 */

public class AutoImageView extends ImageView {
    private final int STYLE_ARC = 1;
    private final int STYLE_SWEEP = 2;
    private final int STYLE_NONE = 0;
    private int mProportionHeight;
    private int mProportionWidth;
    private boolean mIsLoadingProcess;
    private TypedArray typedArray;
    private int mHeight;
    private int mWidth;
    private float mProcess;
    private Paint mPaint;
    private RectF mRectF;
    private float mProcessRectFHalfWidth;
    private float mProcessRectFHalfHeight;
    private float mProcessWidth;
    private int mStyle;
    private Drawable mDrawable;

    public AutoImageView(Context context) {
        super(context);
    }

    public AutoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TopCrop需要matrix
//        setScaleType(ScaleType.MATRIX);
        setScaleType(ScaleType.CENTER_CROP);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoImageView);
        mProportionHeight = typedArray.getInt(R.styleable.AutoImageView_proportion_height, 0);
        mProportionWidth = typedArray.getInt(R.styleable.AutoImageView_proportion_width, 0);
        mStyle = typedArray.getInt(R.styleable.AutoImageView_loading_style, STYLE_ARC);
        typedArray.recycle();
        mIsLoadingProcess = mStyle != STYLE_NONE;
        mPaint = new Paint();
        mRectF = new RectF();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(DisplayUtil.sp2px(18));
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mProcessRectFHalfWidth = DisplayUtil.dp2px(20);
        mProcessRectFHalfHeight = DisplayUtil.dp2px(20);
        mProcessWidth = DisplayUtil.dp2px(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mProportionHeight != 0 || mProportionWidth != 0) {
            mHeight = mWidth * mProportionHeight / mProportionWidth;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        if (!mIsLoadingProcess) {
            return;
        }
        if (mDrawable == null) {
            if (mStyle == STYLE_ARC) {
                canvas.drawColor(Color.DKGRAY);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(mProcessWidth);
                mPaint.setColor(Color.LTGRAY);
                canvas.drawCircle(mWidth / 2, mHeight / 2, mProcessRectFHalfWidth, mPaint);
                mRectF.set(mWidth / 2 - mProcessRectFHalfWidth, mHeight / 2 - mProcessRectFHalfHeight, mWidth / 2 + mProcessRectFHalfWidth, mHeight / 2 + mProcessRectFHalfHeight);
                mPaint.setColor(Color.parseColor("#456789"));
                canvas.drawArc(mRectF, -90, 360 * mProcess, false, mPaint);

            } else if (mStyle == STYLE_SWEEP) {
                mPaint.setColor(Color.DKGRAY);
                mRectF.set(0, 0, mWidth, mHeight * mProcess);
                canvas.drawRect(mRectF, mPaint);
                mPaint.setColor(Color.LTGRAY);
                mRectF.set(0, mHeight * mProcess, mWidth, mHeight);
                canvas.drawRect(mRectF, mPaint);
                mPaint.setColor(Color.parseColor("#456789"));
                canvas.drawText((int) (mProcess * 100) + "%", mWidth / 2, mHeight / 2, mPaint);
            }
        }
    }

    public void setProcess(float process) {
        this.mProcess = process;
        mIsLoadingProcess = true;
        mDrawable = null;
        invalidate();
    }

    /**
     * TOP_CROP
     */
    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
//        Matrix matrix = getImageMatrix();
//        float scaleWidth = (float) getWidth() / (float) getDrawable().getIntrinsicWidth();
//        float scaleHeight = (float) getHeight() / (float) getDrawable().getIntrinsicHeight();
//        float scaleFactor = (scaleWidth > scaleHeight) ? scaleWidth : scaleHeight;
//        matrix.setScale(scaleFactor, scaleFactor);
//        if (scaleFactor == scaleHeight) {
//            float translateX = ((getDrawable().getIntrinsicWidth() * scaleFactor) - getWidth()) / 2;
//            matrix.postTranslate(-translateX, 0);
//        }
//        setImageMatrix(matrix);
        return super.setFrame(l, t, r, b);
    }


    public void setLoadedDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        mIsLoadingProcess = false;
        setImageDrawable(drawable);
    }

    public void showLoadingProcess(boolean loadingProcess) {
        this.mIsLoadingProcess = loadingProcess;
    }
}
