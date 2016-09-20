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
import demos.view.FitTextView
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
        initFitTextViews()
        cycle_view_pager.setUp(intArrayOf(R.drawable.bg_main, R.drawable.ic_launcher, R.drawable.iv_myself))
    }

    private fun initReceiversGroup() {
        val list = arrayListOf<ReceiversGroup.Contact>()
        list.add(ReceiversGroup.Contact("11111111111"))
        list.add(ReceiversGroup.Contact("22222222222"))
        list.add(ReceiversGroup.Contact("33333333333", "张三"))
        list.add(ReceiversGroup.Contact("44444444444", "李四"))
        list.add(ReceiversGroup.Contact("55555555555", "王二麻子"))
        list.add(ReceiversGroup.Contact("66666666666"))
        list.add(ReceiversGroup.Contact("77777777777"))
//        list.add(ReceiversGroup.Contact("88888888888"))
        receivers_group.addContacts(list)
    }

    private fun initFitTextViews() {
        val l = arrayListOf<FitTextView>()
        for (i in 0..fit_text_container.childCount - 1) {
            l.add(fit_text_container.getChildAt(i) as FitTextView)
        }
        FitTextView.adjustTextViewsWidth(l)
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
