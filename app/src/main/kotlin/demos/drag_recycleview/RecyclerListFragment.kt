package demos.drag_recycleview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demos.drag_recycleview.DragRecyclerViewAdapter
import demos.drag_recycleview.SimpleItemTouchHelperCallback
import demos.drag_recycleview.OnStartDragListener


class RecyclerListFragment : Fragment(), OnStartDragListener {

    private var mItemTouchHelper: ItemTouchHelper? = null

    override fun startDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper!!.startDrag(viewHolder)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = RecyclerView(container!!.context)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val adapter = DragRecyclerViewAdapter(activity, this)
        //参数view即为我们在onCreateView中return的view
        val recyclerView = view as RecyclerView
        //固定recyclerview大小
        recyclerView.setHasFixedSize(true)
        //设置adapter
        recyclerView.adapter = adapter
        //设置布局类型为LinearLayoutManager，相当于ListView的样式
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val callback = SimpleItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper!!.attachToRecyclerView(recyclerView)
    }

}
