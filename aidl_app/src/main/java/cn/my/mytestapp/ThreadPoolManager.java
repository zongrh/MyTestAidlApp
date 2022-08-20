package cn.my.mytestapp;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * FileName: ThreadPoolManager
 * Author: nanzong
 * Date: 2022/8/14 10:32 下午
 * Description:
 * History:
 */
public class ThreadPoolManager {
    private ThreadPoolExecutor threadPool;
    private ArrayBlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<Runnable>(10);
    /**
     * 核心线程数
     */
    private static int corePoolSize = 1 * 2;
    /**
     * 最大线程个数
     */
    private static int maximumPoolSize = 1 * 5;
    /**
     * 保持心跳时间
     */
    private static int keepAliveTime = 1;
    /**
     * 定时执行线程个数
     */
    private final static int minSchedule = 2;
    /**
     * 延时执行线程
     */
    private ScheduledExecutorService appSchedule;
    /**
     * 主线程执行句柄
     */
    private Handler mainHander = new Handler(Looper.getMainLooper());

    /**
     * 线程池构造方法
     */
    private ThreadPoolManager() {
        RejectedExecutionHandler mHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                if (threadPoolExecutor.isShutdown()) {
                    taskQueue.offer(runnable);
                }
            }
        };
        Runnable command = new Runnable() {
            @Override
            public void run() {
                Runnable task = null;
                try {
                    task = taskQueue.take(); // 使用具备阻塞特性的方法
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                threadPool.execute(task);
            }
        };
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        // 每一次执行终止和下一次执行开始之间都会存在给定的延迟 16毫秒
        scheduledExecutorService.scheduleWithFixedDelay(command, 0L, 16L, TimeUnit.MILLISECONDS);
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5),
                new NameThreadFactory("CoreImplServiceHandler"), mHandler) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                printExecption(r, t);
            }
        };
        appSchedule = Executors.newScheduledThreadPool(minSchedule);
    }

    private static class SingletonHolder {
        private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
    }

    public static ThreadPoolManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void printExecption(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException cancellationException) {
                t = cancellationException;
            } catch (ExecutionException executionException) {
                t = executionException;
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            t.printStackTrace();
        }
    }

    public int getActiveCont() {
        return threadPool.getActiveCount();
    }

    public int getCorePoolCont() {
        return corePoolSize;
    }

    /**
     * 任务添加到线程池
     *
     * @param runnable
     * @return
     */
    public Future<?> addExecuteTask(Runnable runnable) {
        if (runnable == null) {
            return null;
        }
        return this.threadPool.submit(runnable);
    }

    public Future<?> addExecuteTask(Callable<?> callable) {
        if (callable == null) {
            return null;
        }
        return this.threadPool.submit(callable);
    }

    /**
     * 延时任务添加到线程池
     *
     * @param task
     * @param delayMills
     */
    public void addDelayExecuteTask(Runnable task, long delayMills) {
        appSchedule.schedule(task, delayMills, TimeUnit.MILLISECONDS);
    }

    /**
     * 添加主线程任务，谨慎使用，并非同步加入到主线程
     *
     * @param task
     */
    public void addMainTask(Runnable task) {
        mainHander.post(task);
    }

    /**
     * 添加主线程延时任务
     *
     * @param task
     * @param delayMills
     */
    public void addMainDelayTask(Runnable task, long delayMills) {
        mainHander.postDelayed(task, delayMills);
    }


    /**
     * 移除主线程延时任务
     *
     * @param task
     */
    public void removeMainDelayTask(Runnable task) {
        mainHander.removeCallbacks(task);
    }

    /**
     * 结束线程池中的延时任务
     */
    public void shutDownDelayTask() {
        for (Runnable task : taskQueue) {
            taskQueue.remove(task);
            threadPool.getQueue().remove();
        }
    }

    /**
     * 不建议使用，有概率无法清除成功
     * 清除指定的延时线程
     *
     * @param runnable
     */
    @Deprecated
    public void shutDownDelayTask(Runnable runnable) {
        taskQueue.remove(runnable);
        threadPool.getQueue().remove(runnable);
        threadPool.remove(runnable);
    }

    /**
     * 按指定频率间隔执行某个任务
     * 以固定延迟时间进行执行，本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
     * https://blog.csdn.net/tsyj810883979/article/details/8481621
     * @param task
     * @param initDelay
     * @param delay
     */
    public void addPeriodDelayExecuteTask(Runnable task, long initDelay, long delay) {
        this.appSchedule.scheduleWithFixedDelay(task, initDelay, delay, TimeUnit.MILLISECONDS);
    }

    public boolean isAsyncBySimple() {
        return getActiveCont() < getCorePoolCont();
    }
} 