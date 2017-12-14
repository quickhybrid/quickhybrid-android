package com.quick.core.application;


/**
 * Created by dailichun on 2017/12/6.
 * 全局异常处理
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler mCrashHandler;

    public static CrashHandler getInstance() {
        if (mCrashHandler == null) {
            synchronized (CrashHandler.class) {
                if (mCrashHandler == null) {
                    mCrashHandler = new CrashHandler();
                }
            }
        }
        return mCrashHandler;
    }

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void handlerException(final Throwable ex) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO: 保存日志，这里预留

                // 退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                // 关闭虚拟机，彻底释放内存空间
                System.exit(0);
            }

        }).start();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        handlerException(e);
    }
}
