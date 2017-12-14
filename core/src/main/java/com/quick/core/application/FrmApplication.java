package com.quick.core.application;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.liulishuo.filedownloader.FileDownloader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dailichun on 2017/12/6.
 * 初始化application
 */
public class FrmApplication extends Application {

    private static final String LOGIN_CLASSNAME = "LOGIN_CLASSNAME";

    private static FrmApplication instance;

    private int mFinalCount;

    /**
     * app返回到后台的时间
     */
    private long lastActiveTime;

    /**
     * 登录页名字
     */
    private String loginClassName;

    /**
     * 是否在前台
     */
    private static boolean isActive = true;

    /**
     * 所有打开过的activity集合
     */
    public List<Activity> activityList = new ArrayList<>();

    /**
     * 当refreshToken过期后弹出对话框时为true，防止多次弹出对话框
     */
    public boolean isWaittingQuit = false;

    public static synchronized void initializeInstance(FrmApplication application) {
        if (instance == null) {
            instance = application;
        }
    }

    public static synchronized FrmApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Application未初始化");
        }
        return instance;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public boolean setActive() {
        return isActive = true;
    }

    public long getLastActiveTime() {
        return lastActiveTime;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInstance(this);

        //处理未捕获的异常
        CrashHandler.getInstance().init();

        //初始化ImageLoader
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        //初始化下载器
        FileDownloader.init(getApplicationContext());

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activityList.add(activity);

                // TODO: 可以做一些记录登陆状态等操作
            }


            @Override
            public void onActivityStarted(Activity activity) {
                mFinalCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                if (mFinalCount == 0) {
                    //从前台回到了后台
                    isActive = false;
                    lastActiveTime = System.currentTimeMillis();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activityList.remove(activity);
            }
        });


        // 可以对日志进行检查清空等
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


}
