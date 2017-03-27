package pers.victor.androiddemos.activity;

import android.support.v4.content.ContextCompat;
import android.view.View;

import pers.victor.androiddemos.R;

import pers.victor.androiddemos.annotations.bus.Bus;
import pers.victor.androiddemos.annotations.bus.BusMethod;
import pers.victor.androiddemos.annotations.bus.MsgEvent;
import pers.victor.androiddemos.view.AutoImageView;

public class ProcessImageActivity extends ToolbarActivity implements Runnable {
    private AutoImageView imageView1;
    private AutoImageView imageView2;
    private boolean isThreadRun;

    @Override
    public int bindLayout() {
        return R.layout.activity_process_image;
    }

    @Override
    public void initView() {
        Bus.register(this);

        imageView1 = $(R.id.auto_image1);
        imageView2 = $(R.id.auto_image2);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isThreadRun) {
                    new Thread(ProcessImageActivity.this).start();
                }
            }
        };
        imageView1.setOnClickListener(onClickListener);
        imageView2.setOnClickListener(onClickListener);
    }

    @BusMethod
    private void updateAutoImageViewProcess(MsgEvent.AutoImageViewProcess e) {
        if (e.getType() == 0) {
            imageView1.setProcess(e.getProcess());
            imageView2.setProcess(e.getProcess());
        } else {
            imageView1.setLoadedDrawable(ContextCompat.getDrawable(this, R.drawable.pic2));
            imageView2.setLoadedDrawable(ContextCompat.getDrawable(this, R.drawable.pic3));
        }
    }

    @Override
    protected void onDestroy() {
        Bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void run() {
        float process = 0f;
        isThreadRun = true;
        while (true) {
            try {
                process += 0.01;
                Bus.postAnnotation(new MsgEvent.AutoImageViewProcess(0, process));
                Thread.sleep(30);
                if (process >= 1.00f) {
                    Bus.postAnnotation(new MsgEvent.AutoImageViewProcess(1, process));
                    process = 0;
                    isThreadRun = false;
                    break;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
