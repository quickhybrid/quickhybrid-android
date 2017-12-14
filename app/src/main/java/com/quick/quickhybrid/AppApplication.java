package com.quick.quickhybrid;


import com.quick.core.application.FrmApplication;
import com.quick.core.baseapp.theme.BaseThemeControl;


public class AppApplication extends FrmApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.BUILD_TYPE.equals("release")) {
            // 可以防止二次打包

        } else {

            //保存所有log日志，调试时可开启，正式包不允许开启
            // LogUtil.autoLog();

            //开启ButterKnife的日志输出
//            ButterKnife.setDebug(true);

            //是否开启下载日志
//            FileDownloadLog.NEED_LOG = true;


        }

        //初始化默认主题
        BaseThemeControl.getInstance().initTheme(BaseThemeControl.getDefaultThemes());

    }

}
