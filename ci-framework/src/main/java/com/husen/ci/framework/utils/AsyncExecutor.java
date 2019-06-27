package com.husen.ci.framework.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/***
 @Author:MrHuang
 @Date: 2019/6/20 16:28
 @DESC: TODO
 @VERSION: 1.0


 Callable 和 Runnable
 相同点：
     两者都是接口；（废话）
     两者都可用来编写多线程程序；
     两者都需要调用Thread.start()启动线程；

 不同点：
     两者最大的不同点是：实现Callable接口的任务线程能返回执行结果；而实现Runnable接口的任务线程不能返回结果；
     Callable接口的call()方法允许抛出异常；而Runnable接口的run()方法的异常只能在内部消化，不能继续上抛；

 注意点：
    Callable接口支持返回执行结果，此时需要调用FutureTask.get()方法实现，此方法会阻塞主线程直到获取‘将来’结果；当不调用此方法时，主线程不会阻塞！
 ***/
public final class AsyncExecutor {

    /**
     * 核心线程数
     */
    private static final int CORE_SIZE = 30;

    /**
     * 最大线程数
     */
    private static final int MAX_SIZE = 200;

    /**
     * 空闲时间释放值
     */
    private static final int KEEP_SECONDS = 30;

    /**
     * 队列大小
     */
    private static final int QUEUE_SIZE = 5000;

    private static final ExecutorService EXECUTOR_SERVICE =
            new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_SECONDS, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(QUEUE_SIZE),
                    new ThreadFactoryBuilder().setNameFormat("AsyncExecutor-%s").build());


    /**
     * Runnable不能出现异常 必须try catch
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    /**
     * Callable可以出现异常
     * @param callable
     * @param <T>
     * @return
     */
    public static <T> Future<T> submit(Callable<T> callable) {
       return EXECUTOR_SERVICE.submit(callable);
    }

    public static void main(String[] args) {
        for( int i = 0 ; i < 10; i++) {
            final int it = i;
            AsyncExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + it);
            });
        }
    }
}
