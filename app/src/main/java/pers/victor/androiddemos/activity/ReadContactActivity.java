package pers.victor.androiddemos.activity;

import android.Manifest;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import pers.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import pers.victor.androiddemos.adapter.ContactLocalAdapter;
import pers.victor.androiddemos.annotations.bind.Bind;
import pers.victor.androiddemos.annotations.bind.BindView;
import pers.victor.androiddemos.module.ContactModule;
import pers.victor.androiddemos.util.ShowToast;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ReadContactActivity extends ToolbarActivity {
    @BindView(R.id.rv_contact_list)
    RecyclerView rvContactList;
    @BindView(R.id.pb_contact_list)
    ProgressBar pbContactList;

    private List<ContactModule> contactModules;
    private ContactLocalAdapter contactLocalAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_read_contact;
    }

    @Override
    public void initView() {
        Bind.bind(this);
        rxPermissions
                .request(Manifest.permission.READ_CONTACTS)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if (granted) {
                            new ReadContactTask().execute();
                        } else {
                            ShowToast.shortToast("申请READ_CONTACTS权限失败");
                        }
                    }
                });
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
            rvContactList.setLayoutManager(new GridLayoutManager(ReadContactActivity.this, 1));
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

            contactLocalAdapter = new ContactLocalAdapter(ReadContactActivity.this, contactModules);
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
