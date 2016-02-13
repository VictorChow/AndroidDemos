package demos.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.victor.androiddemos.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import demos.adapter.VpAppFragmentAdapter;
import demos.receiver.PackageMonitorReceiver;

public class AppInfoActivity extends BaseActivity {

    @Bind(R.id.tab_app)
    TabLayout tabApp;
    @Bind(R.id.vp_app)
    ViewPager vpApp;

    private PackageMonitorReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        ButterKnife.bind(this);
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
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("已安装应用");
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
