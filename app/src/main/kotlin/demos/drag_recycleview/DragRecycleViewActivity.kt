package demos.drag_recycleview

import android.support.v4.app.Fragment
import com.victor.androiddemos.R
import demos.activity.ToolbarActivity

class DragRecycleViewActivity : ToolbarActivity(), OnListItemClickListener {
    override fun bindLayout() = R.layout.activity_recyclerview

    override fun initView() {
        val fragment = RecycleViewFragment()
        //用add将MainFragment添加到framelayout上
        supportFragmentManager.beginTransaction().add(R.id.activity_main, fragment).commit()
    }

    override fun onListItemClick(pos: Int) {
        //当MainFragment的Item被点击后，就会回调此方法
        //在此方法中写真正的逻辑，这样Activity和Fragment
        //之间就是松耦合关系，MainFragment可以复用
        var fragment: Fragment? = null
        when (pos) {
            0 ->
                //当点击第一个item时候，new一个RecyclerListFragment
                fragment = RecyclerListFragment()
            1 ->
                //当点击第二个item时候，new一个RecyclerGridFragment
                fragment = RecyclerGridFragment()
        }
        //这次用replace，替换framelayout的布局，也就是MainFragment
        supportFragmentManager.beginTransaction().replace(R.id.activity_main, fragment).addToBackStack(null).commit()
    }
}
