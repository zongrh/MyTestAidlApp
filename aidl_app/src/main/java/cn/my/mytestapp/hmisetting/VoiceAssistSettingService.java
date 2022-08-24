package cn.my.mytestapp.hmisetting;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Timer;

import cn.my.hmiadapter.IVoiceAssistInterface;

/**
 * FileName: VoiceAssistSettingService
 * Author: nanzong
 * Date: 2022/8/20 9:42 上午
 * Description:
 * History:
 */
public class VoiceAssistSettingService extends Service {
    private int SEND_MSG = 1;

    private String mName = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("VoiceAssistSettingService", "onBind " + intent);
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("VoiceAssistSettingService", "onCreate ");

    }

    @Override
    public void onDestroy() {
        Log.e("VoiceAssistSettingService", "onDestroy ");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("VoiceAssistSettingService", "onUnbind ");
        return super.onUnbind(intent);
    }

    private Binder mBinder = new IVoiceAssistInterface.Stub() {

        @Override
        public void changeUserName(String name, String form) throws RemoteException {
            Log.e("VoiceAssistServiceManagerService", "我是服务端获取数据 ： changeUserName  name: " + name + " , form: " + form);
            mName = name;
        }

        @Override
        public String getUserName() throws RemoteException {
            Log.e("VoiceAssistServiceManagerService", "我是服务端获取的数据 ： changeUserName  name: " + mName);
            return mName;
        }

        @Override
        public boolean isFirstOpen() throws RemoteException {
            return false;
        }

        @Override
        public void setFirstOpen(boolean firstOpen) throws RemoteException {
            Log.e("VoiceAssistServiceManagerService", "服务端：setFirstOpen  firstOpen: " + firstOpen);
        }

        @Override
        public boolean isShow() throws RemoteException {
            return false;
        }
    };

}