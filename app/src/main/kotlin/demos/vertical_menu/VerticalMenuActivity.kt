package demos.vertical_menu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.victor.androiddemos.R
import demos.util.ShowToast
import kotlinx.android.synthetic.main.activity_vertical_menu.*

/**
 * Created by victor on 16-8-1.
 */
class VerticalMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_menu)
        for (i in 0..vertical_menu_layout.childCount - 1) {
            vertical_menu_layout.getChildAt(i).setOnClickListener {
                ShowToast.shortToast("第 $i 个")
            }
        }
    }
}