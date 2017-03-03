package demos.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import demos.util.ShowToast;

/**
 * Created by Victor on 16/2/7.
 */
public class GestureVelocityView extends View {
    private GestureDetector gestureDetector;
    private VelocityTracker velocityTracker;

    public GestureVelocityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new MyGesture());
        setLongClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointId = event.getPointerId(0);
        int mMaxFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        if (null == velocityTracker) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        ViewParent parent;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
//                velocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
//                ShowToast.shortToast("x速度: " + velocityTracker.getXVelocity(pointId) + "  y速度: " + velocityTracker.getYVelocity(pointId));
//                if (velocityTracker != null) {
//                    velocityTracker.clear();
//                    velocityTracker.recycle();
//                    velocityTracker = null;
//                }
                parent = getParent();
                while (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    parent = parent.getParent();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                parent = getParent();
                while (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                    parent = parent.getParent();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private class MyGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            ShowToast.shortToast("onDoubleTap");
//            System.out.println("onDoubleTap");
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            System.out.println("onDoubleTapEvent  " + e.getAction());
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            System.out.println("onSingleTapUp");
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            ShowToast.shortToast("onLongPress");
            super.onLongPress(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            ShowToast.shortToast("onFling");
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            System.out.println("onDown");
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            ShowToast.shortToast("onSingleTapConfirmed");
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            System.out.println("onShowPress");
            super.onShowPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            ShowToast.shortToast("onScroll");
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            System.out.println("onContextClick");
            return super.onContextClick(e);
        }
    }
}
