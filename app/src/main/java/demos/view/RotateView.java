package demos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

/**
 * Created by Victor on 15/12/4.
 */
public class RotateView extends View {
    private Paint mPaint;
    private int mLength;
    private int mHalfLength;
    private RectF mRectF;
    private int mDistance;
    private float mDx;// 位移距离
    private float mPreDx;
    private float mDegreeDivider; //角度分隔
    private float mValueDivider; //值分隔
    private float mMaxValue;
    private float mMinValue;
    private float mMiddleValue; //开始显示的值
    private float mInitialValue; // 初始值,用于计算其他的值
    private int mSpeed;
    private float mScale; // 角度和值的比例

    private String mValue;
    private PointF mPointF;

    public RotateView(Context context) {
        super(context);
    }

    public RotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mRectF = new RectF();
        mPointF = new PointF();
        mMaxValue = 120;
        mMinValue = 25;
        mDistance = 220;
        mDegreeDivider = 4.5f;
        mValueDivider = 0.1f;
        mMiddleValue = 65;
        mSpeed = 25;
        mInitialValue = mMiddleValue + 90 / mDegreeDivider * mValueDivider;
        mScale = mDegreeDivider / mValueDivider;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mLength = width;
        mHalfLength = mLength / 2;
        setMeasuredDimension(mLength, mHalfLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInEditMode()) {
            canvas.drawColor(Color.WHITE);
            mPaint.setColor(Color.DKGRAY);
            mRectF.set(0, 0, mLength, mLength);
            canvas.drawArc(mRectF, 0, -180, true, mPaint);
            mRectF.set(mDistance, mDistance, mLength - mDistance, mLength);
            mPaint.setColor(Color.WHITE);
            canvas.drawArc(mRectF, 0, -180, true, mPaint);
            mPaint.setStrokeWidth(5);
            mPaint.setColor(Color.RED);
            canvas.drawLine(mHalfLength, 0, mHalfLength, mDistance - 50, mPaint);
            mPaint.setColor(Color.LTGRAY);

            // 原有刻度线
            for (float i = mDx, m = 0; i < 360 - mDegreeDivider; i += mDegreeDivider, m = ++m % 10) {
                float cos = (float) Math.cos(Math.toRadians(i));
                float sin = (float) Math.sin(Math.toRadians(i));
                if (m % 5 == 0) {
                    canvas.drawLine(mHalfLength + mHalfLength * cos,
                            mHalfLength - mHalfLength * sin,
                            mHalfLength + (mHalfLength - 50) * cos,
                            mHalfLength - (mHalfLength - 50) * sin, mPaint);
                }
                canvas.drawLine(mHalfLength + mHalfLength * cos,
                        mHalfLength - mHalfLength * sin,
                        mHalfLength + (mHalfLength - 25) * cos,
                        mHalfLength - (mHalfLength - 25) * sin, mPaint);

                mPaint.setColor(Color.WHITE);
                mPaint.setTextSize(40);
                // +0.1 保证数字不会变化
                int value = (int) (mInitialValue - (i - mDx) / mDegreeDivider * mValueDivider + 0.1);
                if (value >= 0) {
                    if (m == 0) {
                        canvas.drawText(String.valueOf(value), mHalfLength + (mHalfLength - 100) * cos,
                                mHalfLength - (mHalfLength - 100) * sin, mPaint);
                    }
                }
            }

            // 右边新增
            for (int j = 0, m = 0; j < (int) (mDx / mDegreeDivider); j++, m = ++m % 10) {
                float cos = (float) Math.cos(Math.toRadians(mDx % mDegreeDivider + j * mDegreeDivider));
                float sin = (float) Math.sin(Math.toRadians(mDx % mDegreeDivider + j * mDegreeDivider));
                canvas.drawLine(mHalfLength + mHalfLength * cos,
                        mHalfLength - mHalfLength * sin,
                        mHalfLength + (mHalfLength - 50) * cos,
                        mHalfLength - (mHalfLength - 50) * sin, mPaint);

                mPaint.setColor(Color.WHITE);
                mPaint.setAntiAlias(true);
                mPaint.setTextSize(40);
                if (m == 0) {
                    canvas.drawText(String.valueOf((int) ((mInitialValue - (j - (int) (mDx / mDegreeDivider)) * mValueDivider))), mHalfLength + (mHalfLength - 100) * cos,
                            mHalfLength - (mHalfLength - 100) * sin, mPaint);
                }
            }
            mPaint.setColor(Color.parseColor("#ff444444"));
            mPaint.setTextSize(120);
            mValue = String.format("%.1f", (mDx / mScale) + mMiddleValue) + " kg";
            canvas.drawText(mValue, mHalfLength, mLength / 3, mPaint);
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
                mPointF.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (getCurrent(event) / mScale + mMiddleValue >= mMaxValue) {
                    break;
                }
                if ((getCurrent(event)) / mScale + mMiddleValue <= mMinValue) {
                    break;
                }
                mDx = getCurrent(event);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                parent = getParent();
                while (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    parent = parent.getParent();
                }
                mPreDx = mDx;
                break;
        }
        return true;
    }

    private float getCurrent(MotionEvent event) {
        return (mPointF.x - event.getX()) / mSpeed + mPreDx;
    }

    public String getValue() {
        return mValue;
    }
}
