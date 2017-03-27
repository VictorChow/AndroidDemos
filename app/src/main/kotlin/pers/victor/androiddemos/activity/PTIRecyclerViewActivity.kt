package pers.victor.androiddemos.activity

import pers.victor.androiddemos.R
import pers.victor.androiddemos.view.PTIRecyclerView
import kotlinx.android.synthetic.main.activity_pti_recycler.*


/**
 * Created by victor on 16-8-1.
 */
class PTIRecyclerViewActivity : ToolbarActivity() {

    override fun bindLayout() = R.layout.activity_pti_recycler

    override fun initView() {
        val list = arrayListOf<String>()
        for (i in 0..2) {
            list.add("")
        }
        rv_group.setUp(object : PTIRecyclerView.Adapter<String>(this, R.layout.rv_pti_item, list) {
            override fun onBindViewHolder(holder: Holder, position: Int) {
            }
        })
    }
}