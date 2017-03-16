package demos.activity;

import android.view.View;

import com.victor.androiddemos.R;

import demos.view.SnakeView;

public class TetrisActivity extends ToolbarActivity {
    //    private TetrisView tetrisView;
    private SnakeView snakeView;

    @Override
    public int bindLayout() {
        return R.layout.activity_tetris;
    }

    @Override
    public void initView() {
//        tetrisView = $(R.id.tetris_view);

        snakeView = $(R.id.snake_view);

        findViewById(R.id.iv_snake_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.swerve(SnakeView.UP);
            }
        });
        findViewById(R.id.iv_snake_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.swerve(SnakeView.DOWN);
            }
        });
        findViewById(R.id.iv_snake_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.swerve(SnakeView.LEFT);
            }
        });
        findViewById(R.id.iv_snake_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snakeView.swerve(SnakeView.RIGHT);
            }
        });

    }

    @Override
    protected void onDestroy() {
        snakeView.destroyView();
        super.onDestroy();
    }
}
