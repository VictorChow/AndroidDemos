package demos.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.victor.androiddemos.R;

import java.util.List;

import demos.module.SmsLocalModule;
import demos.util.bind.Bind;
import demos.util.bind.BindView;

/**
 * Created by Victor on 16/2/14.
 */
public class SmsLocalAdapter extends RecyclerView.Adapter<SmsLocalAdapter.Holder> {
    private RecyclerView recyclerView;
    private Context context;
    private List<SmsLocalModule> modules;

    public SmsLocalAdapter(Context context, List<SmsLocalModule> modules) {
        this.context = context;
        this.modules = modules;
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_local_sms, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tvSmsCount.setText(modules.get(position).getBodies().size() + "项记录");
        holder.tvSmsName.setText(modules.get(position).getName());
        holder.tvSmsPhone.setText(modules.get(position).getNumber());
        holder.itemView.setOnClickListener(v -> {
            if (recyclerView.getParent() != null) {
                ((ViewGroup) recyclerView.getParent()).removeView(recyclerView);
            }
            SmsLocalDetailAdapter adapter = new SmsLocalDetailAdapter(context, modules.get(position).getBodies(), modules.get(position).getTypes());
            recyclerView.setAdapter(adapter);
            new AlertDialog.Builder(context)
                    .setTitle(modules.get(position).getNumber() + "(" + modules.get(position).getName() + ")")
                    .setView(recyclerView)
                    .setPositiveButton("好", (dialog, which) -> {
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sms_phone)
        TextView tvSmsPhone;
        @BindView(R.id.tv_sms_name)
        TextView tvSmsName;
        @BindView(R.id.tv_sms_count)
        TextView tvSmsCount;

        public Holder(View itemView) {
            super(itemView);
            Bind.bind(this, itemView);
        }
    }

}
