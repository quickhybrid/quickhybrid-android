package com.quick.core.ui.widget.popmenu;


/**
 * Created by dailichun on 2017/12/6.
 * 弹出窗口状态变化监听器
 */
public interface PopChangedListener {
    void onShow(FrmPopMenu pop);

    void onHide(FrmPopMenu pop);
}
