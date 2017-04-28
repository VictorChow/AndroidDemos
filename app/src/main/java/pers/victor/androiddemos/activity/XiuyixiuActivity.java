package pers.victor.androiddemos.activity;

import android.widget.ImageView;

import pers.victor.androiddemos.R;
import pers.victor.androiddemos.view.RippleView;

public class XiuyixiuActivity extends ToolbarActivity {

    @Override
    public int bindLayout() {
        return R.layout.layout_xiuyixiu;
    }

    @Override
    public void initView() {
        final RippleView rippleView = $(R.id.ripple_view);
        ImageView imageView = $(R.id.iv_ripple_tap);
        imageView.setOnClickListener(v -> rippleView.createRipple());
    }
}
