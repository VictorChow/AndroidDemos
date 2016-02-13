package demos.fragment;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import demos.adapter.AppInstalledAdapter;
import demos.module.AppModule;


public class AppInfoFragment extends BaseFragment {
    private boolean isSystemApp;
    private AppInstalledAdapter adapter;

    @Bind(R.id.rv_app_info)
    RecyclerView rvAppInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_info, container, false);
        ButterKnife.bind(this, view);
        rvAppInfo.setLayoutManager(new GridLayoutManager(mContext, 1));
        getInstalledApp();
        return view;
    }

    private void getInstalledApp() {
        List<AppModule> appList = new ArrayList<>(); //用来存储获取的应用信息数据
        List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppModule tmpInfo = new AppModule();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
            tmpInfo.setPackageName(packageInfo.packageName);
            tmpInfo.setVersionName(packageInfo.versionName);
            tmpInfo.setVersionCode(packageInfo.versionCode);
            tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()));
//            如果(packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0),则该应用是非系统应用
            tmpInfo.setSystemApp((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            if (tmpInfo.isSystemApp() == isSystemApp) {
                appList.add(tmpInfo);
            }
        }

        adapter = new AppInstalledAdapter(mContext, appList, isSystemApp);
        rvAppInfo.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public AppInfoFragment setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
        return this;
    }

}
