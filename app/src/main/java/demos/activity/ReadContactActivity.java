package demos.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import demos.adapter.ContactLocalAdapter;
import demos.module.ContactModule;
import demos.annotations.bind.Bind;
import demos.annotations.bind.BindView;

public class ReadContactActivity extends BaseActivity {
    @BindView(R.id.rv_contact_list)
    RecyclerView rvContactList;
    @BindView(R.id.pb_contact_list)
    ProgressBar pbContactList;

    private List<ContactModule> contactModules;
    private ContactLocalAdapter contactLocalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contact);
        Bind.bind(this);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("读取联系人");

        new ReadContactTask().execute();
    }

    /*
     * 读取联系人的信息
     */
    public void testReadAllContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0, nameIndex = 0;
        assert cursor != null;
        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
//            PrintLog.d("id = " + contactId + " name = " + name);

            ContactModule contactModule = new ContactModule();
            contactModule.setName(name);

            //查找该联系人的phone信息
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            int phoneIndex = 0;
            assert phones != null;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
//                PrintLog.d("phone = " + phoneNumber);
                contactModule.addPhone(phoneNumber);
            }
            phones.close();

            if (contactModule.getPhones() != null) {
                contactModules.add(contactModule);
            }
        }
        cursor.close();
    }

    private class ReadContactTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            rvContactList.setLayoutManager(new GridLayoutManager(mContext, 1));
            contactModules = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            testReadAllContacts();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pbContactList.setVisibility(View.GONE);
            rvContactList.setVisibility(View.VISIBLE);

            contactLocalAdapter = new ContactLocalAdapter(mContext, contactModules);
            rvContactList.setAdapter(contactLocalAdapter);
        }
    }


//            /*
//             * 查找该联系人的email信息
//             */
//            Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
//                    null, null);
//            int emailIndex = 0;
//            if (emails.getCount() > 0) {
//                emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//            }
//            while (emails.moveToNext()) {
//                String email = emails.getString(emailIndex);
//                PrintLog.d(email);
//            }
}
