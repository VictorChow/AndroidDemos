package demos.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import demos.util.bind.BindClick;
import demos.util.bind.BindUtil;
import demos.util.bind.BindView;
import demos.util.bus.Bus;
import demos.view.AutoImageView;
import demos.view.CountDownView;
import demos.view.MarqueeView;


public class CustomViewFragment extends Fragment implements Runnable {

    @BindView(R.id.AutoImageView1)
    AutoImageView autoImageView1;
    @BindView(R.id.AutoImageView2)
    AutoImageView autoImageView2;
    @BindView(R.id.count_down_view)
    CountDownView countDownView;
    @BindView(R.id.marquee_view)
    MarqueeView marqueeView;
    private float mProcess;
    private boolean isThreadRun;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                autoImageView1.setProcess(mProcess);
                autoImageView2.setProcess(mProcess);
            } else {
                autoImageView1.setLoadedDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.iv_scratch_card));
                autoImageView2.setLoadedDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.iv_scratch_card));
            }
        }
    };

    @BindClick({R.id.tv_auto_image})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_auto_image:
                if (!isThreadRun) {
                    new Thread(this).start();
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        BindUtil.bind(this);
        Bus.INSTANCE.register(this);
        init();
    }

    private void init() {
        countDownView.setCountDownTime(1, 23, 45);
        List<String> data = new ArrayList<>();
        data.add("一一一一一");
        data.add("二二二二二");
        data.add("三三三三三");
        data.add("四四四四四");
        data.add("五五五五五");
        data.add("六六六六六");
        marqueeView.setContentData(data);
    }

    @Override
    public void run() {
        isThreadRun = true;
        while (true) {
            try {
                mProcess += 0.01;
                Message msg1 = new Message();
                msg1.arg1 = 0;
                handler.sendMessage(msg1);
                Thread.sleep(30);
                if (mProcess >= 1.00f) {
                    Message msg2 = new Message();
                    msg2.arg1 = 1;
                    handler.sendMessage(msg2);
                    mProcess = 0;
                    isThreadRun = false;
                    break;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
