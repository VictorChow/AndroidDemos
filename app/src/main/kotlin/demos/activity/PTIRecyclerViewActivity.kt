package demos.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.victor.androiddemos.R
import demos.view.PTIRecyclerView
import kotlinx.android.synthetic.main.activity_pti_recycler.*


/**
 * Created by victor on 16-8-1.
 */
class PTIRecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pti_recycler)

        val list = arrayListOf<String>()
        for (i in 0..2) {
            list.add("")
        }
        rv_group.setUp(object : PTIRecyclerView.Adapter<String>(this, R.layout.rv_pti_item, list) {
            override fun onBindViewHolder(holder: Holder, position: Int) {
            }
        })

    }
}