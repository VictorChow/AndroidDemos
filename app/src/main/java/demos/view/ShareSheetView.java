package demos.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 16-12-30
 */

public class ShareSheetView extends ViewGroup {
    private final int HEIGHT = 50;
    private final int NAME_WIDTH = 75;
    private final int ITEM_WIDTH = 75;
    private final int backgroundColor = Color.parseColor("#00000000");
    private final int titleColor = Color.parseColor("#f1f1f1");
    private final int itemLeftColor = Color.parseColor("#fbce41");
    private final int itemRightColor = Color.parseColor("#e35152");
    private final int dividerColor = Color.parseColor("#666666");

    private LinearLayout root;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<ObservableScrollView> scrollViews;
    private OnLoadListener onLoadListener;
    private View footer;
    private DataSource dataSource;
    private boolean isLoading;
    private int scrollX;

    public ShareSheetView(Context context) {
        super(context);
        init();
    }

    public ShareSheetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        scrollViews = new ArrayList<>();
        root = new LinearLayout(getContext());
        root.setBackgroundColor(backgroundColor);
        root.setOrientation(LinearLayout.VERTICAL);
        addView(root, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        recyclerView = new RecyclerView(getContext());
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(linearLayoutManager);
        root.addView(recyclerView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        root.layout(l, t, r, b);
    }

    public void initialize(DataSource dataSource) {
        this.dataSource = dataSource;
        View titleView = renderItem();
        LinearLayout leftContainer = (LinearLayout) titleView.findViewById(R.id.left_container);
        LinearLayout rightContainer = (LinearLayout) titleView.findViewById(R.id.right_container);
        final ObservableScrollView scrollView = (ObservableScrollView) titleView.findViewById(R.id.scrollView);
        scrollViews.add(scrollView);
        scrollView.setOnScrollChangedCallback(new OnScrollChangedCallback() {
            @Override
            public void onScrollChanged(int l, int t) {
                scrollX = l;
                for (ObservableScrollView observableScrollView : scrollViews) {
                    observableScrollView.scrollTo(l, t);
                }
            }
        });
        for (String leftTitle : dataSource.getTitle().getLeft()) {
            TextView textView = renderText(titleColor);
            textView.setText(leftTitle);
            leftContainer.addView(textView, dp2px(NAME_WIDTH), dp2px(HEIGHT));
        }
        for (String rightTitle : dataSource.getTitle().getRight()) {
            TextView textView = renderText(titleColor);
            textView.setText(rightTitle);
            rightContainer.addView(textView, dp2px(ITEM_WIDTH), dp2px(HEIGHT));
        }
        root.addView(titleView, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        View divider = new View(getContext());
        divider.setBackgroundColor(dividerColor);
        root.addView(divider, 1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(0.5f)));
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                footer = renderFooter();
                recyclerView.setAdapter(new ShareSheetAdapter());
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        int count = recyclerView.getAdapter().getItemCount();
                        if (count == 0) {
                            return;
                        }
                        if (!isLoading && linearLayoutManager.findLastVisibleItemPosition() == count - 1 && onLoadListener != null) {
                            isLoading = true;
                            onLoadListener.onLoadMore();
                        }
                    }
                });
            }
        });
    }

    public void update(List<ValueSource> valueSources, boolean isComplete) {
        int count = recyclerView.getAdapter().getItemCount();
        this.dataSource.getValue().addAll(valueSources);
        recyclerView.getAdapter().notifyItemRangeInserted(count, valueSources.size());

        ProgressBar progressBar = (ProgressBar) footer.findViewById(R.id.progress);
        TextView textView = (TextView) footer.findViewById(R.id.foot_text);
        if (isComplete) {
            onLoadListener = null;
            textView.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
        } else {
            textView.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
        }
        isLoading = false;
    }

    private View renderFooter() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setPadding(0, dp2px(8), 0, dp2px(8));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setId(R.id.progress);
        progressBar.setIndeterminate(true);
        linearLayout.addView(progressBar, LinearLayout.LayoutParams.WRAP_CONTENT, dp2px(26));
        TextView textView = new TextView(getContext());
        textView.setId(R.id.foot_text);
        textView.setVisibility(GONE);
        textView.setText("已加载全部");
        textView.setTextColor(titleColor);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(13);
        linearLayout.addView(textView, LinearLayout.LayoutParams.WRAP_CONTENT, dp2px(26));
        return linearLayout;
    }

    private View renderItem() {
        LinearLayout body = new LinearLayout(getContext());
        body.setOrientation(LinearLayout.VERTICAL);
        LinearLayout item = new LinearLayout(getContext());
        item.setOverScrollMode(LinearLayout.OVER_SCROLL_NEVER);
        item.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout leftContainer = new LinearLayout(getContext());
        leftContainer.setId(R.id.left_container);
        leftContainer.setOverScrollMode(LinearLayout.OVER_SCROLL_NEVER);
        leftContainer.setOrientation(LinearLayout.HORIZONTAL);
        leftContainer.setOverScrollMode(LinearLayout.OVER_SCROLL_NEVER);
        item.addView(leftContainer, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ObservableScrollView scrollView = new ObservableScrollView(getContext());
        scrollView.setId(R.id.scrollView);
        LinearLayout rightContainer = new LinearLayout(getContext());
        rightContainer.setId(R.id.right_container);
        rightContainer.setOrientation(LinearLayout.HORIZONTAL);
        rightContainer.setOverScrollMode(LinearLayout.OVER_SCROLL_NEVER);
        scrollView.addView(rightContainer, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        item.addView(scrollView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        body.addView(item, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View divider = new View(getContext());
        divider.setBackgroundColor(dividerColor);
        body.addView(divider, 1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(0.5f)));
        return body;
    }

    private TextView renderText(int color) {
        TextView textView = new TextView(getContext());
        textView.setTextColor(color);
        textView.setTextSize(15);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private int dp2px(float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void checkScrollX() {
        for (ObservableScrollView view : scrollViews) {
            if (view.getScrollX() != scrollX) {
                view.scrollTo(scrollX, 0);
            }
        }
    }

    private interface OnScrollChangedCallback {
        void onScrollChanged(int l, int t);
    }

    public interface OnLoadListener {
        void onLoadMore();
    }

    public static class DataSource {
        /**
         * title : {"left":[""],"right":[""]}
         * value : [{"left":[""],"right":[""]}]
         */

        private TitleSource title;
        private List<ValueSource> value;

        public TitleSource getTitle() {
            return title;
        }

        public void setTitle(TitleSource title) {
            this.title = title;
        }

        public List<ValueSource> getValue() {
            return value;
        }

        public void setValue(List<ValueSource> value) {
            this.value = value;
        }
    }

    public static class TitleSource {
        private List<String> left;
        private List<String> right;

        public List<String> getLeft() {
            return left;
        }

        public void setLeft(List<String> left) {
            this.left = left;
        }

        public List<String> getRight() {
            return right;
        }

        public void setRight(List<String> right) {
            this.right = right;
        }
    }

    public static class ValueSource {
        private List<String> left;
        private List<String> right;

        public List<String> getLeft() {
            return left;
        }

        public void setLeft(List<String> left) {
            this.left = left;
        }

        public List<String> getRight() {
            return right;
        }

        public void setRight(List<String> right) {
            this.right = right;
        }
    }

    private static class ObservableScrollView extends HorizontalScrollView {
        private OnScrollChangedCallback onScrollCallback;

        public ObservableScrollView(Context context) {
            super(context);
            init();
        }

        public ObservableScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setOverScrollMode(HorizontalScrollView.OVER_SCROLL_NEVER);
            setHorizontalScrollBarEnabled(false);
        }

        public void setOnScrollChangedCallback(OnScrollChangedCallback onScrollCallback) {
            this.onScrollCallback = onScrollCallback;
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            if (onScrollCallback != null) {
                onScrollCallback.onScrollChanged(l, t);
            }
        }
    }

    private class ShareSheetAdapter extends RecyclerView.Adapter<Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            final Holder holder;
            if (viewType == 0) {
                holder = new Holder(renderItem());
                for (String left : dataSource.getTitle().getLeft()) {
                    holder.leftContainer.addView(renderText(itemLeftColor), dp2px(NAME_WIDTH), dp2px(HEIGHT));
                }
                for (String right : dataSource.getTitle().getRight()) {
                    holder.rightContainer.addView(renderText(itemRightColor), dp2px(ITEM_WIDTH), dp2px(HEIGHT));
                }
                holder.scrollView.setOnScrollChangedCallback(new OnScrollChangedCallback() {
                    @Override
                    public void onScrollChanged(int l, int t) {
                        scrollX = l;
                        for (ObservableScrollView observableScrollView : scrollViews) {
                            observableScrollView.scrollTo(l, t);
                        }
                    }
                });
                scrollViews.add(holder.scrollView);
            } else {
                holder = new Holder(footer);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            if (position == getItemCount() - 1) {
                return;
            }
            ValueSource valueSource = dataSource.getValue().get(position);
            for (int i = 0; i < holder.leftContainer.getChildCount(); i++) {
                ((TextView) holder.leftContainer.getChildAt(i)).setText(valueSource.getLeft().get(i));
            }
            for (int i = 0; i < holder.rightContainer.getChildCount(); i++) {
                ((TextView) holder.rightContainer.getChildAt(i)).setText(valueSource.getRight().get(i));
            }
        }

        @Override
        public int getItemCount() {
            return dataSource.getValue().size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position == dataSource.getValue().size() ? 1 : 0;
        }

        @Override
        public void onViewAttachedToWindow(Holder holder) {
            checkScrollX();
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        private LinearLayout leftContainer;
        private LinearLayout rightContainer;
        private ObservableScrollView scrollView;

        private Holder(View itemView) {
            super(itemView);
            leftContainer = (LinearLayout) itemView.findViewById(R.id.left_container);
            rightContainer = (LinearLayout) itemView.findViewById(R.id.right_container);
            scrollView = (ObservableScrollView) itemView.findViewById(R.id.scrollView);
        }
    }

}
