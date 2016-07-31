package demos.drag_recycleview

import android.content.Context
import android.graphics.Color
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.victor.androiddemos.R
import kotlinx.android.synthetic.main.item_drag_recyclerview.view.*
import java.util.*

/**
 * Created by victor on 16-7-31.
 */
class DragRecyclerViewAdapter(val context: Context, val mStartDragListener: OnStartDragListener) : RecyclerView.Adapter<DragRecyclerViewAdapter.Holder>(), OnMoveAndSwipedListener {

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        //交换mItems数据的位置
        Collections.swap(mItems, fromPosition, toPosition)
        //交换RecyclerView列表中item的位置
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        mItems.removeAt(position)
        notifyItemRemoved(position)
    }

    private val mItems = arrayListOf<String>()

    init {
        mItems.addAll(context.resources.getStringArray(R.array.dummy_items))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = Holder(LayoutInflater.from(context).inflate(R.layout.item_drag_recyclerview, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.text.text = mItems[position]
        //handle是我们拖动item时候要用的，目前先空着
        holder.itemView.handle.setOnTouchListener { v, event ->
            //如果按下
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                //回调RecyclerListFragment中的startDrag方法, 让mItemTouchHelper执行拖拽操作
                mStartDragListener.startDrag(holder)
            }
            false
        }
        holder.itemView.isLongClickable = false
    }

    override fun getItemCount() = mItems.size

    class Holder(view: View) : RecyclerView.ViewHolder(view), OnStateChangedListener {
        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }
    }
}