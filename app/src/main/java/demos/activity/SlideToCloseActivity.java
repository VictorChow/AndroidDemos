package demos.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.androiddemos.R;

/**
 * Created by Victor on 16/2/9.
 * 能滑动关闭的activity,需要在Manifest里设置style
 */
public class SlideToCloseActivity extends Activity {
    private View decorView;
    private RelativeLayout relativeLayout;
    private TextView textView;
    private float x;
    private float dx;
    private boolean tag = false;
    private int width;
    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_to_close);
        decorView = getWindow().getDecorView();

        animator = new ValueAnimator();
        animator.setDuration(300);
        animator.setTarget(decorView);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                decorView.setX(value);
            }
        });

        relativeLayout = (RelativeLayout) findViewById(R.id.rl);
        textView = (TextView) findViewById(R.id.txt);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    dx = event.getX() - x;
                    if (dx < 0) {
                        return true;
                    }
                    tag = dx > width / 3;
                    textView.setText(tag ? "松开关闭" : "你好啊");
                    decorView.setX(dx);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (dx > 0) {
                        startAnim(tag ? width : 0);
                    }
                    dx = 0;
                }
                return true;
            }
        });


    }

    private void startAnim(final int end) {
        animator.setIntValues((int) dx, end);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (end != 0) {
                    finish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        width = decorView.getWidth();
    }
}
