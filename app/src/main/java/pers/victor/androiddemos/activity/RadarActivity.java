package pers.victor.androiddemos.activity;

import android.animation.Animator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pers.victor.androiddemos.R;
import pers.victor.androiddemos.util.DisplayUtil;
import pers.victor.androiddemos.util.SimpleAnimator;
import pers.victor.androiddemos.view.RadarView;

public class RadarActivity extends ToolbarActivity {
    private FrameLayout frameLayout;
    private int pointSize = DisplayUtil.dp2px(12);

    @Override
    public int bindLayout() {
        return R.layout.activity_radar;
    }

    @Override
    public void initView() {
        RadarView radarView = (RadarView) findViewById(R.id.radar_view);
        radarView.post(() -> {
            radarView.startAnim();
            createPoint();
        });
        frameLayout = (FrameLayout) findViewById(R.id.fl_radar);
    }

    private void createPoint() {
        Observable.interval(2, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> {
                    ImageView view = new ImageView(this);
                    view.setImageResource(R.drawable.bg_white_gradient);
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(pointSize, pointSize);
                    frameLayout.addView(view, lp);
                    view.setX(getRandomPosition());
                    view.setY(getRandomPosition());
                    SimpleAnimator.create(view)
                            .property("alpha")
                            .values(0f, 1f, 1f, 0f)
                            .duration(5000)
                            .interpolator(new DecelerateInterpolator())
                            .listener(new SimpleAnimator.AnimatorListener() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    frameLayout.removeView(view);
                                }
                            })
                            .go();
                });
    }

    private float getRandomPosition() {
        return new Random().nextInt(DisplayUtil.dp2px(200)) + DisplayUtil.dp2px(50);
    }
}
