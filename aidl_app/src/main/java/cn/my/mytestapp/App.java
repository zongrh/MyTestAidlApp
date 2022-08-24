package cn.my.mytestapp;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import cn.my.hmiadapter.VoiceAssistServiceManager;

/**
 * FileName: App
 * Author: nanzong
 * Date: 2022/8/20 11:49 上午
 * Description:
 * History:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 启动服务
        VoiceAssistServiceManager.getInstanch().bind(this, null);
        boolean isRunning = isRunning(this, "cn.my.mytestapp.hmisetting.VoiceAssistSettingService");
        Log.e("VoiceAssistServiceManager", "isRunning: " + isRunning);
    }

    public boolean isRunning(Context c, String serviceName) {
        ActivityManager myAM = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);

        ArrayList<ActivityManager.RunningServiceInfo> runningServices = (ArrayList<ActivityManager.RunningServiceInfo>) myAM.getRunningServices(40);
        //获取最多40个当前正在运行的服务，放进ArrList里,以现在手机的处理能力，要是超过40个服务，估计已经卡死，所以不用考虑超过40个该怎么办
        for (int i = 0; i < runningServices.size(); i++)//循环枚举对比
        {
            if (runningServices.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}