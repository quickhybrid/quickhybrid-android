package com.quick.jsbridge.view;

import android.widget.ProgressBar;

import com.quick.core.ui.app.IPageControl;
import com.quick.jsbridge.bean.QuickBean;
import com.quick.jsbridge.control.WebloaderControl;
import com.quick.jsbridge.view.webview.QuickWebView;

/**
 * Created by dailichun on 2017/12/6.
 * quick的fragment容器，如果要加载H5页面请使用{@link }
 */

public interface IQuickFragment {
    /**
     * 获取框架页面控制器
     *
     * @return
     */
    IPageControl getPageControl();

    /**
     * 获取Quick容器控制器
     *
     * @return
     */
    WebloaderControl getWebloaderControl();
    /**
     * 获取webview对象
     *
     * @return
     */
    QuickWebView getQuickWebView();

    /**
     * 获取页面参数
     *
     * @return
     */
    QuickBean getQuickBean();

    /**
     * 获取进度条
     *
     * @return
     */
    ProgressBar getProgressBar();

    /**
     * 设置页面参数
     *
     * @return
     */
    void setQuickBean(QuickBean bean);

}
