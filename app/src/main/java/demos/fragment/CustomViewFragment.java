package demos.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import demos.annotations.bus.MsgEvent;
import demos.annotations.bind.Bind;
import demos.annotations.bind.BindClick;
import demos.annotations.bind.BindView;
import demos.annotations.bus.Bus;
import demos.annotations.bus.BusMethod;
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
    private boolean isThreadRun;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.arg1 == 0) {
//                autoImageView1.setProcess(mProcess);
//                autoImageView2.setProcess(mProcess);
//            } else {
//                autoImageView1.setLoadedDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.iv_scratch_card));
//                autoImageView2.setLoadedDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.iv_scratch_card));
//            }
//        }
//    };

    @BusMethod
    private void updateAutoImageViewProcess(MsgEvent.AutoImageViewProcess e) {
        if (e.getType() == 0) {
            autoImageView1.setProcess(e.getProcess());
            autoImageView2.setProcess(e.getProcess());
        } else {
            autoImageView1.setLoadedDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.iv_scratch_card));
            autoImageView2.setLoadedDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.iv_scratch_card));
        }
    }

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
        Bind.bind(this);
        Bus.register(this);
        init();
    }

    @Override
    public void onDestroyView() {
        Bus.unregister(this);
        super.onDestroyView();
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
