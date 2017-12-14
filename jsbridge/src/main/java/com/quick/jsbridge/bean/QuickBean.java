package com.quick.jsbridge.bean;

import android.content.pm.ActivityInfo;

import java.io.Serializable;

/**
 * Created by dailichun on 2017/12/6.
 */

public class QuickBean implements Serializable {
    /**
     * H5页面url地址
     */
    public String pageUrl = "about:blank";

    /**
     * 页面类型
     * -1:不加载导航栏
     * 1：默认类型
     * 2：搜索导航栏
     */
    public int pageStyle = 1;

    /**
     * 横竖屏{@link android.content.pm.ActivityInfo}
     * 0：横屏
     * 1：竖屏（默认）
     * 2:跟随用户设置
     */
    public int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public QuickBean() {

    }

    public QuickBean(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public QuickBean copy() {
        QuickBean model = new QuickBean();

        model.orientation = this.orientation;
        model.pageStyle = this.pageStyle;
        model.pageUrl = this.pageUrl;

        return model;
    }
}
