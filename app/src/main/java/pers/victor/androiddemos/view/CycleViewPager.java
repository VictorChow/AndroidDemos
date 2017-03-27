package pers.victor.androiddemos.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pers.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pers.victor.androiddemos.util.ImageUtil;

/**
 * Created by Victor on 2015-8-31
 */
public class CycleViewPager extends ViewPager {
    private int mCount;
    private int currentPosition;     // 当前滑动位置
    private long period;          // 自动滑动时间间隔
    private boolean isCycle;         // 是否循环滑动
    private boolean isAuto;      // 是否自动滑动
    private boolean isTaskRunning;
    private boolean isOnTouch;
    private boolean shouldDestroy;

    private Timer mTimer;
    private TimerTask mTask;
    private LinearLayout container;     // indicator容器
    private List<ImageView> imageViews = new ArrayList<>();
    private List<ImageView> indicators = new ArrayList<>();

    // 自定义indicator相关属性
    private int selectorId;
    private int spacingDip;
    private int indicatorWidth;
    private int indicatorHeight;
    private OnPagerItemClickListener onPagerItemClickListener;

    public CycleViewPager(Context context) {
        super(context);
    }

    public CycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnPagerItemClickListener(OnPagerItemClickListener onPagerItemClickListener) {
        this.onPagerItemClickListener = onPagerItemClickListener;
    }

    private void setAdapter(CycleViewPagerAdapter adapter) {
        super.setAdapter(adapter);
        shouldDestroy = true;
        mCount = adapter.getCount();
        mCount = isCycle ? mCount : mCount + 2;
        setOffscreenPageLimit(mCount - 2 > 0 ? mCount - 2 : 1);
        setCurrentItem(isCycle ? 1 : 0);
        addIndicator();

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isCycle) {
                    if (position == 0 && positionOffset == 0.0) {
                        setCurrentItem(mCount - 2, false);
                    } else if (position >= mCount - 1 && positionOffset == 0.0) {
                        setCurrentItem(1, false);
                    }
                }
                if (positionOffset == 0.0) {
                    currentPosition = position;
                    if (isAuto) {
                        startTask();
                    }
                } else {
                    // 手动滑动时关闭自动滑动
                    if (isOnTouch && isAuto) {
                        if (isTaskRunning) {
                            stopTask();
                        }
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                int index = position;
                if (isCycle) {
                    if (position == 0) {
                        index = mCount - 2;
                    } else if (position >= mCount - 1) {
                        index = 1;
                    }
                }
                if (container != null) {
                    setIndicatorResource(index);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!isOnTouch) {
                    isOnTouch = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isOnTouch) {
                    isOnTouch = false;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * CycleViewPager添加indicator
     */
    private void addIndicator() {
        if (container == null) {
            return;
        }
        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }
        for (int i = 0; i < mCount - 2; i++) {
            ImageView imageView = new ImageView(getContext());
            indicators.add(imageView);
            imageView.setSelected(i == 0);
            if (this.selectorId == 0) {
                imageView.setImageResource(R.drawable.indicator_circle_selector);
                int px = dp2px(7);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(px, px);
                int margin = dp2px(2.5f);
                params.setMargins(margin, 0, margin, 0);
                container.addView(imageView, params);
            } else {
                imageView.setImageResource(this.selectorId);
                int height = dp2px(this.indicatorHeight);
                int width = dp2px(this.indicatorWidth);
                int margin = dp2px(this.spacingDip);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                params.setMargins(margin, 0, margin, 0);
                container.addView(imageView, params);
            }
        }
    }

    /**
     * 设置indicator的选中
     *
     * @param pos viewpager当前位置
     */
    private void setIndicatorResource(int pos) {
        int position = pos;
        if (container == null) {
            return;
        }
        if (position == 0 && isCycle) {
            return;
        }
        if (!isCycle) {
            position++;
        }
        if (container.getChildCount() > 1) {
            final int finalPosition = position;
            post(new Runnable() {
                @Override
                public void run() {
                    container.getChildAt(finalPosition - 1).setSelected(true);
                    for (int i = 0; i < mCount - 2; i++) {
                        container.getChildAt(i).setSelected(i == finalPosition - 1);
                    }
                }
            });
        }
    }

    /**
     * 开启定时任务
     */
    private void startTask() {
        if (container == null) {
            return;
        }
        if (isTaskRunning) {
            return;
        }
        ;
        stopTask();
        isTaskRunning = true;
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        currentPosition++;
                        setPagerCurrentPage();
                    }
                });
            }
        };
        mTimer.schedule(mTask, period, period);
    }

    /**
     * 停止定时任务
     */
    private void stopTask() {
        isTaskRunning = false;
        if (mTask != null) {
            mTask.cancel();
        }
        mTask = null;
        if (mTimer != null) {
            mTimer.purge();
            mTimer.cancel();
        }
        mTimer = null;
    }

    /**
     * 处理内存泄露
     */
    public void onDestroyView() {
        if (!shouldDestroy)
            return;
//        for (imageView in imageViews) {
//            val drawable = imageView.drawable
//            if (drawable != null && drawable is BitmapDrawable) {
//                val bitmap: Bitmap? = drawable.bitmap
//                if (bitmap != null && !bitmap.isRecycled) {
//                    imageView.setImageBitmap(null)
//                    bitmap.recycle()
//                }
//            }
//            if (drawable != null) {
//                drawable.callback = null
//            }
//        }
        imageViews.clear();
//        for (indicator in indicators) {
//            val drawable = indicator.drawable
//            if (drawable != null) {
//                indicator.setImageDrawable(null)
//                drawable.callback = null
//            }
//        }
        indicators.clear();
        stopTask();
        System.gc();
        shouldDestroy = false;
    }

    /**
     * 开启自动滑动
     *
     * @param millisecond 间隔时间
     */
    public void enableAuto(long millisecond) {
        isAuto = true;
        period = millisecond;
    }

    /**
     * 设置当前显示页
     */
    private void setPagerCurrentPage() {
        if (isCycle) {
            if (currentPosition == mCount - 1) {
                currentPosition = 1;
                setCurrentItem(currentPosition);
            } else if (currentPosition == 0) {
                currentPosition = mCount - 2;
                setCurrentItem(currentPosition);
            } else {
                setCurrentItem(currentPosition);
            }
        } else {
            setCurrentItem(currentPosition % (mCount - 2));
        }
    }

    /**
     * 自定义indicator
     *
     * @param selectorId         *
     * @param indicatorWidthDip  *
     * @param indicatorHeightDip *
     * @param spacingDip
     */
    public void setIndicator(@DrawableRes int selectorId, int indicatorWidthDip, int indicatorHeightDip, int spacingDip) {
        this.selectorId = selectorId;
        this.spacingDip = spacingDip;
        this.indicatorWidth = indicatorWidthDip;
        this.indicatorHeight = indicatorHeightDip;
    }

    @Override
    public void setCurrentItem(int item) {
        int index = item;
        if (isCycle && item >= mCount) {
            index = 2;
        }
        currentPosition = index;
        super.setCurrentItem(index);
    }

    public void setUp(int[] resource, ArrayList<String> titles, boolean isCycle, LinearLayout container) {
        this.isCycle = isCycle;
        this.container = container;
        CycleViewPagerAdapter cycleViewPagerAdapter = new CycleViewPagerAdapter(resource, titles);
        setAdapter(cycleViewPagerAdapter);
    }

    public void setUp(ArrayList<String> imageUrl, ArrayList<String> titles, boolean isCycle, LinearLayout container) {
        this.isCycle = isCycle;
        this.container = container;
        CycleViewPagerAdapter cycleViewPagerAdapter = new CycleViewPagerAdapter(imageUrl, titles);
        setAdapter(cycleViewPagerAdapter);
    }

    /**
     * 直接调用setUp(), adapter在内部生成
     */
    @Deprecated
    @Override
    public void setAdapter(PagerAdapter adapter) {
    }

    private int dp2px(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    public interface OnPagerItemClickListener {
        void onClick(int position);
    }

    private class CycleViewPagerAdapter extends PagerAdapter {
        private int[] resource;
        private List<String> imageUrl;
        private List<String> titles;

        private CycleViewPagerAdapter(int[] resource, ArrayList<String> titles) {
            if (isCycle) {
                this.resource = resource.length == 1 ? resource : formatPagerAdapterRes(resource);
                if (titles != null) {
                    this.titles = titles.size() == 1 ? titles : formatPagerAdapterRes(titles);
                }
            } else {
                this.resource = resource;
                this.titles = titles;
            }
            if (resource.length == 1) {
                isCycle = false;
                container.setVisibility(View.INVISIBLE);
            }
        }

        private CycleViewPagerAdapter(ArrayList<String> imageUrl, ArrayList<String> titles) {
            if (isCycle) {
                this.imageUrl = imageUrl.size() == 1 ? imageUrl : formatPagerAdapterRes(imageUrl);
                if (titles != null) {
                    this.titles = titles.size() == 1 ? titles : formatPagerAdapterRes(titles);
                }
            } else {
                this.imageUrl = imageUrl;
                this.titles = titles;
            }
            if (imageUrl.size() == 1) {
                isCycle = false;
                container.setVisibility(View.INVISIBLE);
            } else {
                container.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public int getCount() {
            return (resource != null) ? resource.length : imageUrl.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_pager_layout, container, false);
            final ImageView image = (ImageView) view.findViewById(R.id.iv_pager_item);
            TextView text = (TextView) view.findViewById(R.id.tv_pager_title);
            image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPagerItemClickListener != null) {
                        onPagerItemClickListener.onClick(isCycle ? position - 1 : position);
                    }
                }
            });
            imageViews.add(image);
            if (resource != null) {
                image.setImageResource(resource[position]);
            } else {
                post(new Runnable() {
                    @Override
                    public void run() {
                        ImageUtil.bind(image, imageUrl.get(position));
                    }
                });
            }
            if (this.titles == null) {
                text.setVisibility(View.GONE);
            } else {
                text.setText(this.titles.get(position));
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        /**
         * 处理原始图片资源数组，ABC变为CABCA
         *
         * @param res 原始数组
         *            *
         * @return 添加首尾的数组
         */
        private int[] formatPagerAdapterRes(int[] res) {
            int[] displayRes = new int[res.length + 2];
            displayRes[0] = res[res.length - 1];
            displayRes[displayRes.length - 1] = res[0];
            System.arraycopy(res, 0, displayRes, 1, res.length);
            return displayRes;
        }

        /**
         * 处理原始图片url列表，ABC变为CABCA
         *
         * @param resource 原始列表
         *                 *
         * @return 添加首尾的列表
         */
        private ArrayList<String> formatPagerAdapterRes(ArrayList<String> resource) {
            if (resource.size() > 0) {
                resource.add(0, resource.get(resource.size() - 1));
                resource.add(resource.get(1));
            }
            return resource;
        }
    }
}
