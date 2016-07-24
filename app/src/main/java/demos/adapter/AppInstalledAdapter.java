package demos.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.victor.androiddemos.R;

import java.util.List;

import demos.activity.AppInfoActivity;
import demos.module.AppModule;
import demos.receiver.PackageMonitorReceiver;
import demos.util.SharedPreferenceUtil;
import demos.util.ShowToast;
import demos.util.bind.Bind;
import demos.util.bind.BindView;

/**
 * Created by Victor on 16/2/13.
 */
public class AppInstalledAdapter extends RecyclerView.Adapter<AppInstalledAdapter.Holder> {
    private List<AppModule> appModules;
    private Context context;
    private PackageMonitorReceiver receiver;
    private boolean isSystemApp;

    public AppInstalledAdapter(Context context, List<AppModule> appModules, boolean isSystemApp) {
        this.appModules = appModules;
        this.context = context;
        this.receiver = ((AppInfoActivity) context).getReceiver();
        this.isSystemApp = isSystemApp;
        this.receiver.addCallback(() -> {
            int pos = SharedPreferenceUtil.getInt(context, "APP_POS", -1);
            boolean isSysApp = SharedPreferenceUtil.getBoolean(context, "IS_SYSTEM_APP", false);
            if (isSystemApp == isSysApp) {
                ShowToast.shortToast("成功卸载" + appModules.get(pos).getAppName());
                appModules.remove(pos);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_intalled_app, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.ivAppIcon.setImageDrawable(appModules.get(position).getAppIcon());
        holder.tvAppName.setText(appModules.get(position).getAppName());
        if (appModules.get(position).getAppPermissions() == null) {
            holder.tvAppPermission.setText("0项权限");
        } else {
            holder.tvAppPermission.setText(appModules.get(position).getAppPermissions().length + "项权限");
        }
        holder.tvAppPackage.setText("包名: " + appModules.get(position).getPackageName());
        holder.tvAppVersionName.setText("版本名: " + appModules.get(position).getVersionName());
        holder.tvAppVersionCode.setText("版本号: " + appModules.get(position).getVersionCode());
        holder.itemView.setOnLongClickListener(v -> {
            if (isSystemApp) {
                new AlertDialog.Builder(context)
                        .setTitle("禁止卸载系统应用")
                        .setMessage("卸载系统应用可能会导致系统不稳定!")
                        .setPositiveButton("OK", (dialog, which) -> {
                        })
                        .show();
            } else {
                SharedPreferenceUtil.setBoolean(context, "IS_SYSTEM_APP", isSystemApp);
                SharedPreferenceUtil.setInt(context, "APP_POS", position);
                uninstallApk(appModules.get(position).getPackageName());
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return appModules.size();
    }

    /* 安装apk */
    public void installApk(String fileName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + fileName), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /* 卸载apk */
    public void uninstallApk(String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_app_icon)
        ImageView ivAppIcon;
        @BindView(R.id.tv_app_name)
        TextView tvAppName;
        @BindView(R.id.tv_app_permission)
        TextView tvAppPermission;
        @BindView(R.id.tv_app_package)
        TextView tvAppPackage;
        @BindView(R.id.tv_app_version_name)
        TextView tvAppVersionName;
        @BindView(R.id.tv_app_version_code)
        TextView tvAppVersionCode;

        public Holder(View itemView) {
            super(itemView);
            Bind.bind(this, itemView);
        }
    }
}
