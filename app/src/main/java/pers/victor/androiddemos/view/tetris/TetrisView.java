package pers.victor.androiddemos.view.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Victor on 2017/3/13. (ง •̀_•́)ง
 */

public class TetrisView extends View {
    private final int ROW = 15;
    private final int COLUMN = 10;
    private final int INTERVAL = 800;
    private final Paint linePaint = new Paint();
    private final Paint blockPaint = new Paint();
    private final int[][] values = new int[ROW][COLUMN];
    private final List<String> tetrisPos = new ArrayList<>();

    private int side;
    private Timer timer;
    private TimerTask timerTask;

    public TetrisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

        post(new Runnable() {
            @Override
            public void run() {
                start();
            }
        });
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
                checkMove();
            }
        };
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

    private void drawBlocks(Canvas canvas) {
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COLUMN; c++) {
                if (values[r][c] == 1) {
                    canvas.drawRect(getRect(r, c), blockPaint);
                }
            }
        }
    }

    private void checkMove() {
        boolean canMove = true;
        for (String pos : tetrisPos) {
            String[] p = pos.split(",");
            int r = Integer.parseInt(p[0]);
            int c = Integer.parseInt(p[1]);
            if (r == ROW - 1) {
                canMove = false;
                break;
            }
            if (r + 1 < ROW && !tetrisPos.contains(String.format(Locale.CHINA, "%d,%d", r + 1, c))) {
                if (values[r + 1][c] == 1) {
                    canMove = false;
                    break;
                }
            }
        }
        if (canMove) {
            print();
            for (int i = 0; i < tetrisPos.size(); i++) {
                String pos = tetrisPos.get(i);
                String[] p = pos.split(",");
                int r = Integer.parseInt(p[0]);
                int c = Integer.parseInt(p[1]);
                values[r][c] -= 1;
                values[r + 1][c] += 1;
                tetrisPos.set(i, String.format(Locale.CHINA, "%d,%d", r + 1, c));
            }
            print();
            postInvalidate();
        } else {
            createTetris();
        }
    }

    private void checkRotate() {

    }

    public void start() {
        createTetris();
        timer.schedule(timerTask, INTERVAL, INTERVAL);
    }

    private void createTetris() {
        tetrisPos.clear();
        Tetris tetris;
        boolean canCreate = false;
        while (!canCreate) {
            tetris = Tetris.create();
            switch (tetris.getShape()) {
                case L:
                    canCreate = values[0][4] * values[1][4] * values[2][4] * values[2][5] == 0;
                    if (canCreate) {
                        values[0][4] = values[1][4] = values[2][4] = values[2][5] = 1;
                        tetrisPos.add("0,4");
                        tetrisPos.add("1,4");
                        tetrisPos.add("2,4");
                        tetrisPos.add("2,5");
                    }
                    break;
                case LR:
                    canCreate = values[0][5] * values[1][5] * values[2][5] * values[2][4] == 0;
                    if (canCreate) {
                        values[0][5] = values[1][5] = values[2][5] = values[2][4] = 1;
                        tetrisPos.add("0,5");
                        tetrisPos.add("1,5");
                        tetrisPos.add("2,5");
                        tetrisPos.add("2,4");
                    }
                    break;
                case S:
                    canCreate = values[0][4] * values[1][4] * values[1][5] * values[2][5] == 0;
                    if (canCreate) {
                        values[0][4] = values[1][4] = values[1][5] = values[2][5] = 1;
                        tetrisPos.add("0,4");
                        tetrisPos.add("1,4");
                        tetrisPos.add("1,5");
                        tetrisPos.add("2,5");
                    }
                    break;
                case SR:
                    canCreate = values[0][5] * values[1][4] * values[1][5] * values[2][4] == 0;
                    if (canCreate) {
                        values[0][5] = values[1][4] = values[1][5] = values[2][4] = 1;
                        tetrisPos.add("0,5");
                        tetrisPos.add("1,4");
                        tetrisPos.add("1,5");
                        tetrisPos.add("2,4");
                    }
                    break;
                case I:
                    canCreate = values[0][3] * values[0][4] * values[0][5] * values[0][6] == 0;
                    if (canCreate) {
                        values[0][3] = values[0][4] = values[0][5] = values[0][6] = 1;
                        tetrisPos.add("0,3");
                        tetrisPos.add("0,4");
                        tetrisPos.add("0,5");
                        tetrisPos.add("0,6");
                    }
                    break;
                case O:
                    canCreate = values[0][4] * values[0][5] * values[1][4] * values[1][5] == 0;
                    if (canCreate) {
                        values[0][4] = values[0][5] = values[1][4] = values[1][5] = 1;
                        tetrisPos.add("0,4");
                        tetrisPos.add("0,5");
                        tetrisPos.add("1,4");
                        tetrisPos.add("1,5");
                    }
                    break;
                case T:
                    canCreate = values[0][4] * values[1][3] * values[1][4] * values[1][5] == 0;
                    if (canCreate) {
                        values[0][4] = values[1][3] = values[1][4] = values[1][5] = 1;
                        tetrisPos.add("0,4");
                        tetrisPos.add("1,3");
                        tetrisPos.add("1,4");
                        tetrisPos.add("1,5");
                    }
                    break;
            }
            System.out.println(tetris.getShape());
        }
        postInvalidate();
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

    private Rect getRect(int row, int column) {
        int left = (column + 1) + column * side;
        int top = (row + 1) + row * side;
        int right = left + side;
        int bottom = top + side;
        return new Rect(left, top, right, bottom);
    }


    private void print() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                System.out.print(values[i][j] == 0 ? "· " : "o ");
            }
            System.out.println();
        }
        System.out.println(" ");
    }
}
