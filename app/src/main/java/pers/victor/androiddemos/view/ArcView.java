package pers.victor.androiddemos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import pers.victor.androiddemos.R;

/**
 * Created by Victor on 2017/3/28. (ง •̀_•́)ง
 */

public class ArcView extends View {
    private Paint mPaint;
    private RectF mRectF;
    private int mDiameter;
    private List<ArcEntity> arcEntities;

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int d = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        mRectF.set(0f, 0f, d, d);
        mDiameter = d;
        setMeasuredDimension(d, d);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int angle = 0;

        for (ArcEntity arc : arcEntities) {
            mPaint.setColor(arc.color);
            canvas.drawArc(mRectF, angle - 90, arc.value, true, mPaint);
            angle += arc.value;
        }
        canvas.translate(mDiameter / 2, mDiameter / 2);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        canvas.drawCircle(0f, 0f, (mDiameter / 3.4f), mPaint);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(80f);
    }

    public void setArcEntities(List<ArcEntity> arcEntities) {
        this.arcEntities = arcEntities;
        int totalValue = 0;
        for (ArcEntity entity : arcEntities) {
            System.out.println(entity.value);
            totalValue += entity.value;
        }
        for (ArcEntity entity : this.arcEntities) {
            entity.value = ((entity.value * 1.0f / totalValue * 360) + 1);
        }
    }


    public static class ArcEntity {
        private String title;
        private int color;
        private float value;

        public ArcEntity(String title, int color, float value) {
            this.title = title;
            this.color = color;
            this.value = value;
        }
    }
}
