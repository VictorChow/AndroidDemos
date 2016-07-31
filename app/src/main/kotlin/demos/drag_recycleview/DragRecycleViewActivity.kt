package demos.drag_recycleview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.victor.androiddemos.R
import demos.drag_recycleview.RecycleViewFragment
import demos.drag_recycleview.OnListItemClickListener
import demos.drag_recycleview.RecyclerGridFragment
import demos.drag_recycleview.RecyclerListFragment

class DragRecycleViewActivity : AppCompatActivity(), OnListItemClickListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)

        if (savedInstanceState == null) {
            val fragment = RecycleViewFragment()
            //用add将MainFragment添加到framelayout上
            supportFragmentManager.beginTransaction().add(R.id.activity_main, fragment).commit()
        }
    }
}
