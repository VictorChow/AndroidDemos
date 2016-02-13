package demos.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.victor.androiddemos.R;

import demos.AndroidDemos;

public class TelephonyActivity extends BaseActivity {

    private Button btnSend = null; // 按钮
    private EditText etTelNumber;//手机号输入框
//	private TextView distxtView;

    private static final String TAG = "MainActivity";
    private String tTelNum = "";

    //利用sp全局保存手机号码
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephony);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AndroidDemos.getInstance());

        btnSend = (Button) super.findViewById(R.id.btn_startbroadcast);
        etTelNumber = (EditText) findViewById(R.id.editTelNum);
//		distxtView = (TextView) findViewById(R.id.textView2);


        etTelNumber.setText(sharedPreferences.getString("phoneNum", ""));

        btnSend.setOnClickListener(v -> {
            //实例化广播接收器，原因是广播在Android4.0之后不能自启。
            tTelNum = etTelNumber.getText().toString().trim();


            if (tTelNum.length() < 9 || tTelNum.isEmpty()) {
                Toast toast = Toast.makeText(mContext,"号码错误:不足9位", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Log.i(TAG, "onStart BroadcastReceiverUtil");
                Toast.makeText(mContext, "监听广播已经开启", Toast.LENGTH_SHORT).show();
                sharedPreferences.edit().putString("phoneNum", tTelNum).commit();
                Log.i(TAG, "onStart BroadcastReceiverUtil");
            }
        });

    }
}
