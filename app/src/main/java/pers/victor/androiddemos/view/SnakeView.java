package pers.victor.androiddemos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Victor on 2017/3/16. (ง •̀_•́)ง
 */

public class SnakeView extends View {
    public static final int UP = 1;
    public static final int DOWN = -1;
    public static final int LEFT = 2;
    public static final int RIGHT = -2;

    private final int ROW = 25;
    private final int COLUMN = 15;
    private final int INTERVAL = 300;

    private final Paint linePaint = new Paint();
    private final Paint blockPaint = new Paint();
    private final Random random = new Random();
    private final List<String> points = new LinkedList<>();

    private int side;
    private int curDirection = RIGHT;
    private long lastClickTime;
    private String nextPoint;
    private Timer timer;
    private TimerTask timerTask;

    public SnakeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint.setStrokeWidth(1);
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.WHITE);

        blockPaint.setStyle(Paint.Style.FILL);
        blockPaint.setAntiAlias(true);
        blockPaint.setColor(Color.WHITE);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                move();
            }
        };

        post(new Runnable() {
            @Override
            public void run() {
                start();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        side = (measureWidth - (COLUMN + 1)) / COLUMN;
        int width = side * COLUMN + (COLUMN + 1);
        int height = side * ROW + (ROW + 1);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLines(canvas);
        drawBlocks(canvas);
    }

    private void drawBlocks(Canvas canvas) {
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COLUMN; c++) {
                if (points.contains(getPointString(r, c))) {
                    canvas.drawRect(getRect(r, c), blockPaint);
                }
            }
        }

        if (nextPoint != null) {
            int[] pos = getPointArray(nextPoint);
            canvas.drawRect(getRect(pos[0], pos[1]), blockPaint);
        }
    }

    private void drawLines(Canvas canvas) {
        int curY = 0;
        for (int i = 0; i <= ROW; i++) {
            canvas.drawLine(0, curY, getWidth(), curY, linePaint);
            curY += side + 1;
        }
        int curX = 0;
        for (int i = 0; i <= COLUMN; i++) {
            canvas.drawLine(curX, 0, curX, getHeight(), linePaint);
            curX += side + 1;
        }
    }

    private Rect getRect(int row, int column) {
        int left = (column + 1) + column * side;
        int top = (row + 1) + row * side;
        int right = left + side;
        int bottom = top + side;
        return new Rect(left, top, right, bottom);
    }

    public void swerve(@Direction int direction) {
        if (System.currentTimeMillis() - lastClickTime < 100) {
            return;
        }
        if (curDirection + direction == 0 || curDirection == direction) {
            return;
        }
        curDirection = direction;
        lastClickTime = System.currentTimeMillis();
    }

    private String getPointString(int r, int c) {
        return String.format(Locale.CHINA, "%d,%d", r, c);
    }

    private int[] getPointArray(String s) {
        int[] pos = new int[2];
        String str[] = s.split(",");
        pos[0] = Integer.parseInt(str[0]);
        pos[1] = Integer.parseInt(str[1]);
        return pos;
    }

    private void start() {
        points.add("0,2");
        points.add("0,1");
        points.add("0,0");

        createNextPoint();
        timer.schedule(timerTask, INTERVAL, INTERVAL);
    }

    private void move() {
        int[] first = getPointArray(points.get(0));
        int r = first[0];
        int c = first[1];
        String newFirst = "";
        switch (curDirection) {
            case UP:
                newFirst = getPointString(r - 1, c);
                if (r - 1 == -1 || points.contains(newFirst)) {
                    stop();
                    return;
                }
                break;
            case DOWN:
                newFirst = getPointString(r + 1, c);
                if (r + 1 == ROW || points.contains(newFirst)) {
                    stop();
                    return;
                }
                break;
            case LEFT:
                newFirst = getPointString(r, c - 1);
                if (c - 1 == -1 || points.contains(newFirst)) {
                    stop();
                    return;
                }
                break;
            case RIGHT:
                newFirst = getPointString(r, c + 1);
                if (c + 1 == COLUMN || points.contains(newFirst)) {
                    stop();
                    return;
                }
                break;
        }

        points.add(0, newFirst);
        if (newFirst.contentEquals(nextPoint)) {
            createNextPoint();
        } else {
            points.remove(points.size() - 1);
        }
        postInvalidate();
    }

    private void stop() {
        blockPaint.setColor(Color.parseColor("#CD2626"));
        timer.cancel();
        timer.purge();
        timerTask.cancel();
        postInvalidate();
    }

    private void createNextPoint() {
        int nextR = random.nextInt(ROW);
        int nextC = random.nextInt(COLUMN);
        nextPoint = getPointString(nextR, nextC);
        while (points.contains(nextPoint)) {
            nextR = random.nextInt(ROW);
            nextC = random.nextInt(COLUMN);
            nextPoint = getPointString(nextR, nextC);
        }
    }

    public void destroyView() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @IntDef({UP, DOWN, LEFT, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Direction {
    }
}
