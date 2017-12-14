package com.quick.core.ui.app;

import android.view.View;


/**
 * Created by dailichun on 2017/12/6.
 * 界面错误处理接口
 */

public interface IErrorControl {

    /**
     * 获取根布局
     *
     * @return
     */
    View getRootView();

    /**
     * 按照错误类型显示错误页面
     *
     * @param type
     */
    void showStatus(int type);

    /**
     * 指定错误图片和文字
     *
     * @param resid
     * @param info
     */
    void showStatus(int resid, String info);

    /**
     * 设置错误描述
     *
     * @param description
     */
    void setErrorDescription(String description);

    /**
     * 隐藏错误页面,并且展示正文内容
     */
    void hideStatus();

    /**
     * 设置点击按钮
     *
     * @param text
     * @param clickListener
     */
    void setClickButton(String text, View.OnClickListener clickListener);
}
