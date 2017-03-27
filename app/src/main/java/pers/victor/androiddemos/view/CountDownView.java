package pers.victor.androiddemos.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import pers.victor.androiddemos.R;

import java.util.Timer;
import java.util.TimerTask;

import pers.victor.androiddemos.util.DisplayUtil;

/**
 * Created by Victor on 16/1/13.
 */
public class CountDownView extends ViewGroup {
    private Context context;
    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvSecond;
    private TextView tvColon1;
    private TextView tvColon2;
    private TimerTask timerTask;
    private Timer timer;
    private int hour;
    private int minute;
    private int second;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvHour.setText(String.format("%02d", hour));
            tvMinute.setText(String.format("%02d", minute));
            tvSecond.setText(String.format("%02d", second));
        }
    };

    public CountDownView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(DisplayUtil.dp2px(90), DisplayUtil.dp2px(20));
    }

    private void init() {
        tvHour = new TextView(context);
        tvMinute = new TextView(context);
        tvSecond = new TextView(context);
        tvColon1 = new TextView(context);
        tvColon2 = new TextView(context);
        tvHour.setTextSize(15);
        tvMinute.setTextSize(15);
        tvSecond.setTextSize(15);
        tvColon1.setTextSize(17);
        tvColon2.setTextSize(17);
        tvColon1.setTypeface(Typeface.DEFAULT_BOLD);
        tvColon2.setTypeface(Typeface.DEFAULT_BOLD);
        tvHour.setTextColor(Color.WHITE);
        tvMinute.setTextColor(Color.WHITE);
        tvSecond.setTextColor(Color.WHITE);
        tvColon1.setTextColor(Color.parseColor("#ff7f7f7f"));
        tvColon2.setTextColor(Color.parseColor("#ff7f7f7f"));
        tvHour.setGravity(Gravity.CENTER);
        tvMinute.setGravity(Gravity.CENTER);
        tvSecond.setGravity(Gravity.CENTER);
        tvColon1.setGravity(Gravity.CENTER);
        tvColon2.setGravity(Gravity.CENTER);
        tvHour.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_count_time));
        tvMinute.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_count_time));
        tvSecond.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_count_time));
        tvHour.setText("01");
        tvMinute.setText("02");
        tvSecond.setText("03");
        tvColon1.setText(":");
        tvColon2.setText(":");
        addView(tvHour);
        addView(tvColon1);
        addView(tvMinute);
        addView(tvColon2);
        addView(tvSecond);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                --second;
                if (second < 0) {
                    --minute;
                    if (minute >= 0) {
                        second = 59;
                    } else {
                        --hour;
                        if (hour >= 0) {
                            minute = 59;
                            second = 59;
                        } else {
                            hour = minute = second = 0;
                        }
                    }
                }
                handler.sendMessage(new Message());
                if (hour == 0 && minute == 0 & second == 0) {
                    cancel();
                }
            }
        };
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        tvHour.layout(0, 0, DisplayUtil.dp2px(24), DisplayUtil.dp2px(20));
        tvColon1.layout(DisplayUtil.dp2px(24), -DisplayUtil.dp2px(2), DisplayUtil.dp2px(33), DisplayUtil.dp2px(18));
        tvMinute.layout(DisplayUtil.dp2px(33), 0, DisplayUtil.dp2px(57), DisplayUtil.dp2px(20));
        tvColon2.layout(DisplayUtil.dp2px(57), -DisplayUtil.dp2px(2), DisplayUtil.dp2px(66), DisplayUtil.dp2px(18));
        tvSecond.layout(DisplayUtil.dp2px(66), 0, DisplayUtil.dp2px(90), DisplayUtil.dp2px(20));
    }

    public void setCountDownTime(final int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        tvHour.setText(String.format("%02d", hour));
        tvMinute.setText(String.format("%02d", minute));
        tvSecond.setText(String.format("%02d", second));
        timer.schedule(timerTask, 0, 1000);
    }
}
