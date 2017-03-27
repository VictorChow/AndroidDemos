package pers.victor.androiddemos.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import pers.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 16-8-15
 */
public class CycleViewPagerParent extends ViewGroup {
    private int proportionHeight;
    private int proportionWidth;
    private int indicatorMarginBottom;
    private int cycleTime;
    private boolean isCycleSlide;
    private boolean isAutomaticSlide;
    private CycleViewPager cycleViewPager;
    private LinearLayout indicatorContainer;

    public CycleViewPagerParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CycleViewPagerParent);
        proportionHeight = typedArray.getInt(R.styleable.CycleViewPagerParent_proportionHeight, 0);
        proportionWidth = typedArray.getInt(R.styleable.CycleViewPagerParent_proportionWidth, 0);
        isCycleSlide = typedArray.getBoolean(R.styleable.CycleViewPagerParent_slideCycle, true);
        isAutomaticSlide = typedArray.getBoolean(R.styleable.CycleViewPagerParent_slideAutomatic, true);
        cycleTime = typedArray.getInt(R.styleable.CycleViewPagerParent_slideCycleTime, 5000);
        indicatorMarginBottom = typedArray.getDimensionPixelSize(R.styleable.CycleViewPagerParent_indicatorMarginBottom, dp2px(6));
        cycleViewPager = new CycleViewPager(context, attrs);
        addView(cycleViewPager, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        indicatorContainer = new LinearLayout(context, attrs);
        indicatorContainer.setGravity(typedArray.getInt(R.styleable.CycleViewPagerParent_indicatorGravity, Gravity.CENTER));
        indicatorContainer.setOrientation(LinearLayout.HORIZONTAL);
        int paddingHorizontal = typedArray.getDimensionPixelSize(R.styleable.CycleViewPagerParent_indicatorMarginHorizontal, 0);
        indicatorContainer.setPadding(paddingHorizontal, 0, paddingHorizontal, 0);
        addView(indicatorContainer, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        boolean showIndicator = typedArray.getBoolean(R.styleable.CycleViewPagerParent_showIndicator, true);
        indicatorContainer.setVisibility(showIndicator ? View.VISIBLE : View.GONE);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY && proportionHeight * proportionWidth != 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = width * proportionHeight / proportionWidth;
            int newWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));
            int newHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));
            setMeasuredDimension(newWidthSpec, newHeightSpec);
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof CycleViewPager) {
                    LayoutParams lp = getChildAt(i).getLayoutParams();
                    lp.height = height;
                    lp.width = width;
                    getChildAt(i).setLayoutParams(lp);
                }
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof CycleViewPager) {
                getChildAt(i).layout(0, 0, getWidth(), getHeight());
            } else {
                getChildAt(i).layout(0, getHeight() - indicatorContainer.getMeasuredHeight() - indicatorMarginBottom, getWidth(), getHeight() - indicatorMarginBottom);
            }
        }
    }


    public void setUp(int[] resource, List<String> titles) {
        cycleViewPager.setUp(resource, (ArrayList<String>) titles, isCycleSlide, indicatorContainer);
        if (isAutomaticSlide) {
            cycleViewPager.enableAuto(cycleTime);
        }
    }

    public void setUp(int[] resource) {
        setUp(resource, null);
    }

    public void setUp(List<String> imageUrl, List<String> titles) {
        cycleViewPager.setUp((ArrayList<String>) imageUrl, (ArrayList<String>) titles, isCycleSlide, indicatorContainer);
        if (isAutomaticSlide) {
            cycleViewPager.enableAuto(cycleTime);
        }
    }

    public void setUp(List<String> imageUrl) {
        setUp(imageUrl, null);
    }

    public void setIndicator(@DrawableRes int selectorId, int indicatorWidthDip, int indicatorHeightDip, int spacingDip) {
        cycleViewPager.setIndicator(selectorId, indicatorWidthDip, indicatorHeightDip, spacingDip);
    }

    public void setOnPagerItemClickListener(CycleViewPager.OnPagerItemClickListener listener) {
        cycleViewPager.setOnPagerItemClickListener(listener);
    }

    public void onDestroyView() {
        cycleViewPager.onDestroyView();
    }

    private int dp2px(float dp){
        return (int) (dp*getResources().getDisplayMetrics().density+0.5f);
    }
}