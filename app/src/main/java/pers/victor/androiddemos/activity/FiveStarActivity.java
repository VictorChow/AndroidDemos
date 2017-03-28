package pers.victor.androiddemos.activity;

import android.graphics.Color;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;
import android.widget.TextView;

import pers.victor.androiddemos.R;
import pers.victor.androiddemos.view.FiveStarView;

/**
 * Created by Victor on 2017/4/1. (ง •̀_•́)ง
 */

public class FiveStarActivity extends ToolbarActivity {
    private int mR = 0;
    private int mG = 0;
    private int mB = 0;

    @Override
    public int bindLayout() {
        return R.layout.activity_five_star;
    }

    @Override
    public void initView() {
        final FiveStarView star = $(R.id.five_star);
        AppCompatSeekBar r = $(R.id.r);
        AppCompatSeekBar g = $(R.id.g);
        AppCompatSeekBar b = $(R.id.b);
        final TextView tvR = $(R.id.tv_r);
        final TextView tvG = $(R.id.tv_g);
        final TextView tvB = $(R.id.tv_b);
        final TextView tvRHex = $(R.id.tv_r_hex);
        final TextView tvGHex = $(R.id.tv_g_hex);
        final TextView tvBHex = $(R.id.tv_b_hex);
        r.setMax(255);
        g.setMax(255);
        b.setMax(255);
        r.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mR = progress;
                star.setColor(Color.rgb(mR, mG, mB));
                tvR.setText(String.valueOf(progress));
                tvRHex.setText(Integer.toHexString(progress).toUpperCase());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        g.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mG = progress;
                star.setColor(Color.rgb(mR, mG, mB));
                tvG.setText(String.valueOf(progress));
                tvGHex.setText(Integer.toHexString(progress).toUpperCase());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mB = progress;
                star.setColor(Color.rgb(mR, mG, mB));
                tvB.setText(String.valueOf(progress));
                tvBHex.setText(Integer.toHexString(progress).toUpperCase());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
