package demos.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import demos.adapter.SmsLocalAdapter;
import demos.module.SmsLocalModule;
import demos.annotations.bind.Bind;
import demos.annotations.bind.BindView;

public class SmsLocalActivity extends BaseActivity {
    @BindView(R.id.rv_sms_local)
    RecyclerView rvSmsLocal;

    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private List<SmsLocalModule> smsLocalModules;
    private SmsLocalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_local);
        Bind.bind(this);

        smsLocalModules = new ArrayList<>();
        getLocalSms();
        adapter = new SmsLocalAdapter(mContext, smsLocalModules);
        rvSmsLocal.setLayoutManager(new GridLayoutManager(mContext, 1));
        rvSmsLocal.setAdapter(adapter);
    }

    public void getLocalSms() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body", "_id", "address", "person", "date", "type"};//"_id", "address", "person",, "date", "type
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            return;
        }
        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));
            String type = cur.getString(cur.getColumnIndex("type"));
            if (number.contains("+86")) {
                number = number.replace("+86", "");
            }
            boolean moduleExist = false;
            for (SmsLocalModule smsLocalModule : smsLocalModules) {
                if (smsLocalModule.getNumber().equals(number)) {
                    smsLocalModule.addBodyAndType(body, type);
                    moduleExist = true;
                    break;
                }
            }
            if (!moduleExist) {
                SmsLocalModule module = new SmsLocalModule();
                module.setNumber(number);
                module.setName(name == null ? "未命名" : name);
                module.addBodyAndType(body, type);
                smsLocalModules.add(module);
            }
        }
        cur.close();
    }

}
