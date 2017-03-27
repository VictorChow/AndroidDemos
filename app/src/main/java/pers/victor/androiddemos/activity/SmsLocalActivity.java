package pers.victor.androiddemos.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import pers.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import pers.victor.androiddemos.adapter.SmsLocalAdapter;
import pers.victor.androiddemos.annotations.bind.Bind;
import pers.victor.androiddemos.annotations.bind.BindView;
import pers.victor.androiddemos.module.SmsLocalModule;

public class SmsLocalActivity extends ToolbarActivity {
    @BindView(R.id.rv_sms_local)
    RecyclerView rvSmsLocal;

    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private List<SmsLocalModule> smsLocalModules = new ArrayList<>();
    private SmsLocalAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_sms_local;
    }

    @Override
    public void initView() {
        Bind.bind(this);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                getLocalSms();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter = new SmsLocalAdapter(SmsLocalActivity.this, smsLocalModules);
                rvSmsLocal.setLayoutManager(new GridLayoutManager(SmsLocalActivity.this, 1));
                rvSmsLocal.setAdapter(adapter);
            }
        }.execute();

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
            String name = getContactNameByAddress(number);
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

    // 根据手机号得到名字
    private String getContactNameByAddress(String phoneNumber) {
        Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cur = getContentResolver().query(personUri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                int nameIdx = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                String name = cur.getString(nameIdx);
                cur.close();
                return name;
            }
        }
        return "未知";
    }

}
