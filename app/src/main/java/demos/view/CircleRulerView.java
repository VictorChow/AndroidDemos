package demos.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ImageView;

import demos.util.DisplayUtil;


public class CircleRulerView extends ImageView {

    private Paint mPaint;

    private int bordColor;// 边框颜色
    private int curveColor;// 刻度颜色
    private int textColor;
    private int bordWidth;// 边框宽度
    private int wheelHeight, wheelWidth;
    private int circleX;// 圆心X坐标
    private int radius;// 圆半径

    private int curveLineB_H = 40;
    private int curveLineS_H = 20;
    private int curveLineB_W = 3;
    private int curveLineS_W = 2;

    private int divCount = 80;
    private float divAngle = 0;

    private Matrix matrix; // Matrix used to perform rotations
    private double startAngle = 0;
    private float totalRotation = 0;
    private float minRotation = 0;
    private float maxRotation = 0;
    private float topValue = 60.0f;
    private Context context;

    private float selectedPosition; // the section currently selected by the user.
    private WheelChangeListener wheelChangeListener;

    public CircleRulerView(Context context) {
        this(context, null);
    }

    public CircleRulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.setScaleType(ScaleType.MATRIX);
        bordColor = Color.parseColor("#000000");
        curveColor = Color.parseColor("#000000");
        textColor = Color.parseColor("#000000");
        bordWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8,
                getResources().getDisplayMetrics());

        init();
    }

    private void init() {
        selectedPosition = topValue;
        mPaint = new Paint();
        divAngle = (float) (360 / divCount);
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }
        maxRotation = 400 * divAngle;
        minRotation = -900 * divAngle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);// 空心
        circleX = getWidth() / 2;
        radius = circleX - bordWidth;// 半径


        // drawBord(canvas);
        for (int i = 0; i <= (divCount / 5) + 1; i++) {

            drawCurveByAngle(canvas, totalRotation + i * 5 * divAngle);
        }

        int moveCount = 0;
        if (totalRotation > 0) moveCount = (int) Math.floor(totalRotation / (divAngle * 10));
        else moveCount = (int) Math.ceil(totalRotation / (divAngle * 10));
        for (int i = -divCount / 20; i <= divCount / 20; i++) {

            drawTextByAngle(canvas, totalRotation % (10 * divAngle) + (i) * 10 * divAngle - 95,
                    i - moveCount);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (wheelHeight == 0 || wheelWidth == 0) {
            wheelHeight = h;
            wheelWidth = w;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewParent parent;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent = getParent();
                while (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                    parent = parent.getParent();
                }
                startAngle = getAngle(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                double currentAngle = getAngle(event.getX(), event.getY());

                rotateWheel((float) (startAngle - currentAngle));

                startAngle = currentAngle;
                selectedPosition = topValue - (totalRotation / divAngle) / 10;
                if (wheelChangeListener != null) {
                    wheelChangeListener.onSelectionChange(selectedPosition);
                }
                break;
            case MotionEvent.ACTION_UP:
                parent = getParent();
                while (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    parent = parent.getParent();
                }
                float over = totalRotation % divAngle;
                float distance = 0;
                if (over >= divAngle / 2) {
                    distance = divAngle - over;
                } else if (over <= -divAngle / 2) {
                    distance = -(over + divAngle);
                } else {
                    distance = -over;
                }
                rotateWheel(distance);
                selectedPosition = topValue - (totalRotation / divAngle) / 10;
                if (wheelChangeListener != null) {
                    wheelChangeListener.onSelectionChange(selectedPosition);
                }
                break;

            default:
                break;
        }
        return true;
    }

    private double getAngle(double x, double y) {
        x = x - (wheelWidth / 2d);
        y = wheelHeight - y - (wheelHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    private void rotateWheel(float degrees) {
        if (degrees > 180) degrees -= 360;
        else if (degrees < -180) degrees += 360;
        if ((totalRotation + degrees > maxRotation) || (totalRotation + degrees < minRotation))
            return;
        totalRotation += degrees;
        matrix.postRotate(degrees, wheelWidth / 2, wheelHeight / 2);
        CircleRulerView.this.setImageMatrix(matrix);
        // CustomView4.this.setRotation(sumAngle);//此方法不能重绘
    }

    private void drawCurveByAngle(Canvas canvas, float angle) {
        mPaint.setColor(curveColor);
        mPaint.setStrokeWidth(curveLineB_W);

        if ((angle % 360 >= -90 && angle % 360 <= 90) || (angle % 360 >= -360 && angle % 360 <=
                -270) || (angle % 360 >= 270 && angle % 360 <= 360)) {
            canvas.drawLine((float) (circleX + radius * Math.cos(Math.toRadians(90 - angle))),
                    (float) (circleX - radius * Math.cos(Math.toRadians(angle))),
                    (float) (circleX + (radius - curveLineB_H) * Math.cos(
                            Math.toRadians(90 - angle))),
                    (float) (circleX - (radius - curveLineB_H) * Math.cos(Math.toRadians(angle))),
                    mPaint);
        }
        mPaint.setStrokeWidth(curveLineS_W);
        for (int i = 1; i < 5; i++) {
            if (((angle + i * divAngle) % 360 >= -90 && (angle + i * divAngle) % 360 <= 90) || (
                    (angle + i * divAngle) % 360 >= -360 && (angle + i * divAngle) % 360 <= -270)
                    || ((angle + i * divAngle) % 360 >= 270 && (angle + i * divAngle) % 360 <=
                    360)) {
                canvas.drawLine((float) (circleX + radius * Math.cos(
                        Math.toRadians(90 - angle - i * divAngle))),
                        (float) (circleX - radius * Math.cos(Math.toRadians(angle + i * divAngle))),
                        (float) (circleX + (radius - curveLineS_H) * Math.cos(
                                Math.toRadians(90 - angle - i * divAngle))),
                        (float) (circleX - (radius - curveLineS_H) * Math.cos(
                                Math.toRadians(angle + i * divAngle))), mPaint);
            }
        }
    }

    private void drawTextByAngle(Canvas canvas, float angle, int num) {

        mPaint.setTextSize(DisplayUtil.sp2px(18));
        mPaint.setColor(textColor);
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.addCircle(circleX, circleX,
                radius - curveLineB_H - DisplayUtil.dp2px(2), Direction.CW);
        float textAngle = 0;
        if (angle < 0) textAngle = 360 - Math.abs(angle) % 360;
        else textAngle = Math.abs(angle) % 360;

        if (textAngle >= 180) canvas.drawTextOnPath((int) (topValue + num) + "", path,
                (float) ((textAngle / 180) * Math.PI * (radius - curveLineB_H - DisplayUtil.dp2px(
                        2))), DisplayUtil.dp2px(10),
                mPaint);
    }

    private void drawBord(Canvas canvas) {
        mPaint.setStrokeWidth(bordWidth); // 设置圆环的宽度
        mPaint.setColor(bordColor);
        canvas.drawCircle(circleX, circleX, radius, mPaint);
    }

    public interface WheelChangeListener {
        /**
         * Called when user selects a new position in the wheel menu.
         *
         * @param selectedPosition the new position selected.
         */
        void onSelectionChange(float selectedPosition);
    }

    public void setWheelChangeListener(WheelChangeListener wheelChangeListener) {
        this.wheelChangeListener = wheelChangeListener;
    }
}
