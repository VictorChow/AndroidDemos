package demos.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.victor.androiddemos.R
import demos.annotations.bind.Bind
import demos.annotations.bind.BindView
import demos.annotations.bus.Bus
import demos.annotations.bus.BusMethod
import demos.view.ReceiversGroup
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
        initReceiversGroup()
    }

    private fun initReceiversGroup() {
        val list = arrayListOf<ReceiversGroup.Receiver>()
        list.add(ReceiversGroup.Receiver("11111111111"))
        list.add(ReceiversGroup.Receiver("22222222222"))
        list.add(ReceiversGroup.Receiver("33333333333", "张三"))
        list.add(ReceiversGroup.Receiver("44444444444", "李四"))
        list.add(ReceiversGroup.Receiver("55555555555", "王二麻子"))
        list.add(ReceiversGroup.Receiver("66666666666"))
        list.add(ReceiversGroup.Receiver("77777777777"))
        list.add(ReceiversGroup.Receiver("88888888888"))
        receivers_group.addTextViews(list)
        receivers_group.addTextViews(list)
        println(receivers_group.getMobileNumber())
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