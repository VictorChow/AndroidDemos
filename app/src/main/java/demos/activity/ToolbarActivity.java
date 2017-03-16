package demos.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.victor.androiddemos.R;

import demos.AndroidDemos;

/**
 * Created by Victor on 2017/3/6. (ง •̀_•́)ง
 */

public abstract class ToolbarActivity extends SwipeFinishActivity {
    protected RxPermissions rxPermissions;
    private Point point;
    private boolean isExitAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(this);
        point = getIntent().getParcelableExtra("position");

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        View contentView = View.inflate(this, R.layout.activity_toolbar, null);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        LinearLayout llRoot = (LinearLayout) contentView.findViewById(R.id.ll_toolbar_content);
        View content = getLayoutInflater().inflate(bindLayout(), llRoot, false);
        llRoot.addView(content);
        setContentView(contentView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExitAnim) {
                    return;
                }
                isExitAnim = true;
                animAtFinish();
            }
        });

        initView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                animAtStart();
            }
        });
    }

    private void animAtStart() {
        Animator animator = ViewAnimationUtils.createCircularReveal(findViewById(R.id.ll_toolbar_content),
                point.x,
                point.y,
                0,
                getMaxRadius());
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    private void animAtFinish() {
        Animator animator = ViewAnimationUtils.createCircularReveal(findViewById(R.id.ll_toolbar_content),
                point.x,
                point.y,
                getMaxRadius(),
                0);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                findViewById(R.id.ll_toolbar_content).setVisibility(View.INVISIBLE);
                finish();
                overridePendingTransition(R.anim.anim_stay, R.anim.anim_stay);
            }
        });
        animator.start();
    }

    private float getMaxRadius() {
        int maxHorizontal = point.x > AndroidDemos.screenWidth / 2 ? point.x : AndroidDemos.screenWidth - point.x;
        int maxVertical = point.y > AndroidDemos.screenHeight / 2 ? point.y : AndroidDemos.screenHeight - point.y;
        return (float) Math.sqrt(Math.pow(maxHorizontal, 2) + Math.pow(maxVertical, 2));
    }

    @LayoutRes
    public abstract int bindLayout();

    public abstract void initView();

    @Override
    public void onBackPressed() {
        if (isExitAnim) {
            return;
        }
        isExitAnim = true;
        animAtFinish();
    }

    protected <T> T $(int id) {
        return (T) findViewById(id);
    }
}
