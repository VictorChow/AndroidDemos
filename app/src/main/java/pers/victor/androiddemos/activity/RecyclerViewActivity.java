package pers.victor.androiddemos.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pers.victor.androiddemos.R;

import java.util.ArrayList;

import pers.victor.androiddemos.annotations.bind.Bind;
import pers.victor.androiddemos.annotations.bind.BindView;


public class RecyclerViewActivity extends AppCompatActivity {
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    private Context context;
    private ArrayList<String> items1;
    private ArrayList<String> items2;
    private ArrayList<String> items3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        Bind.bind(this);
        context = getApplicationContext();

        items1 = new ArrayList<>();
        items2 = new ArrayList<>();
        items3 = new ArrayList<>();

        items1.add("");
        items2.add("");
        items3.add("");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (0 <= position && position < items1.size()) {
                    return 3;
                } else if (items1.size() <= position && position < items1.size() + items2.size()) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recycleView.setLayoutManager(gridLayoutManager);
        recycleView.setAdapter(new MyAdapter());
    }


    private class MyAdapter extends RecyclerView.Adapter<Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            Holder holder = null;
            switch (viewType) {
                case 1:
                    holder = new Holder(LayoutInflater.from(context).inflate(R.layout.rv_item_1, parent, false));
                    break;
                case 2:
                    holder = new Holder(LayoutInflater.from(context).inflate(R.layout.rv_item_2, parent, false));
                    break;
                case 3:
                    holder = new Holder(LayoutInflater.from(context).inflate(R.layout.rv_item_3, parent, false));
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (0 <= position && position < items1.size()) {
                holder.tv1_1.setText("占3格");
            } else if (items1.size() <= position && position < items1.size() + items2.size()) {
                holder.tv2_1.setText("占2格");
            } else {
                holder.tv3_1.setText("占1格");
            }
        }

        @Override
        public int getItemCount() {
            return items1.size() + items2.size() + items3.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (0 <= position && position < items1.size()) {
                return 1;
            } else if (items1.size() <= position && position < items1.size() + items2.size()) {
                return 2;
            } else {
                return 3;
            }
        }
    }


    private class Holder extends RecyclerView.ViewHolder {
        public TextView tv1_1;
        public TextView tv2_1;
        public TextView tv3_1;

        public Holder(View itemView) {
            super(itemView);
            tv1_1 = (TextView) itemView.findViewById(R.id.tv_item1_1);
            tv2_1 = (TextView) itemView.findViewById(R.id.tv_item2_1);
            tv3_1 = (TextView) itemView.findViewById(R.id.tv_item3_1);
        }
    }

}
