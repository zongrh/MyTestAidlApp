package cn.my.mytestapp;

import android.app.Application;

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

    }
}