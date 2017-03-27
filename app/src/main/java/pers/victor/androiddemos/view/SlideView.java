package pers.victor.androiddemos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import pers.victor.androiddemos.R;

import pers.victor.androiddemos.util.DisplayUtil;


/**
 * Created by Victor on 15/12/11.
 */
public class SlideView extends View {
    private int mViewWidth;
    private int mViewHalfWidth;
    private int mViewHeight;

    private float mSpacingVertical;
    private float mValue;
    private float mMinValue;
    private float mMaxValue;
    private float mTempValue;
    private float mUnit;
    private float mDy;
    private float mPreDy;
    private int mGradientWidth;
    private int mGradientOffsetMiddleX;
    private int mGradientOffsetEndX;
    private int mLongerLineLength;
    private int mShorterLineLength;
    private int mSpacingToRightGradient;

    private PointF mPointF;
    private GradientDrawable mDrawable;
    private Paint mPaint;

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.bg_grey_fade_vertical);
        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mSpacingVertical = DisplayUtil.dp2px(12);
        mMaxValue = 220;
        mMinValue = 100;
        mPointF = new PointF();
        mValue = 170;
        mTempValue = mValue;
        mUnit = 10 / (mSpacingVertical * 5);
        mGradientWidth = DisplayUtil.dp2px(8);
        mGradientOffsetMiddleX = DisplayUtil.dp2px(50);
        mGradientOffsetEndX = DisplayUtil.dp2px(15);
        mLongerLineLength = DisplayUtil.dp2px(10);
        mShorterLineLength = DisplayUtil.dp2px(6);
        mSpacingToRightGradient = DisplayUtil.dp2px(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mViewWidth = width;
        mViewHeight = height;
        mViewHalfWidth = mViewWidth / 2;
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode()) {
            canvas.drawColor(Color.WHITE);
            //渐变线
            mDrawable.setBounds(mViewHalfWidth + mGradientOffsetMiddleX, 0, mViewHalfWidth + mGradientOffsetMiddleX + mGradientWidth, mViewHeight);
            mDrawable.draw(canvas);
            mDrawable.setBounds(mViewWidth - mGradientOffsetEndX - mGradientWidth, 0, mViewWidth - mGradientOffsetEndX, mViewHeight);
            mDrawable.draw(canvas);

            mPaint.setColor(Color.parseColor("#ff757575"));
            float drawValue = mMaxValue + 10;
            //spacing * 3 * 5 使初始值定为170
            for (float i = mDy - mSpacingVertical * 3 * 5, j = 0; drawValue > mMinValue; i += mSpacingVertical, j = ++j % 5) {
                if (j == 0) {
                    //长标尺
                    canvas.drawLine(mViewWidth - mGradientOffsetEndX - mGradientWidth - mLongerLineLength - mSpacingToRightGradient, i, mViewWidth - mGradientOffsetEndX - mGradientWidth - mSpacingToRightGradient, i, mPaint);
                    mPaint.setTextSize(DisplayUtil.sp2px(12));
                    mPaint.setTypeface(Typeface.DEFAULT);
                    canvas.drawText(String.valueOf((int) (drawValue -= 10)), mViewWidth - mGradientOffsetEndX - mGradientWidth - DisplayUtil.dp2px(25), i + 15, mPaint);
                } else {
                    //短标尺
                    canvas.drawLine(mViewWidth - mGradientOffsetEndX - mGradientWidth - mShorterLineLength - mSpacingToRightGradient, i, mViewWidth - mGradientOffsetEndX - mGradientWidth - mSpacingToRightGradient, i, mPaint);
                }
            }

            //标准线
            for (int i = 20; i < mViewWidth - 180; i += 30) {
                mPaint.setColor(Color.BLACK);
                canvas.drawLine(i, 10 * mSpacingVertical, i + 20, 10 * mSpacingVertical, mPaint);
            }

            //打印Value

            mPaint.setTypeface(Typeface.DEFAULT);
            mPaint.setTextSize(DisplayUtil.sp2px(22));
            canvas.drawText("身高", (mViewHalfWidth + mGradientOffsetMiddleX) / 2, 4 * mSpacingVertical, mPaint);
            mPaint.setTextSize(DisplayUtil.sp2px(30));
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText(String.valueOf((int) mTempValue), (mViewHalfWidth + mGradientOffsetMiddleX) / 2 - DisplayUtil.dp2px(20), 7 * mSpacingVertical, mPaint);
            mPaint.setTextSize(DisplayUtil.sp2px(22));
            mPaint.setTypeface(Typeface.DEFAULT);
            canvas.drawText("cm", (mViewHalfWidth + mGradientOffsetMiddleX) / 2 + DisplayUtil.dp2px(25), 7 * mSpacingVertical, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewParent parent;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent = getParent();
                while (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                    parent = parent.getParent();
                }
                if (event.getX() < mViewWidth / 2) {
                    return false;
                }
                mPointF.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (mValue + mUnit * (event.getY() - mPointF.y) < mMinValue) {
                    break;
                } else if (mValue + mUnit * (event.getY() - mPointF.y) > mMaxValue + 0.3f) {
                    break;
                }
                mDy = event.getY() - mPointF.y + mPreDy;
                changeValue(event.getY() - mPointF.y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPreDy = mDy;
                mValue = mTempValue;
                parent = getParent();
                while (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    parent = parent.getParent();
                }
                break;

        }
        return true;
    }

    private void changeValue(float dy) {
        mTempValue = mValue + mUnit * dy;
    }

    public String getValue() {
        return String.valueOf((int) mValue);
    }
}
