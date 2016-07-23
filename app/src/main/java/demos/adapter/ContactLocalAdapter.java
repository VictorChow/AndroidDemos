package demos.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.victor.androiddemos.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import demos.module.ContactModule;

/**
 * Created by Victor on 16/2/15.
 */
public class ContactLocalAdapter extends RecyclerView.Adapter<ContactLocalAdapter.Holder> {
    private Context context;
    private List<ContactModule> contactModules;

    public ContactLocalAdapter(Context context, List<ContactModule> contactModules) {
        this.context = context;
        this.contactModules = contactModules;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_contact_list, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tvContactName.setText(contactModules.get(position).getName());
        holder.llContactPhones.removeAllViews();
        for (String s : contactModules.get(position).getPhones()) {
            TextView tv = new TextView(context);
            tv.setTextColor(ContextCompat.getColor(context, R.color.text_default));
            tv.setTextSize(14);
            tv.setText(s);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.llContactPhones.addView(tv, lp);
        }
    }

    @Override
    public int getItemCount() {
        return contactModules.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_contact_name)
        TextView tvContactName;
        @Bind(R.id.ll_contact_phones)
        LinearLayout llContactPhones;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
