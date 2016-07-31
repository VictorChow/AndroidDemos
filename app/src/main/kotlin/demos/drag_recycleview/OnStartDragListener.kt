package demos.drag_recycleview

import android.support.v7.widget.RecyclerView

/**
 * Created by victor on 16-7-31.
 */
interface OnStartDragListener {
    fun startDrag(viewHolder: RecyclerView.ViewHolder)
}