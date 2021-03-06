package pers.victor.androiddemos.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import pers.victor.androiddemos.R;

import java.util.List;

import pers.victor.androiddemos.annotations.bind.Bind;
import pers.victor.androiddemos.annotations.bind.BindView;
import pers.victor.androiddemos.module.ContactModule;

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
            tv.setTextColor(ContextCompat.getColor(context, R.color.white1));
            tv.setTextSize(20);
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
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.ll_contact_phones)
        LinearLayout llContactPhones;

        Holder(View itemView) {
            super(itemView);
            Bind.bind(this, itemView);
        }
    }

}
