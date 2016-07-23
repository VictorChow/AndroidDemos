package demos.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.victor.androiddemos.R
import demos.util.Bus
import demos.util.BusMethod
import kotlinx.android.synthetic.main.fragment_first.*


class FirstFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = inflater!!.inflate(R.layout.fragment_first, container, false)!!

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Bus.register(this)
        tv_msg_send.setOnClickListener {
            Bus.postReflect(et_msg_send.text.toString())
            Bus.postAnnotation(et_msg_send.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Bus.unregister(this)
    }

    fun postReflect(s: String) {
        tv_msg_reflect.text = s
    }

    @BusMethod
    fun onEvent(s: String) {
        tv_msg_annotation.text = s
    }
}
