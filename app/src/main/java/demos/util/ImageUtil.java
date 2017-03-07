package demos.util;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.victor.androiddemos.R;

import demos.AndroidDemos;

/**
 * Created by victor on 16-12-23
 */

public class ImageUtil {
    public static void bind(ImageView imageView, String url, @DrawableRes int holder) {
        Glide.with(imageView.getContext())
                .load(url)
                .asBitmap()
                .placeholder(holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void bind(ImageView imageView, String url) {
        bind(imageView, url, R.drawable.ic_launcher);
    }

    public static void download(String url, final DownloadListener downloadListener) {
        Glide.with(AndroidDemos.getInstance())
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (downloadListener != null) {
                            downloadListener.onComplete(resource);
                        }
                    }
                });
    }


    public interface DownloadListener {
        void onComplete(Bitmap bitmap);
    }
}
