package cn.my.hmiadapter;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: VoiceAssistServiceManager
 * Author: nanzong
 * Date: 2022/8/20 9:17 上午
 * Description:
 * History:
 */
public class VoiceAssistServiceManager {
    private static final int MSG_RECONNECT = 1;
    private IVoiceAssistInterface mIVoiceAssistInterface;
    private Context mContext;
    private IBinderCallback mIBinderCallback;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == MSG_RECONNECT) {
                bind(mContext, mIBinderCallback);
            }
        }
    };

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("VoiceAssistServiceManagerService", "VoiceAssistServiceManager connect service " + iBinder.toString());
            mIVoiceAssistInterface = IVoiceAssistInterface.Stub.asInterface(iBinder);
            mHandler.removeMessages(MSG_RECONNECT);

            try {
                Log.d("VoiceAssistServiceManagerService", "VoiceAssistServiceManager connect service " + mIVoiceAssistInterface.getUserName());
                iBinder.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (mIBinderCallback != null) {
                mIBinderCallback.onBindSuccess();
            }
        }

        /**
         * 给binder设置死亡代理
         */
        private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                if (mIVoiceAssistInterface == null) {
                    return;
                }
                // 在创建ServiceConnection的匿名类中的onServiceConnected方法中
                // 设置死亡代理
                mIVoiceAssistInterface.asBinder().unlinkToDeath(mDeathRecipient, 0);
                mIVoiceAssistInterface = null;
                Log.d("VoiceAssistServiceManagerService", "VoiceAssistServiceManager onServiceDisconnected  设置死亡代理" );
                //这里重新绑定服务
                bind(mContext, mIBinderCallback);
            }
        };

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("VoiceAssistServiceManagerService", "VoiceAssistServiceManager onServiceDisconnected  " + componentName.toString());
            mIVoiceAssistInterface = null;
            mHandler.removeMessages(MSG_RECONNECT);
            mHandler.sendEmptyMessage(MSG_RECONNECT);
        }
    };

    private VoiceAssistServiceManager() {
    }

    private static class SingletonHolder {
        private static final VoiceAssistServiceManager INSTANCE = new VoiceAssistServiceManager();
    }

    public static VoiceAssistServiceManager getInstanch() {
        return SingletonHolder.INSTANCE;
    }


    public void bind(Context context, IBinderCallback callback) {
        Log.d("VoiceAssistServiceManagerService", "VoiceAssistServiceManager bind  ");
        Log.d("VoiceAssistServiceManagerService", "VoiceAssistServiceManager bind  " + context.getPackageName());

        if (!checkPermission(context.getPackageName())) {
            Log.d("VoiceAssistServiceManagerService", " permission died , con not connect service   ");
            return;
        }
        if (mIVoiceAssistInterface == null) {
            mIBinderCallback = callback;
            mContext = context;
            Intent service = new Intent();
            service.setAction("android.intent.action.ASSIST_SETTING");
            service.setPackage("cn.my.mytestapp");
            Log.d("VoiceAssistServiceManagerService", "VoiceAssistServiceManager bindService  ");
            context.bindService(service, mConnection, Context.BIND_AUTO_CREATE);
        }

    }

    private boolean checkPermission(String packageName) {
        List<String> allowPackages = new ArrayList<>();
        allowPackages.add("cn.my.mytestapp");
        allowPackages.add("com.my.clent");
        return allowPackages.contains(packageName);
    }

    public interface IBinderCallback {
        void onBindSuccess();
    }

    public void setChangeUserName(String name, String from) {
        if (mIVoiceAssistInterface == null) {
            Log.d("VoiceAssistServiceManagerService", "mIVoiceAssistInterface = null  name:" + name + " ,from:" + from);
        }
        if (mIVoiceAssistInterface != null) {
            try {
                Log.d("VoiceAssistServiceManagerService", "setChangeUserName  name:" + name + " ,from:" + from);
                mIVoiceAssistInterface.changeUserName(name, from);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUserName() {
        if (mIVoiceAssistInterface != null) {
            try {
                return mIVoiceAssistInterface.getUserName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public boolean isFristOpen() {
        if (mIVoiceAssistInterface != null) {
            try {
                return mIVoiceAssistInterface.isFirstOpen();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void setFristOpen(boolean fristOpen) {
        if (mIVoiceAssistInterface != null) {
            try {
                mIVoiceAssistInterface.setFirstOpen(fristOpen);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isShow() {
        if (mIVoiceAssistInterface != null) {
            try {
                return mIVoiceAssistInterface.isShow();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
