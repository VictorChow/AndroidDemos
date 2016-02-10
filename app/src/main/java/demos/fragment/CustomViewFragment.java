package demos.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victor.androiddemos.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demos.view.AutoImageView;
import demos.view.CountDownView;


public class CustomViewFragment extends Fragment implements Runnable {

    @Bind(R.id.AutoImageView1)
    AutoImageView autoImageView1;
    @Bind(R.id.AutoImageView2)
    AutoImageView autoImageView2;
    @Bind(R.id.count_down_view)
    CountDownView countDownView;

    @OnClick({R.id.tv_auto_image})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_auto_image:
                if (!isThreadRun) {
                    new Thread(this).start();
                }
                break;
        }
    }

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

    public CustomViewFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_view, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        countDownView.setCountDownTime(1, 23, 45);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
