package demos.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victor.androiddemos.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import demos.activity.AppInfoActivity;
import demos.activity.RecyclerViewActivity;
import demos.activity.ServiceActivity;
import demos.activity.SlideToCloseActivity;
import demos.activity.SmsMonitorActivity;
import demos.activity.TelephonyActivity;


public class FunctionFragment extends Fragment {

    @OnClick({R.id.btn_slide_close, R.id.btn_service, R.id.btn_recycler_view, R.id.btn_sms_monitor,
            R.id.btn_telephony_monitor, R.id.btn_installed_app})
    void onItemClick(View v) {
        switch (v.getId()) {
            case R.id.btn_slide_close:
                startActivity(new Intent(getActivity(), SlideToCloseActivity.class));
                break;
            case R.id.btn_service:
                startActivity(new Intent(getActivity(), ServiceActivity.class));
                break;
            case R.id.btn_recycler_view:
                startActivity(new Intent(getActivity(), RecyclerViewActivity.class));
                break;
            case R.id.btn_sms_monitor:
                startActivity(new Intent(getActivity(), SmsMonitorActivity.class));
                break;
            case R.id.btn_telephony_monitor:
                startActivity(new Intent(getActivity(), TelephonyActivity.class));
                break;
            case R.id.btn_installed_app:
                startActivity(new Intent(getActivity(), AppInfoActivity.class));
                break;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_function, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
