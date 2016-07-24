package demos.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.victor.androiddemos.R
import demos.util.bind.Bind
import demos.util.bind.BindView
import demos.util.bus.Bus
import demos.util.bus.BusMethod
import kotlinx.android.synthetic.main.fragment_first.*


class FirstFragment : Fragment() {

    @BindView(R.id.tv_msg_send)
    private lateinit var tv: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = inflater!!.inflate(R.layout.fragment_first, container, false)!!

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Bus.register(this)
        Bind.bind(this)
        tv.setOnClickListener {
            Bus.postReflect(et_msg_send.text.toString())
            Bus.postAnnotation(et_msg_send.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Bus.unregister(this)
    }

    fun onPostReflect(s: String) {
        tv_msg_reflect.text = s
    }

    @BusMethod
    fun onEvent(s: String) {
        tv_msg_annotation.text = s
    }
}
