package com.my.clent;

import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.my.hmiadapter.VoiceAssistServiceManager;


public class MainActivity extends AppCompatActivity {

    private TextView mShowSetNameTv,mShowGetNameTv;

    private EditText mEditText;
    private String mContent = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowSetNameTv = (TextView) findViewById(R.id.show_set_name_text);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mShowGetNameTv = findViewById(R.id.show_get_name_text);


        findViewById(R.id.button_set_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEditText.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(MainActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
                }
                mContent = mContent + "\r\n" + msg;
                mShowSetNameTv.setText(mContent);
                Log.d("VoiceAssistServiceManagerService", "客户端：clent onclick " + mContent);

                VoiceAssistServiceManager.getInstanch().setChangeUserName(mContent, "editText");
            }
        });

        findViewById(R.id.button_get_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = VoiceAssistServiceManager.getInstanch().getUserName();
                Log.d("VoiceAssistServiceManagerService", "客户端获取服务端 User name：" + name);
                mShowGetNameTv.setText(name);
            }
        });

        findViewById(R.id.btn_set_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContent = "";
                mShowSetNameTv.setText("");
                VoiceAssistServiceManager.getInstanch().setFristOpen(true);
            }
        });
    }


    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        /**
         * 当承载IBinder的进程消失时接收回调的接口
         */
        @Override
        public void binderDied() {

        }
    };

    @Override
    protected void onDestroy() {

//        unbindService();
        super.onDestroy();
    }


}
