package pers.victor.androiddemos.drag_recycleview


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import pers.victor.androiddemos.R


/**
 * A simple [Fragment] subclass.
 */
class RecycleViewFragment : ListFragment() {
    private var mListItemClickListener: OnListItemClickListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = inflater!!.inflate(R.layout.fragment_recyclerview, container, false)!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //他的宿主Activity将实现onListItemClickListener接口
        //使用getActivity（）获得的宿主Activity，将他强转成onListItemClickListener接口
        mListItemClickListener = activity as OnListItemClickListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //获得我们在strings.xml中定义个数组
        val items = resources.getStringArray(R.array.main_items)
        //创建适配器
        val adapter = ArrayAdapter(activity, R.layout.item_text, items)
        //设置适配器
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        if (mListItemClickListener != null) {
            //由于宿主Activity实现了onListItemClickListener接口
            //因此调用的是宿主Activity的onListItemClick方法
            //并且将点击的item的position传给Activity
            mListItemClickListener?.onListItemClick(position)
        }
    }
}
