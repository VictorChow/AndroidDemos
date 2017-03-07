package demos.activity;

import android.view.View;
import android.widget.ImageView;

import com.victor.androiddemos.R;

import demos.view.RippleView;

public class XiuyixiuActivity extends ToolbarActivity {

    @Override
    public int bindLayout() {
        return R.layout.layout_xiuyixiu;
    }

    @Override
    public void initView() {
        final RippleView rippleView = $(R.id.ripple_view);
        ImageView imageView = $(R.id.iv_ripple_tap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rippleView.createRipple();
            }
        });
    }
}
