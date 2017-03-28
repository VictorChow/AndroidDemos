package pers.victor.androiddemos.adapter

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pers.victor.androiddemos.R
import pers.victor.androiddemos.activity.ApkListActivity
import pers.victor.androiddemos.util.ApkUtil
import kotlinx.android.synthetic.main.item_apk_list.view.*
import pers.victor.androiddemos.module.ApkModule
import java.io.File

/**
 * Created by Victor on 16/3/24.
 */
class ApkListAdapter(var context: Context, var apkList: MutableList<ApkModule>, var listener: OnItemDeleteListener?) : RecyclerView.Adapter<ApkListAdapter.Holder>() {
    override fun getItemCount() = apkList.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = Holder(LayoutInflater.from(context).inflate(R.layout.item_apk_list, parent, false))

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        if ((context as ApkListActivity).getSearchType() == ApkListActivity.SEARCH_LARGE_FILES)
            holder!!.itemView.tv_apk_install.visibility = View.GONE
        holder!!.itemView.tv_apk_name.text = apkList[position].name
        holder.itemView.tv_apk_path.text = apkList[position].path.subSequence(19, apkList[position].path.length)
        holder.itemView.iv_apk_icon.setImageDrawable(apkList[position].icon)
        holder.itemView.tv_apk_length.text = apkList[position].length
        holder.itemView.tv_apk_install.setOnClickListener { ApkUtil.installApk(context, apkList[position].path) }
        holder.itemView.tv_apk_delete.setOnClickListener {
            Snackbar.make((context as ApkListActivity).window.decorView, "确定删除${apkList[position].name}么?", Snackbar.LENGTH_LONG)
                    .setAction("确定", {
                        File(apkList[position].path).delete()
                        Snackbar.make((context as ApkListActivity).window.decorView, "已删除" + apkList[position].name, Snackbar.LENGTH_LONG).show()
                        apkList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(0, apkList.size)
                        listener?.itemDelete()
                    }).show()
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemDeleteListener {
        fun itemDelete();
    }
}