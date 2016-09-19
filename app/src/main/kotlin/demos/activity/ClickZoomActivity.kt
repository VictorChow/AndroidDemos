package demos.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AccelerateDecelerateInterpolator
import com.victor.androiddemos.R
import demos.util.SimpleAnimator
import kotlinx.android.synthetic.main.activity_click_zoom.*

/**
 * Created by victor on 16-8-1.
 */
class ClickZoomActivity : AppCompatActivity() {
    private var lastViewPos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_zoom)


        for (i in 0..flow_layout.childCount - 1) {
            flow_layout.getChildAt(i).setOnClickListener {
                if (i == lastViewPos) return@setOnClickListener
                flow_layout.bringChildToFront(flow_layout.childList[i].child)
                SimpleAnimator(it)
                        .duration(200)
                        .floatValues(1f, 1.5f)
                        .interpolator(AccelerateDecelerateInterpolator())
                        .property("scaleX")
                        .go()
                SimpleAnimator(it)
                        .duration(200)
                        .floatValues(1f, 1.5f)
                        .interpolator(AccelerateDecelerateInterpolator())
                        .property("scaleY")
                        .go()
                if (lastViewPos != -1) {
                    val lastView = flow_layout.childList[lastViewPos].child
                    SimpleAnimator(lastView)
                            .duration(200)
                            .floatValues(1.5f, 1f)
                            .interpolator(AccelerateDecelerateInterpolator())
                            .property("scaleX")
                            .go()
                    SimpleAnimator(lastView)
                            .duration(200)
                            .floatValues(1.5f, 1f)
                            .interpolator(AccelerateDecelerateInterpolator())
                            .property("scaleY")
                            .go()
                }
                lastViewPos = i
            }

        }
    }
}