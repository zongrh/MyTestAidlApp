package cn.my.mytestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Callable;

import cn.my.hmiadapter.VoiceAssistServiceManager;
import cn.my.mytestapp.hmisetting.VoiceAssistSettingService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestModel model = new TestModel();
        model.setShow(false);
        Log.d("MainActivity", "isShow: " + model.isShow());
        Log.d("MainActivity", "isReTestShow: " + model.isReTestShow());



        ThreadPoolManager.getInstance().addMainTask(new Runnable() {
            @Override
            public void run() {

            }
        });
        ThreadPoolManager.getInstance().addExecuteTask(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                return null;
            }
        });
    }
}