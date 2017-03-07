package demos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Victor on 16/5/10.
 */
public class FitTextView extends TextView {

    private Paint textPaint;
    private float eachWidth;
    private float spacingWidth;
    private float currX;
    private String textStr;

    public FitTextView(Context context) {
        super(context);
        init();
    }

    public FitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public static void adjustTextViewsWidth(List<FitTextView> textViews) {
        adjustTextViewsWidth(textViews.toArray(new FitTextView[]{}));
    }

    public static void adjustTextViewsWidth(FitTextView... textViews) {
        int maxWidth = 0;
        for (FitTextView textView : textViews) {
            maxWidth = textView.getTextWidth() > maxWidth ? textView.getTextWidth() : maxWidth;
        }
        for (FitTextView textView : textViews) {
            textView.adjustWidth(maxWidth);
        }
    }

    private void init() {
        textStr = getText().toString();
        textPaint = new Paint();
        textPaint.setTextSize(getTextSize());
        textPaint.setAntiAlias(true);
        textPaint.setColor(getCurrentTextColor());
        textPaint.setTextAlign(Paint.Align.LEFT);
        setMinWidth((int) textPaint.measureText(textStr));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureText();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        currX = 0f;
        for (int i = 0; i < textStr.length(); i++) {
            canvas.drawText(String.valueOf(textStr.charAt(i)), currX, getBaseline(), textPaint);
            currX += eachWidth + spacingWidth;
        }
    }

    public String getRealText() {
        return textStr;
    }

    public void setText(String text) {
        textStr = text;
        measureText();
        postInvalidate();
    }

    private void measureText() {
        if (textPaint.measureText(textStr) > getMinWidth()) {
            setMinWidth((int) textPaint.measureText(textStr));
        }
        eachWidth = textPaint.measureText(textStr) / textStr.length();
        spacingWidth = (getMeasuredWidth() - textPaint.measureText(textStr)) / (textStr.length() - 1);
    }

    private void adjustWidth(int width) {
        setMinWidth(width);
        measureText();
        postInvalidate();
    }

    private int getTextWidth() {
        return textPaint.measureText(textStr) > getMinWidth() ? (int) textPaint.measureText(textStr) : getMinWidth();
    }
}