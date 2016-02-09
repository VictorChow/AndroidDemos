package demos.util;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import demos.view.FriendlyViewPager;

/**
 * Created by Victor on 16/2/9.
 */
public class MenuLayout extends ViewGroup {
    private View mMenuView;
    private View mContentView;
    private VelocityTracker mVelocityTracker;
    private ViewWrapper wrapper;
    private boolean mIsShowMenu;
    private float mDownX;
    private float mDownY;
    private float mOffsetX;
    private float mOffsetY;
    private int mMenuWidth;

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mContentView = getChildAt(1);
        wrapper = new ViewWrapper(mContentView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        mMenuWidth = mMenuView.getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMenuView.layout(0, 0, mMenuView.getMeasuredWidth(), mMenuView.getMeasuredHeight());
        mContentView.layout(0, 0, mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                intercept = false;
                break;
            case MotionEvent.ACTION_UP:
                mOffsetX = mOffsetY = 0;
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetX = event.getX() - mDownX;
                mOffsetY = event.getY() - mDownY;
                if (FriendlyViewPager.IS_FIRST_PAGER) {
                    if (mOffsetX > 0) {
                        intercept = mOffsetX > Math.abs(mOffsetY);
                    }
                }
                if (mIsShowMenu) {
                    intercept = true;
                }
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetX = event.getX() - mDownX;
                if (!mIsShowMenu) {
                    if (mOffsetX < 0) {
                        mOffsetX = 0;
                    }
                    if (mOffsetX > mMenuWidth) {
                        mOffsetX = mMenuWidth;
                    }
                } else {
                    if (mOffsetX > 0) {
                        mOffsetX = 0;
                    }
                    if (-mOffsetX > mMenuWidth) {
                        mOffsetX = -mMenuWidth;
                    }
                }
                float contentViewX = mIsShowMenu ? mMenuWidth : 0;
                mContentView.setX(contentViewX + mOffsetX);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocity = mVelocityTracker.getXVelocity();
                if (Math.abs(velocity) >= 1000) {
                    if (!mIsShowMenu) {
                        if (velocity > 0) {
                            startAnimator(mMenuWidth, 250);
                        } else {
                            startAnimator(0, 250);
                        }
                    } else {
                        if (velocity < 0) {
                            startAnimator(0, 250);
                        } else {
                            startAnimator(mMenuWidth, 250);
                        }
                    }
                } else {
                    if (!mIsShowMenu) {
                        if (mOffsetX > mMenuWidth / 2) {
                            startAnimator(mMenuWidth, 350);
                        } else {
                            startAnimator(0, 350);
                        }
                    } else {
                        if (-mOffsetX > mMenuWidth / 2) {
                            startAnimator(0, 350);
                        } else {
                            startAnimator(mMenuWidth, 350);
                        }
                    }
                }
                mVelocityTracker.clear();
                mOffsetX = 0;
                break;
        }
        return true;
    }

    private void startAnimator(float endX, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(wrapper, "left", endX);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        mIsShowMenu = endX != 0;
    }

    private class ViewWrapper {
        private View mTarget;

        public ViewWrapper(@NonNull View mTarget) {
            this.mTarget = mTarget;
        }

        public void setLeft(float left) {
            mTarget.setX(left);
        }

        public float getLeft() {
            return mTarget.getX();
        }
    }
}
