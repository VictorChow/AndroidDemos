package demos.activity

import android.graphics.Color
import android.widget.SeekBar
import com.victor.androiddemos.R
import kotlinx.android.synthetic.main.activity_five_star.*

/**
 * Copyright (c) 16-9-18 by loren
 */

class FiveStarActivity : ToolbarActivity() {
    private var mR: Int = 0
    private var mG: Int = 0
    private var mB: Int = 0

    override fun bindLayout() = R.layout.activity_five_star

    override fun initView() {
        r.max = 255
        g.max = 255
        b.max = 255
        r.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mR = progress
                five_star.setColor(Color.rgb(mR, mG, mB))
                tv_r.text = progress.toString()
                tv_r_hex.text = Integer.toHexString(progress).toUpperCase()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        g.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mG = progress
                five_star.setColor(Color.rgb(mR, mG, mB))
                tv_g.text = progress.toString()
                tv_g_hex.text = Integer.toHexString(progress).toUpperCase()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        b.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mB = progress
                five_star.setColor(Color.rgb(mR, mG, mB))
                tv_b.text = progress.toString()
                tv_b_hex.text = Integer.toHexString(progress).toUpperCase()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}
