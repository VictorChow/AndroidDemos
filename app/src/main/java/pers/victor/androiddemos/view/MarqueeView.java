package pers.victor.androiddemos.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pers.victor.androiddemos.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by victor on 16-7-14
 */
public class MarqueeView extends ViewGroup implements View.OnClickListener {
    private List<String> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private int tempPos = 1;
    private boolean temp = true;
    private Timer timer;
    private TimerTask timerTask;
    private int height, width;

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void addContentViews() {
        if (data.size() == 0) {
            return;
        }
        if (data.size() == 1) {
            addTextView(0);
            return;
        }
        addTextView(0);
        addTextView(1);
    }

    private void addTextView(int index) {
        TextView textView = new TextView(getContext());
        textView.setText(data.get(index));
        textView.setMaxLines(1);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white1));
        addView(textView, width, height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Method method;
        int temp = 0;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(0, temp, width, temp + height);
            try {
                method = View.class.getDeclaredMethod("setMeasuredDimension", int.class, int.class);
                method.setAccessible(true);
                method.invoke(getChildAt(i), width, height);
            } catch (Exception e) {
                e.printStackTrace();
            }
            temp += height;
        }
    }

    public void setContentData(final List<String> data) {
        onDestroyView();
        this.data = data;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                startTranslation();
            }
        };
        removeAllViews();
        addContentViews();
        timer.schedule(timerTask, 2000, 2000);
    }

    public void onDestroyView() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    private void startTranslation() {
        for (int i = 0; i < 2; i++) {
            final ObjectAnimator animator = ObjectAnimator.ofFloat(getChildAt(temp ? i : 1 - i), "y", height * i, -height + height * i);
            animator.setDuration(500);
            if (i == 0) {
                final int finalI = i;
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        tempPos++;
                        if (tempPos >= data.size())
                            tempPos = 0;
                        ((TextView) getChildAt(temp ? 1 - finalI : finalI)).setText(data.get(tempPos));
                    }
                });
            }

            post(new Runnable() {
                @Override
                public void run() {
                    animator.start();
                }
            });
        }
        temp = !temp;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick((tempPos + data.size() - 1) % data.size());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
}