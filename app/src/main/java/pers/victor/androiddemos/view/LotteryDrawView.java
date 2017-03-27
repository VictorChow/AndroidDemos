package pers.victor.androiddemos.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pers.victor.androiddemos.util.DisplayUtil;

/**
 * Created by victor on 16-12-27
 */

public class LotteryDrawView extends View implements View.OnClickListener {
    private int time = 6000;
    private int strokeWidth;
    private int length;
    private int offsetDegree;
    private int centerX;
    private int centerY;
    private int radius;
    private int totalRate;
    private Paint borderPaint;
    private Paint textPaint;
    private Paint colorPaint1;
    private Paint colorPaint2;
    private List<ItemEntity> items;
    private ValueAnimator animator;

    public LotteryDrawView(Context context) {
        super(context);
        init();
    }

    public LotteryDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        strokeWidth = DisplayUtil.dp2px(1);
        items = new ArrayList<>();
        initPaint();
        initAnimator();
        setOnClickListener(this);
        addItems();
    }

    private void initAnimator() {
        animator = new ValueAnimator();
        animator.setDuration(time);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offsetDegree = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setOnClickListener(null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setOnClickListener(LotteryDrawView.this);
            }
        });
    }

    private void initPaint() {
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(Color.DKGRAY);
        borderPaint.setStrokeWidth(strokeWidth);
        borderPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setStrokeWidth(strokeWidth);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(DisplayUtil.sp2px(14));

        colorPaint1 = new Paint();
        colorPaint1.setAntiAlias(true);
        colorPaint1.setColor(Color.DKGRAY);
        colorPaint1.setStrokeWidth(strokeWidth);
        colorPaint1.setStyle(Paint.Style.FILL);

        colorPaint2 = new Paint();
        colorPaint2.setAntiAlias(true);
        colorPaint2.setColor(Color.GRAY);
        colorPaint2.setStrokeWidth(strokeWidth);
        colorPaint2.setStyle(Paint.Style.FILL);
    }

    private void addItems() {
        int curStart = 0;
        for (int i = 0; i < 8; i++) {
            ItemEntity item = new ItemEntity();
            item.setRate(i);
            item.setName("奖项" + i);
            item.start = curStart;
            item.end = curStart + item.rate;
            items.add(item);
            curStart += item.rate;
        }
        for (ItemEntity item : items) {
            totalRate += item.rate;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        length = MeasureSpec.getSize(widthMeasureSpec);
        centerX = centerY = length / 2;
        radius = length / 2 - strokeWidth;
        setMeasuredDimension(length, length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawCircle(centerX, centerY, radius, borderPaint);

        float eachDegree = 360f / items.size();
        canvas.rotate(offsetDegree, centerX, centerY);

        for (int i = 0; i < items.size(); i++) {
            Paint colorPaint = (i % 2 == 0) ? colorPaint1 : colorPaint2;
            canvas.drawArc(strokeWidth, strokeWidth, length - strokeWidth, length - strokeWidth, -90 - eachDegree / 2, eachDegree, true, colorPaint);
            canvas.drawText(items.get(i).name, centerX, centerY / 3, textPaint);
            canvas.rotate(eachDegree / 2, centerX, centerY);
//            canvas.drawLine(centerX, centerY, centerX, strokeWidth, borderPaint);
            canvas.rotate(eachDegree / 2, centerX, centerY);
        }
    }

    @Override
    public void onClick(View v) {
        start();
    }

    private void start() {
        int randomInt = new Random().nextInt(totalRate) + 1;
        float eachDegree = 360f / items.size();
        for (int i = 0; i < items.size(); i++) {
            ItemEntity item = items.get(i);
            if (randomInt > item.start && randomInt <= item.end) {
                Toast.makeText(getContext(), item.name, Toast.LENGTH_SHORT).show();
                animator.setIntValues(0, 3600 - i * (int) eachDegree);
                break;
            }
        }
        animator.start();
        postInvalidate();
    }

    public static class ItemEntity {
        private int rate;
        private String name;
        private int start;
        private int end;

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
