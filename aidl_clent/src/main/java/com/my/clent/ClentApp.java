package com.my.clent;

import android.app.Application;

import cn.my.hmiadapter.VoiceAssistServiceManager;

/**
 * FileName: ClentApp
 * Author: nanzong
 * Date: 2022/8/20 12:45 下午
 * Description:
 * History:
 */
public class ClentApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VoiceAssistServiceManager.getInstanch().bind(this, null);
    }
}