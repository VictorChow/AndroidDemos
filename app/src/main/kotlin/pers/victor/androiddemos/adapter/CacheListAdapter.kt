package pers.victor.androiddemos.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pers.victor.androiddemos.R
import pers.victor.androiddemos.module.AppModule
import kotlinx.android.synthetic.main.item_clear_cache.view.*

/**
 * Created by Victor on 16/3/27.
 */
class CacheListAdapter(var context: Context, var cacheList: MutableList<AppModule>, var sizeList: MutableList<String>) : RecyclerView.Adapter<CacheListAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder!!.itemView.tv_item_cache_name.text = cacheList[position].appName
        holder.itemView.iv_item_cache_drawable.setImageDrawable(cacheList[position].appIcon)
        holder.itemView.tv_item_cache_size.text = sizeList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = Holder(LayoutInflater.from(context).inflate(R.layout.item_clear_cache, parent, false))

    override fun getItemCount() = cacheList.size


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
