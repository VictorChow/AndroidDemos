package demos.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.victor.androiddemos.R;

import demos.activity.A2048Activity;
import demos.activity.AidlActivity;
import demos.activity.ApkListActivity;
import demos.activity.AppInfoActivity;
import demos.activity.AppManageActivity;
import demos.activity.BatteryActivity;
import demos.activity.CallInterceptActivity;
import demos.activity.CircleRevealActivity;
import demos.activity.ClearCacheActivity;
import demos.activity.CycleViewPagerActivity;
import demos.activity.EventActivity;
import demos.activity.FitTextViewActivity;
import demos.activity.FiveStarActivity;
import demos.activity.FixedFlowLayoutActivity;
import demos.activity.FlymeDownloadActivity;
import demos.activity.GestureActivity;
import demos.activity.HeightWeightActivity;
import demos.activity.LotteryDrawActivity;
import demos.activity.MainActivity;
import demos.activity.PTIRecyclerViewActivity;
import demos.activity.PayPassWordActivity;
import demos.activity.PhoneInfoActivity;
import demos.activity.ProcessImageActivity;
import demos.activity.ReadContactActivity;
import demos.activity.ReceiverGroupActivity;
import demos.activity.ScratchCardActivity;
import demos.activity.ShareSheetActivity;
import demos.activity.SmartGoActivity;
import demos.activity.SmsLocalActivity;
import demos.activity.SmsMonitorActivity;
import demos.activity.TelephonyActivity;
import demos.activity.TetrisActivity;
import demos.activity.VerticalMenuActivity;
import demos.activity.XiuyixiuActivity;
import demos.annotations.bus.Bus;
import demos.drag_recycleview.DragRecycleViewActivity;
import demos.util.ShowToast;

public class ViewFragment extends BaseFragment implements View.OnTouchListener {
    public static final int LAYOUT_VIEW = -1;
    public static final int LAYOUT_FUNCTION = -2;
    private Activity activity;
    private ViewConfiguration configuration;
    private float downRawX;
    private float downRawY;

    @Override
    public void onAttach(Context context) {
        this.activity = (Activity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int layoutId = bundle.getInt("type") == LAYOUT_VIEW ? R.layout.fragment_view : R.layout.fragment_func;
        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        configuration = ViewConfiguration.get(activity);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_tv_container);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            linearLayout.getChildAt(i).setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downRawX = event.getRawX();
                downRawY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (event.getRawX() - downRawX <= configuration.getScaledTouchSlop() && event.getRawY() - downRawY <= configuration.getScaledTouchSlop()) {
                    Point point = new Point();
                    point.x = (int) event.getRawX();
                    point.y = (int) event.getRawY();
                    v.playSoundEffect(SoundEffectConstants.CLICK);
                    switchActivity(v, point);
                }
                break;
        }
        return true;
    }

    private void switchActivity(final View view, final Point point) {
        Class clazz = null;
        switch (view.getId()) {
            case R.id.tv_ripple:
                clazz = XiuyixiuActivity.class;
                break;
            case R.id.tv_menu_layout:
                Bus.postAnnotation(new MainActivity.OpenMenu());
                return;
            case R.id.tv_share_sheet:
                clazz = ShareSheetActivity.class;
                break;
            case R.id.tv_view_pager:
                clazz = CycleViewPagerActivity.class;
                break;
            case R.id.tv_pti_recycler_view:
                clazz = PTIRecyclerViewActivity.class;
                break;
            case R.id.tv_receivers_group:
                clazz = ReceiverGroupActivity.class;
                break;
            case R.id.tv_fit_text:
                clazz = FitTextViewActivity.class;
                break;
            case R.id.tv_fixed_flow:
                clazz = FixedFlowLayoutActivity.class;
                break;
            case R.id.tv_lottery_draw:
                clazz = LotteryDrawActivity.class;
                break;
            case R.id.tv_flyme_download:
                clazz = FlymeDownloadActivity.class;
                break;
            case R.id.tv_circle_reveal:
                clazz = CircleRevealActivity.class;
                break;
            case R.id.tv_pay_pwd:
                clazz = PayPassWordActivity.class;
                break;
            case R.id.tv_drag_recycler_view:
                clazz = DragRecycleViewActivity.class;
                break;
            case R.id.tv_vertical_menu:
                clazz = VerticalMenuActivity.class;
                break;
            case R.id.tv_scratch_card:
                clazz = ScratchCardActivity.class;
                break;
            case R.id.tv_process_image:
                clazz = ProcessImageActivity.class;
                break;
            case R.id.tv_five_star:
                clazz = FiveStarActivity.class;
                break;
            case R.id.tv_height_weight:
                clazz = HeightWeightActivity.class;
                break;
            case R.id.tv_contacts:
                clazz = ReadContactActivity.class;
                break;
            case R.id.tv_installed_app:
                clazz = AppInfoActivity.class;
                break;
            case R.id.tv_telephony:
                clazz = TelephonyActivity.class;
                break;
            case R.id.tv_local_msg:
                clazz = SmsLocalActivity.class;
                break;
            case R.id.tv_listen_msg:
                clazz = SmsMonitorActivity.class;
                break;
            case R.id.tv_2048:
                clazz = A2048Activity.class;
                break;
            case R.id.tv_aidl:
                clazz = AidlActivity.class;
                break;
            case R.id.tv_gesture:
                clazz = GestureActivity.class;
                break;
            case R.id.tv_event:
                clazz = EventActivity.class;
                break;
            case R.id.tv_smart_go:
                clazz = SmartGoActivity.class;
                break;
            case R.id.tv_phone_info:
                clazz = PhoneInfoActivity.class;
                break;
            case R.id.tv_sdcard:
                clazz = AppManageActivity.class;
                break;
            case R.id.tv_scan_file:
                final Class finalClazz = ApkListActivity.class;
                new AlertDialog.Builder(mActivity)
                        .setTitle("问一下")
                        .setMessage("你扫啥?")
                        .setPositiveButton("大文件", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = "扫描大文件";
                                activity.startActivity(new Intent(activity, finalClazz).putExtra("position", point).putExtra("title", title).putExtra("search_type", 2));
                                activity.overridePendingTransition(R.anim.anim_stay, R.anim.anim_stay);
                            }
                        })
                        .setNegativeButton("APK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = "扫描APK";
                                activity.startActivity(new Intent(activity, finalClazz).putExtra("position", point).putExtra("title", title).putExtra("search_type", 1));
                                activity.overridePendingTransition(R.anim.anim_stay, R.anim.anim_stay);
                            }
                        })
                        .setNeutralButton("缓存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = "扫描缓存";
                                activity.startActivity(new Intent(activity, ClearCacheActivity.class).putExtra("position", point).putExtra("title", title));
                                activity.overridePendingTransition(R.anim.anim_stay, R.anim.anim_stay);
                            }
                        })
                        .show();
                return;
            case R.id.tv_battery_info:
                clazz = BatteryActivity.class;
                break;
            case R.id.tv_intercept_call:
                clazz = CallInterceptActivity.class;
                break;
            case R.id.tv_swipe_finish:
                ShowToast.shortToast("随便打开一个都是啊");
                return;
            case R.id.tv_tetris:
                clazz = TetrisActivity.class;
                break;
        }
        String title = ((TextView) view).getText().toString();
        activity.startActivity(new Intent(activity, clazz).putExtra("position", point).putExtra("title", title));
        activity.overridePendingTransition(R.anim.anim_stay, R.anim.anim_stay);
    }
}
