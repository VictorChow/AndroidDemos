package pers.victor.androiddemos.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import java.util.Locale;

import pers.victor.androiddemos.util.ShowToast;

/**
 * Created by Victor on 16/2/7.
 */
public class GestureVelocityView extends View {
    private GestureDetector gestureDetector;

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
        ViewParent parent;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
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

    private static class MyGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            ShowToast.shortToast("onDoubleTap");
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
            ShowToast.shortToast(String.format(Locale.CHINA, "onFling\nvelocityX %.2f\nvelocityY %.2f", velocityX, velocityY));
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
            ShowToast.shortToast(String.format(Locale.CHINA, "onScroll\ndistanceX %.2f\ndistanceY %.2f", distanceX, distanceY));
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            System.out.println("onContextClick");
            return super.onContextClick(e);
        }
    }
}
