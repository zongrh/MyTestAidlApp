package cn.my.mytestapp;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FileName: NameThreadFactory
 * Author: nanzong
 * Date: 2022/8/14 10:50 下午
 * Description:
 * History:
 */
public class NameThreadFactory implements ThreadFactory {
    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);
    private final AtomicInteger mThreadNum = new AtomicInteger(1);
    private final String mPrefix;
    private final boolean mDaemo;
    private final ThreadGroup mGroup;

    public NameThreadFactory(){
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }

    public NameThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NameThreadFactory(String prefix, boolean daemo) {
        mPrefix = prefix+"-thread-";
        mDaemo = daemo;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread thread = new Thread(mGroup, runnable, name, 0);
        thread.setDaemon(mDaemo);
        return thread;
    }
    public ThreadGroup getGroup(){
        return mGroup;
    }
}