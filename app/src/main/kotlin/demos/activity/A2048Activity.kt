package demos.activity

import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.TextView
import com.victor.androiddemos.R
import kotlinx.android.synthetic.main.activity_2048.*
import java.util.*

class A2048Activity : ToolbarActivity() {
    override fun initView() {

        val model = Model()
        model.setRefreshViewListener {
            val data = model.getData()
            for (i in data.indices) {
                for (j in data[i].indices) {
                    val tv = ((container.getChildAt(i) as ViewGroup).getChildAt(j) as TextView)
                    if (data[i][j] != 0)
                        tv.text = data[i][j].toString()
                    else
                        tv.text = ""
                }
            }
            text_score.text = model.getScore().toString()
        }
        model.setFinishListener { text_score.text = "游戏结束: " + text_score.text }

        container.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                }
                MotionEvent.ACTION_UP -> {
                    if (event.x > downX && event.y > downY) {
                        if (Math.abs(event.x - downX) > (Math.abs(event.y - downY)))
                            model.swipe(Model.SwipeDirection.END)
                        else
                            model.swipe(Model.SwipeDirection.BOTTOM)
                    }
                    if (event.x > downX && event.y < downY) {
                        if (Math.abs(event.x - downX) > (Math.abs(event.y - downY)))
                            model.swipe(Model.SwipeDirection.END)
                        else
                            model.swipe(Model.SwipeDirection.TOP)
                    }
                    if (event.x < downX && event.y > downY) {
                        if (Math.abs(event.x - downX) > (Math.abs(event.y - downY)))
                            model.swipe(Model.SwipeDirection.START)
                        else
                            model.swipe(Model.SwipeDirection.BOTTOM)
                    }
                    if (event.x < downX && event.y < downY) {
                        if (Math.abs(event.x - downX) > (Math.abs(event.y - downY)))
                            model.swipe(Model.SwipeDirection.START)
                        else
                            model.swipe(Model.SwipeDirection.TOP)
                    }
                }
            }
            true
        }

    }

    override fun bindLayout() = R.layout.activity_2048

    private var downX = 0f
    private var downY = 0f

    class Model {

        private var refreshListener: (() -> Unit)? = null
        private var finishListener: (() -> Unit)? = null
        private var score = 0
        private var isFinish = false

        fun setRefreshViewListener(func: () -> Unit) {
            this.refreshListener = func
            func.invoke()
        }

        fun setFinishListener(func: () -> Unit) {
            this.finishListener = func
        }

        private val data = arrayOf(
                arrayListOf(0, 0, 0, 0),
                arrayListOf(0, 0, 0, 0),
                arrayListOf(0, 0, 0, 0),
                arrayListOf(0, 0, 0, 0)
        )
        private val size = data.size

        init {
            for (i in 0..1) {
                createRandomData()
            }
            printData()
        }

        fun getData() = data.clone()

        fun getScore() = score

        fun swipe(direction: SwipeDirection) {
            if (isFinish)
                return
            if (isFull()) {
                if (direction == SwipeDirection.START || direction == SwipeDirection.END) {
                    if (!canHorizontalSwipe()) return
                }
                if (direction == SwipeDirection.TOP || direction == SwipeDirection.BOTTOM) {
                    if (!canVerticalSwipe()) return
                }
            }
            when (direction) {
                SwipeDirection.START -> horizontalSwipe(direction)
                SwipeDirection.END -> horizontalSwipe(direction)
                SwipeDirection.TOP -> verticalSwipe(direction)
                SwipeDirection.BOTTOM -> verticalSwipe(direction)
            }
            refreshListener?.invoke()
            printData()
            isFinish = isOver()
            if (isFinish) {
                finishListener?.invoke()
            } else if (!isFull())
                createRandomData()
        }

        private fun horizontalSwipe(direction: SwipeDirection) {
            val cIndices = if (direction == SwipeDirection.START) data.indices else data.indices.reversed()
            for (r in data.indices) {
                for (c in cIndices) {
                    if (data[r][c] != 0) {
                        val cLastIndices = if (direction == SwipeDirection.START) c + 1..data.lastIndex else c - 1 downTo 0
                        for (t in cLastIndices) {
                            if (data[r][c] == data[r][t]) {
                                data[r][c] += data[r][t]
                                data[r][t] = 0
                                score += data[r][c]
                                break
                            } else if (data[r][t] != 0) break
                        }
                    }
                }
                moveRowElements(data[r], direction)
            }
        }

        private fun verticalSwipe(direction: SwipeDirection) {
            val rIndices = if (direction == SwipeDirection.TOP) data.indices else data.indices.reversed()
            for (c in data.indices) {
                for (r in rIndices) {
                    if (data[r][c] != 0) {
                        val rLastIndices = if (direction == SwipeDirection.TOP) r + 1..data.lastIndex else r - 1 downTo 0
                        for (t in rLastIndices) {
                            if (data[r][c] == data[t][c]) {
                                data[r][c] += data[t][c]
                                data[t][c] = 0
                                score += data[r][c]
                                break
                            } else if (data[t][c] != 0) break
                        }
                    }
                }
                moveColumnElements(c, direction)
            }
        }


        private fun moveRowElements(elements: MutableList<Int>, direction: SwipeDirection) {
            (elements.lastIndex downTo 0)
                    .filter { elements[it] == 0 }
                    .forEach { elements.removeAt(it) }
            while (elements.size < size) {
                if (direction == SwipeDirection.START) {
                    elements.add(0)
                }
                if (direction == SwipeDirection.END) {
                    elements.add(0, 0)
                }
            }
        }

        private fun moveColumnElements(column: Int, direction: SwipeDirection) {
            val nums = arrayListOf<Int>()
            for (r in data.indices) {
                nums.add(data[r][column])
            }
            if (direction == SwipeDirection.TOP) moveRowElements(nums, SwipeDirection.START)
            if (direction == SwipeDirection.BOTTOM) moveRowElements(nums, SwipeDirection.END)
            nums.forEachIndexed { row, num -> data[row][column] = num }
        }

        private fun printData() {
            val sb = StringBuilder()
            for (i in data.indices) {
                for (j in data[i].indices) {
                    sb.append("${data[i][j]} ")
                }
                sb.append("\n")
            }
//        Logger.e(sb.toString())
        }

        private fun createRandomData() {
            val random = Random()
            val num = Math.pow(2.0, random.nextInt(2) + 1.0).toInt()
            var row = random.nextInt(4)
            var column = random.nextInt(4)
            while (data[row][column] != 0) {
                row = random.nextInt(4)
                column = random.nextInt(4)
            }
            data[row][column] = num
            refreshListener?.invoke()
        }

        private fun isOver(): Boolean {
            if (!isFull()) {
                return false
            }
            if (canHorizontalSwipe()) {
                return false
            }
            if (canVerticalSwipe()) {
                return false
            }
            return true
        }

        private fun isFull(): Boolean {
            for (r in data.indices) {
                for (c in data.indices) {
                    if (data[r][c] == 0)
                        return false
                }
            }
            return true
        }

        private fun canHorizontalSwipe(): Boolean {
            for (r in data.indices) {
                for (c in 0..data.lastIndex - 1) {
                    if (data[r][c] != 0) {
                        if (data[r][c] == data[r][c + 1]) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        private fun canVerticalSwipe(): Boolean {
            for (c in data.indices) {
                for (r in 0..data.lastIndex - 1) {
                    if (data[r][c] != 0) {
                        if (data[r][c] == data[r + 1][c])
                            return true
                    }
                }
            }
            return false
        }

        enum class SwipeDirection {
            TOP,
            BOTTOM,
            START,
            END
        }
    }

}


