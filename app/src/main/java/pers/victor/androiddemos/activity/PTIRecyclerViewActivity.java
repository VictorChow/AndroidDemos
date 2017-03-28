package pers.victor.androiddemos.activity;

import java.util.ArrayList;
import java.util.List;

import pers.victor.androiddemos.R;
import pers.victor.androiddemos.view.PTIRecyclerView;

/**
 * Created by Victor on 2017/4/1. (ง •̀_•́)ง
 */

public class PTIRecyclerViewActivity extends ToolbarActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_pti_recycler;
    }

    @Override
    public void initView() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        PTIRecyclerView rv = $(R.id.rv_group);
        rv.setUp(new PTIRecyclerView.Adapter<String>(this, R.layout.rv_pti_item, list) {
            @Override
            public void onBindViewHolder(Holder holder, int position) {
            }
        });
    }
}
