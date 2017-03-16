package demos.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.victor.androiddemos.R;

import demos.AndroidDemos;
import demos.util.DisplayUtil;

/**
 * Created by Victor on 2017/3/10. (ง •̀_•́)ง
 */

public class SwipeFinishActivity extends AppCompatActivity {

    private int screenWidth = AndroidDemos.screenWidth;
    private float downX;
    private float downY;
    private float touchSlop;
    private float minX = DisplayUtil.dp2px(30);
    private float elevation = DisplayUtil.dp2px(12);
    private boolean swipeBack = true;
    private View content;
    private VelocityTracker velocityTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
    }

    public void setSwipeBack(boolean swipeBack) {
        this.swipeBack = swipeBack;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!swipeBack) {
            return super.dispatchTouchEvent(ev);
        }
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (downX <= minX) {
                    float dx = ev.getX() - downX;
                    downX = dx < 0 ? ev.getX() : downX;
                    content.setX(dx < 0 ? 0 : dx);
                }
                break;
            case MotionEvent.ACTION_UP:
                //点击事件
                if (Math.abs(ev.getX() - downX) < touchSlop && Math.abs(ev.getY() - downY) < touchSlop) {
                    content.setX(0);
                    velocityTracker.clear();
                    return super.dispatchTouchEvent(ev);
                }
                //其他位置的up事件不拦截
                if (downX > minX) {
                    velocityTracker.clear();
                    return super.dispatchTouchEvent(ev);
                }
                velocityTracker.computeCurrentVelocity(1000);
                float vx = velocityTracker.getXVelocity();
                //速度
                if (downX <= minX && Math.abs(vx) > 1600) {
                    if (vx > 0) {
                        toRight();
                    } else {
                        toLeft();
                    }
                    velocityTracker.clear();
                    return true;
                }
                //保证x为0
                if (content.getX() != 0) {
                    if (content.getX() > screenWidth / 2) {
                        toRight();
                    } else {
                        toLeft();
                    }
                }
                velocityTracker.clear();
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void toLeft() {
        ViewCompat.animate(content)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .xBy(-content.getX())
                .start();
    }

    private void toRight() {
        ViewCompat.animate(content)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        finish();
                        overridePendingTransition(R.anim.anim_stay, R.anim.anim_stay);
                    }
                })
                .xBy(screenWidth - content.getX())
                .start();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        content = getLayoutInflater().inflate(layoutResID, null);
        ViewCompat.setElevation(content, elevation);
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        content = view;
        ViewCompat.setElevation(content, elevation);
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        content = view;
        ViewCompat.setElevation(content, elevation);
        super.setContentView(view, params);
    }
}
