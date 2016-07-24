package demos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import demos.util.bind.Bind;
import demos.util.bind.BindView;


/**
 * Created by Victor on 16/2/14.
 */
public class SmsLocalDetailAdapter extends RecyclerView.Adapter<SmsLocalDetailAdapter.Holder> {
    private static final String TYPE_RECEIVED = "1";
    private static final String TYPE_SEND = "2";

    private Context context;
    private List<String> bodies;
    private List<String> types;

    public SmsLocalDetailAdapter(Context context, List<String> bodies, List<String> types) {
        this.context = context;
        this.bodies = new ArrayList<>(bodies);
        this.types = new ArrayList<>(types);
        Collections.reverse(this.bodies);
        Collections.reverse(this.types);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_local_sms_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (types.get(position).equals(TYPE_RECEIVED)) {
            holder.tvSmsLeft.setText(bodies.get(position));
            holder.tvSmsLeft.setVisibility(View.VISIBLE);
            holder.tvSmsRight.setVisibility(View.GONE);
        }
        if (types.get(position).equals(TYPE_SEND)) {
            holder.tvSmsRight.setText(bodies.get(position));
            holder.tvSmsRight.setVisibility(View.VISIBLE);
            holder.tvSmsLeft.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bodies.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sms_left)
        TextView tvSmsLeft;
        @BindView(R.id.tv_sms_right)
        TextView tvSmsRight;

        public Holder(View itemView) {
            super(itemView);
            Bind.bind(this, itemView);
        }
    }

}
