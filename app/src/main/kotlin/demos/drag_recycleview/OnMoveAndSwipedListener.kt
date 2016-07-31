package demos.drag_recycleview

/**
 * Created by victor on 16-7-31.
 */
interface OnMoveAndSwipedListener {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemDismiss(position: Int)
}