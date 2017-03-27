package pers.victor.androiddemos.fragment;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import pers.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import pers.victor.androiddemos.adapter.AppInstalledAdapter;
import pers.victor.androiddemos.module.AppModule;
import pers.victor.androiddemos.annotations.bind.Bind;
import pers.victor.androiddemos.annotations.bind.BindView;


public class AppInfoFragment extends BaseFragment {
    @BindView(R.id.pb_app_info)
    ProgressBar pbAppInfo;
    @BindView(R.id.rv_app_info)
    RecyclerView rvAppInfo;
    private boolean isSystemApp;
    private AppInstalledAdapter adapter;
    private List<AppModule> appList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_app_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bind.bind(this, view);
        rvAppInfo.setLayoutManager(new GridLayoutManager(mContext, 1));
        new GetAppInstalledTask().execute();
    }

    private void getInstalledApp() {
        appList = new ArrayList<>(); //用来存储获取的应用信息数据
        List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppModule tmpInfo = new AppModule();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
            tmpInfo.setPackageName(packageInfo.packageName);
            tmpInfo.setVersionName(packageInfo.versionName);
            tmpInfo.setVersionCode(packageInfo.versionCode);
            tmpInfo.setAppPermissions(packageInfo.requestedPermissions);
            tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()));
//            如果(packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0),则该应用是非系统应用
            tmpInfo.setSystemApp((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            if (tmpInfo.isSystemApp() == isSystemApp) {
                appList.add(tmpInfo);
            }
        }
    }

    public AppInfoFragment setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
        return this;
    }

    private class GetAppInstalledTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            getInstalledApp();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pbAppInfo.setVisibility(View.GONE);
            rvAppInfo.setVisibility(View.VISIBLE);
            adapter = new AppInstalledAdapter(mContext, appList, isSystemApp);
            rvAppInfo.setAdapter(adapter);
        }
    }

}
