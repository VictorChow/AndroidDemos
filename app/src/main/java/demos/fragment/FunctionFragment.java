package demos.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victor.androiddemos.R;

import demos.activity.A2048Activity;
import demos.activity.AppInfoActivity;
import demos.activity.ClearCacheActivity;
import demos.activity.ClickZoomActivity;
import demos.activity.FiveStarActivity;
import demos.activity.GetLocationActivity;
import demos.activity.PTIRecyclerViewActivity;
import demos.activity.PayPassWordActivity;
import demos.activity.ReadContactActivity;
import demos.activity.RecyclerViewActivity;
import demos.activity.ServiceActivity;
import demos.activity.SlideToCloseActivity;
import demos.activity.SmsLocalActivity;
import demos.activity.SmsMonitorActivity;
import demos.activity.TakePhotoActivity;
import demos.activity.TelephonyActivity;
import demos.activity.VerticalMenuActivity;
import demos.annotations.bind.Bind;
import demos.annotations.bind.BindClick;
import demos.drag_recycleview.DragRecycleViewActivity;
import demos.util.ShowToast;


public class FunctionFragment extends BaseFragment {

    @BindClick({R.id.btn_slide_close, R.id.btn_service, R.id.btn_recycler_view, R.id.btn_sms_monitor,
            R.id.btn_telephony_monitor, R.id.btn_installed_app, R.id.btn_clear_cache, R.id.btn_sms_local,
            R.id.btn_all_contact, R.id.btn_open_camera, R.id.btn_get_location, R.id.btn_drag_recycler_view,
            R.id.btn_vertical_menu, R.id.btn_pti_recycler, R.id.btn_2048, R.id.btn_pay_pwd, R.id.btn_click_zoom,
            R.id.btn_five_star, R.id.float_btn})
    void onItemClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pti_recycler:
                startActivity(new Intent(mContext, PTIRecyclerViewActivity.class));
                break;
            case R.id.btn_slide_close:
                startActivity(new Intent(mContext, SlideToCloseActivity.class));
                break;
            case R.id.btn_service:
                startActivity(new Intent(mContext, ServiceActivity.class));
                break;
            case R.id.btn_recycler_view:
                startActivity(new Intent(mContext, RecyclerViewActivity.class));
                break;
            case R.id.btn_sms_monitor:
                startActivity(new Intent(mContext, SmsMonitorActivity.class));
                break;
            case R.id.btn_telephony_monitor:
                startActivity(new Intent(mContext, TelephonyActivity.class));
                break;
            case R.id.btn_installed_app:
                startActivity(new Intent(mContext, AppInfoActivity.class));
                break;
            case R.id.btn_clear_cache:
                startActivity(new Intent(mContext, ClearCacheActivity.class));
                break;
            case R.id.btn_sms_local:
                startActivity(new Intent(mContext, SmsLocalActivity.class));
                break;
            case R.id.btn_all_contact:
                startActivity(new Intent(mContext, ReadContactActivity.class));
                break;
            case R.id.btn_open_camera:
                startActivity(new Intent(mContext, TakePhotoActivity.class));
                break;
            case R.id.btn_get_location:
                startActivity(new Intent(mContext, GetLocationActivity.class));
                break;
            case R.id.btn_drag_recycler_view:
                startActivity(new Intent(mContext, DragRecycleViewActivity.class));
                break;
            case R.id.btn_vertical_menu:
                startActivity(new Intent(mContext, VerticalMenuActivity.class));
                break;
            case R.id.btn_2048:
                startActivity(new Intent(mContext, A2048Activity.class));
                break;
            case R.id.btn_pay_pwd:
                startActivity(new Intent(mContext, PayPassWordActivity.class));
                break;
            case R.id.btn_click_zoom:
                startActivity(new Intent(mContext, ClickZoomActivity.class));
                break;
            case R.id.btn_five_star:
                startActivity(new Intent(mContext, FiveStarActivity.class));
                break;
            case R.id.float_btn:
                ShowToast.shortToast("哦？");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_function, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bind.bind(this, view);
    }
}
