package com.example.victor.test;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

import pers.victor.androiddemos.R;

/**
 * Created by Victor on 2017/1/29. (ง •̀_•́)ง
 */

public class ChatRefresh extends ViewGroup {
    private TextView textFooter;
    private RecyclerView recyclerView;
    private int footHeight;
    private boolean isOnBottom;
    private boolean isScrolling;
    private boolean isRefreshing;
    private float downX;
    private float downY;
    private Scroller scroller;
    private float damping = 2f;

    public ChatRefresh(Context context) {
        super(context);
        init();
    }

    public ChatRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_refresh, this, false);
        textFooter = (TextView) footer.findViewById(R.id.tv_refresh_footer);
        ViewGroup.LayoutParams lp = footer.getLayoutParams();
        footHeight = lp.height;
        recyclerView = new RecyclerView(getContext());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getAdapter() == null
                        || recyclerView.getLayoutManager() == null
                        || !(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
                    return;
                }
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                isOnBottom = layoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1;
            }
        });
        addView(footer);
        addView(recyclerView);
        scroller = new Scroller(getContext(), new DecelerateInterpolator());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getChildAt(0).layout(l, b, r, b + footHeight);
        recyclerView.layout(l, t, r, b);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isRefreshing) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isOnBottom && ev.getY() - downY < 0 && Math.abs(ev.getY() - downY) > Math.abs(ev.getX() - downX)) {
                    if (!isScrolling) {
                        isScrolling = true;
                        downX = ev.getX();
                        downY = ev.getY();
                    }
                    int scrollY = (int) (-(ev.getY() - downY) / damping);
                    updateFooter(scrollY);
                    scrollTo(0, scrollY);
                    return true;
                } else {
                    if (getScrollY() != 0) {
                        scrollTo(0, 0);
                        downX = ev.getX();
                        downY = ev.getY();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isScrolling) {
                    isScrolling = false;
                    if (getScrollY() != 0) {
                        if (Math.abs(getScrollY()) > 1.5 * footHeight) {
                            isRefreshing = true;
                            scroller.startScroll(0, getScrollY(), 0, -getScrollY() + footHeight, 200);
                            textFooter.setText("正在刷新");
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    refreshComplete();
                                }
                            }, 2000);
                        } else {
                            scroller.startScroll(0, getScrollY(), 0, -getScrollY(), 200);
                        }
                        postInvalidate();
                    }
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!scroller.computeScrollOffset()) {
            return;
        }
        int currY = scroller.getCurrY();
        scrollTo(0, currY);
    }

    private void updateFooter(int dy) {
        if (dy > 1.5 * footHeight) {
            textFooter.setText("松开刷新");
        } else {
            textFooter.setText("上拉刷新");
        }
    }

    public void refreshComplete() {
        if (!isRefreshing) {
            return;
        }
        isRefreshing = false;
        scroller.startScroll(0, footHeight, 0, -footHeight, 300);
        textFooter.setText("下拉刷新");
    }
}
