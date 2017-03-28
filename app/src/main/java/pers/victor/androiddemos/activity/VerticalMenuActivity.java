package pers.victor.androiddemos.activity;

import android.view.View;

import pers.victor.androiddemos.R;
import pers.victor.androiddemos.util.ShowToast;
import pers.victor.androiddemos.view.VerticalMenuLayout;

/**
 * Created by Victor on 2017/4/1. (ง •̀_•́)ง
 */

public class VerticalMenuActivity extends ToolbarActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_vertical_menu;
    }

    @Override
    public void initView() {
        VerticalMenuLayout verticalMenuLayout = $(R.id.vertical_menu_layout);
        for (int i = 0; i < verticalMenuLayout.getChildCount(); i++) {
            final int finalI = i;
            verticalMenuLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowToast.shortToast(finalI);
                }
            });
        }
    }
}
