package demos.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.victor.androiddemos.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import demos.adapter.VpAppFragmentAdapter;

public class AppInfoActivity extends BaseActivity {

    @Bind(R.id.tab_app)
    TabLayout tabApp;
    @Bind(R.id.vp_app)
    ViewPager vpApp;

    private VpAppFragmentAdapter appAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        ButterKnife.bind(this);
        appAdapter = new VpAppFragmentAdapter(getSupportFragmentManager());
        vpApp.setAdapter(appAdapter);
        tabApp.setupWithViewPager(vpApp);
        getSupportActionBar().setTitle("已安装应用");
    }

}
