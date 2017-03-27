package pers.victor.androiddemos.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import pers.victor.androiddemos.R;


/**
 * Created by Victor on 16/1/10.
 * 刮刮乐
 */
public class ScratchCardView extends View {
    private Canvas fgCanvas;
    private Bitmap bgBitmap;
    private Bitmap fgBitmap;
    private Path path;
    private Paint fgPaint;

    public ScratchCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    private void init() {
        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.iv_scratch_card);
        fgBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        fgCanvas = new Canvas(fgBitmap);
        fgCanvas.drawColor(Color.DKGRAY);
        fgPaint = new Paint();
        fgPaint.setAntiAlias(true);
        fgPaint.setDither(true);
        fgPaint.setStrokeJoin(Paint.Join.ROUND);
        fgPaint.setStrokeCap(Paint.Cap.ROUND);
        fgPaint.setStyle(Paint.Style.STROKE);
        fgPaint.setStrokeWidth(50);
        fgPaint.setAlpha(0);
        fgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        path = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        ViewParent parent;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent = getParent();
                while (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                    parent = parent.getParent();
                }
                path.reset();
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                parent = getParent();
                while (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                    parent = parent.getParent();
                }
                break;
        }
        fgCanvas.drawPath(path, fgPaint);
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(fgBitmap, 0, 0, null);
    }
}
