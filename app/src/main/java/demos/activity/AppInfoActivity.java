package demos.activity;

import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.victor.androiddemos.R;

import demos.adapter.VpAppFragmentAdapter;
import demos.annotations.bind.Bind;
import demos.annotations.bind.BindView;
import demos.receiver.PackageMonitorReceiver;

public class AppInfoActivity extends ToolbarActivity {

    @BindView(R.id.tab_app)
    TabLayout tabApp;
    @BindView(R.id.vp_app)
    ViewPager vpApp;

    private PackageMonitorReceiver receiver;

    @Override
    public int bindLayout() {
        return R.layout.activity_app_info;
    }

    @Override
    public void initView() {
        Bind.bind(this);
        receiver = new PackageMonitorReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        registerReceiver(receiver, intentFilter);

        VpAppFragmentAdapter appAdapter = new VpAppFragmentAdapter(getSupportFragmentManager());
        vpApp.setAdapter(appAdapter);
        tabApp.setupWithViewPager(vpApp);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public PackageMonitorReceiver getReceiver() {
        return receiver;
    }
}
