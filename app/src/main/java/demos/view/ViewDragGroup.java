package demos.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.FrameLayout;

import demos.util.DisplayUtil;

/**
 * Created by Victor on 16/1/10.
 */
public class ViewDragGroup extends FrameLayout {
    private ViewDragHelper mViewDragHelper;
    private View mMainView, mMenuView;
    private VelocityTracker mVelocityTracker;

    private float mDownX;
    private float mOffsetX;


    public static boolean IS_SHOW_MENU;

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mMainView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < 0) {
                left = 0;
            } else if (left > DisplayUtil.dp2px(200)) {
                left = DisplayUtil.dp2px(200);
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (mMainView.getLeft() < DisplayUtil.dp2px(100)) {
                hideMenu();

            } else {
                showMenu();
            }
        }
    };

    public ViewDragGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewDragHelper = ViewDragHelper.create(this, mCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mOffsetX = 0;
                intercept = false;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetX = ev.getX() - mDownX;
                intercept = FriendlyViewPager.IS_FIRST_PAGER && mOffsetX > 0;
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                if (IS_SHOW_MENU) {
                    if (mVelocityTracker.getXVelocity() < -1000) {
                        hideMenu();
                    }
                } else {
                    if (mVelocityTracker.getXVelocity() > 1000) {
                        showMenu();
                    }
                }
                break;
        }
        return true;
    }

    private void showMenu() {
        mViewDragHelper.smoothSlideViewTo(mMainView, DisplayUtil.dp2px(200), 0);
        ViewCompat.postInvalidateOnAnimation(ViewDragGroup.this);
        IS_SHOW_MENU = true;
    }

    private void hideMenu() {
        mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
        ViewCompat.postInvalidateOnAnimation(ViewDragGroup.this);
        IS_SHOW_MENU = false;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

}
