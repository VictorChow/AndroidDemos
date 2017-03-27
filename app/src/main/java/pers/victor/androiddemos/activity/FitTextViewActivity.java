package pers.victor.androiddemos.activity;

import android.widget.LinearLayout;

import pers.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import pers.victor.androiddemos.view.FitTextView;

public class FitTextViewActivity extends ToolbarActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_fit_text_view;
    }

    @Override
    public void initView() {
        LinearLayout linearLayout = $(R.id.ll_fit_text);
        List<FitTextView> textViews = new ArrayList<>();
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            FitTextView child = (FitTextView) linearLayout.getChildAt(i);
            textViews.add(child);
        }
        FitTextView.adjustTextViewsWidth(textViews);
    }
}
