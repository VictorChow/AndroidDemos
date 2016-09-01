package demos.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.victor.androiddemos.R

/**
 * Created by victor on 16-6-8.
 */
object ImageUtil {
    fun bind(imageView: ImageView, url: String, holder: Int = R.drawable.ic_launcher, error: Int = R.drawable.ic_launcher) {
        Glide.with(imageView.context)
                .load(url)
                .centerCrop()
                .placeholder(holder)
                .error(error)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }
}