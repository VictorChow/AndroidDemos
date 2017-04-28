package pers.victor.androiddemos.activity;

import android.widget.TextView;

import pers.victor.androiddemos.R;
import pers.victor.androiddemos.annotations.bind.Bind;
import pers.victor.androiddemos.annotations.bind.BindView;
import pers.victor.androiddemos.view.PathAnimView;

/**
 * Created by Victor on 2017/4/28. (ง •̀_•́)ง
 */

public class PathAnimActivity extends ToolbarActivity {
    @BindView(R.id.path_view)
    PathAnimView view;
    @BindView(R.id.tv_path_success)
    TextView tvSuccess;

    @Override
    public int bindLayout() {
        return R.layout.activity_path_anim;
    }

    @Override
    public void initView() {
        Bind.bind(this);

        tvSuccess.setOnClickListener(v -> {
            view.setSuccess(true);
            view.startAnim();
        });
    }
}
