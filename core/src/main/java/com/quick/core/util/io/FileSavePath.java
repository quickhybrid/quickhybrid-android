package com.quick.core.util.io;

import android.os.Environment;
import android.text.TextUtils;

import com.quick.core.util.app.AppUtil;
import com.quick.core.util.common.RuntimeUtil;

/**
 * Created by dailichun on 2017/12/6.
 * 文件保存路径
 */

public class FileSavePath {
    /**
     * 获取应用根文件夹
     *
     * @return
     */
    public static String getStoragePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/" + RuntimeUtil.getPackageName(AppUtil.getApplicationContext()) + "/";
    }

    /**
     * 日志文件夹
     *
     * @return
     */
    public static String getLogFolder() {
        return getStoragePath() + "Log/";
    }

    /**
     * 获取用户的根文件夹
     *
     * @return
     */
    public static String getUserFolder() {
        String userRole = "";

        // TODO: 可以根据登陆用户获取不同路径，这里暂时预留

        if (TextUtils.isEmpty(userRole)) {
            userRole = "Vistor";
        }
        return getStoragePath() + userRole + "/";
    }

    /**
     * 下载中的临时文件存放文件夹
     *
     * @return
     */
    public static String getTempFolder() {
        return getUserFolder() + "Temp/";
    }

    /**
     * 附件下载的文件夹
     *
     * @param type 类型
     * @return
     */
    public static String getAttachFolder(String type) {
        return getUserFolder() + (TextUtils.isEmpty(type) ? "" : type + "/") + "Attach/";
    }

    /**
     * 附件下载的文件夹
     *
     * @return
     */
    public static String getAttachFolder() {
        return getAttachFolder("");
    }

    /**
     * 升级安装包下载的文件夹
     *
     * @return
     */
    public static String getUpgradeFolder() {
        return getUserFolder() + "Upgrade/";
    }
}
