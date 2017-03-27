package pers.victor.androiddemos.activity

import pers.victor.androiddemos.R
import pers.victor.androiddemos.util.ShowToast
import kotlinx.android.synthetic.main.activity_vertical_menu.*

/**
 * Created by victor on 16-8-1.
 */
class VerticalMenuActivity : ToolbarActivity() {
    override fun bindLayout() = R.layout.activity_vertical_menu

    override fun initView() {
        for (i in 0..vertical_menu_layout.childCount - 1) {
            vertical_menu_layout.getChildAt(i).setOnClickListener {
                ShowToast.shortToast("第 $i 个")
            }
        }
    }

}