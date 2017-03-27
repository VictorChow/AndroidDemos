package pers.victor.androiddemos.drag_recycleview

import android.graphics.Canvas
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import pers.victor.androiddemos.drag_recycleview.OnMoveAndSwipedListener
import pers.victor.androiddemos.drag_recycleview.OnStateChangedListener

/**
 * Created by victor on 16-7-31.
 */
class SimpleItemTouchHelperCallback(val mAdapter: OnMoveAndSwipedListener) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (recyclerView.layoutManager !is GridLayoutManager) {
            //设置拖拽方向为上下
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            //设置侧滑方向为从左到右和从右到左都可以
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            //将方向参数设置进去
            return makeMovementFlags(dragFlags, swipeFlags)
        } else {
            //设置拖拽方向为上下左右
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            //不支持侧滑
            val swipeFlags = 0
            return makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        //如果两个item不是一个类型的，我们让他不可以拖拽
        if (viewHolder.itemViewType != target.itemViewType) {
            return false
        }
        //回调adapter中的onItemMove方法
        mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        //当前状态不是idel（空闲）状态时，说明当前正在拖拽或者侧滑
        //看看这个viewHolder是否实现了onStateChangedListener接口
        if (viewHolder is OnStateChangedListener) {
            val listener = viewHolder
            //回调ItemViewHolder中的onItemSelected方法来改变item的背景颜色
            listener.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is OnStateChangedListener) {
            val listener = viewHolder
            listener.onItemClear()
        }
        super.clearView(recyclerView, viewHolder)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //根据侧滑的位移来修改item的透明度
            val alpha = (1 - Math.abs(dX) / viewHolder.itemView.width).toFloat()
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}