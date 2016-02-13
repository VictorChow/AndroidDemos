package demos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.victor.androiddemos.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import demos.module.AppModule;

/**
 * Created by Victor on 16/2/13.
 */
public class InstalledAppAdapter extends RecyclerView.Adapter<InstalledAppAdapter.Holder> {
    private List<AppModule> appModules;
    private Context context;

    public InstalledAppAdapter(Context context, List<AppModule> appModules) {
        this.appModules = appModules;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_intalled_app, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.ivAppIcon.setImageDrawable(appModules.get(position).getAppIcon());
        holder.tvAppName.setText(appModules.get(position).getAppName());
        holder.tvAppPackage.setText("包名: " + appModules.get(position).getPackageName());
        holder.tvAppVersionName.setText("版本名: " + appModules.get(position).getVersionName());
        holder.tvAppVersionCode.setText("版本号: " + appModules.get(position).getVersionCode());
    }

    @Override
    public int getItemCount() {
        return appModules.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_app_icon)
        ImageView ivAppIcon;
        @Bind(R.id.tv_app_name)
        TextView tvAppName;
        @Bind(R.id.tv_app_package)
        TextView tvAppPackage;
        @Bind(R.id.tv_app_version_name)
        TextView tvAppVersionName;
        @Bind(R.id.tv_app_version_code)
        TextView tvAppVersionCode;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
