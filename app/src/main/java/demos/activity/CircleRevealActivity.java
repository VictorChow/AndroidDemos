package demos.activity;

import android.view.View;

import com.victor.androiddemos.R;

import demos.view.CircleRevealView;

public class CircleRevealActivity extends ToolbarActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_circle_reveal;
    }

    @Override
    public void initView() {
        final CircleRevealView circleRevealView = $(R.id.circle_reveal);
        circleRevealView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleRevealView.circleReveal();
            }
        });
        circleRevealView.postDelayed(new Runnable() {
            @Override
            public void run() {
                circleRevealView.circleReveal();
            }
        }, 1000);
    }
}
