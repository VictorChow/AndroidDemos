package demos.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.HashMap;

import demos.util.DisplayUtil;

/**
 * Created by Victor on 2017/2/23. (ง •̀_•́)ง
 */

public class RippleView extends View {
    private final int colorStart = 0xFFF8F8F8;
    private final int colorEnd = 0x00000000;
    private final int radiusMin = DisplayUtil.dp2px(30);
    private final int duration = 3000;
    private int radiusMax;
    private int centerX;
    private int centerY;
    private Shader shader;
    private ArgbEvaluator evaluator;
    private HashMap<Animator, Integer> radiusMap;
    private HashMap<Animator, Paint> paintMap;

    public RippleView(Context context) {
        super(context);
        init();
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        evaluator = new ArgbEvaluator();
        radiusMap = new HashMap<>();
        paintMap = new HashMap<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        radiusMax = (width < height ? width : height) / 2;
        centerX = width / 2;
        centerY = height / 2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Animator animator : radiusMap.keySet()) {
            canvas.drawCircle(centerX, centerY, radiusMap.get(animator), paintMap.get(animator));
        }
    }

    public void createRipple() {
        final Paint paint = new Paint();
        paint.setStrokeWidth(100);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        ValueAnimator animator = new ValueAnimator();
        radiusMap.put(animator, 0);
        paintMap.put(animator, paint);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int radiusCurr = (int) animation.getAnimatedValue();
                radiusMap.put(animation, radiusCurr);
                int color = (int) evaluator.evaluate(animation.getAnimatedFraction(), colorStart, colorEnd);
                shader = new RadialGradient(centerX, centerY, radiusCurr + 1, Color.TRANSPARENT, color, Shader.TileMode.CLAMP);
                paintMap.get(animation).setColor(color);
                paintMap.get(animation).setShader(shader);
                postInvalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                radiusMap.remove(animation);
                paintMap.remove(animation);
            }
        });
        animator.setIntValues(radiusMin, radiusMax);
        animator.start();
    }

}
