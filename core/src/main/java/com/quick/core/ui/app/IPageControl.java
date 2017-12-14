package com.quick.core.ui.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dailichun on 2017/12/6.
 * 基础界面接口,定义了页面的基本操作，基础Activity或者Fragment必须实现该类
 */

public interface IPageControl {

    /**
     * 设置横竖屏
     */
    void setOrientation();

    /**
     * 初始化根布局,包括导航栏，正文，状态栏等
     */
    View initBaseView(LayoutInflater inflater, ViewGroup container);

    /**
     * 显示页面加载进度条
     */
    void showLoading();

    /**
     * 显示页面加载进度条
     */
    void showLoading(String message);

    /**
     * 隐藏页面加载进度条
     */
    void hideLoading();

    /**
     * 显示提示文字
     */
    void toast(String msg);


    /**
     * 设置正文布局
     *
     * @param layoutId
     */
    void setLayout(int layoutId);

    /**
     * 设置正文布局
     *
     * @param view
     */
    void setLayout(View view);

    /**
     * 设置标题
     *
     * @param title
     */
    void setTitle(String title);

    /**
     * 设置标题
     *
     * @param title
     */
    void setTitle(String[] title);

    /**
     * 获取导航栏点击事件
     *
     * @return
     */
    INbControl.INbOnClick getNbOnClick();

    /**
     * 获取上下文
     *
     * @return
     */
    Context getContext();

    /**
     * 获取Activity对象
     *
     * @return
     */
    Activity getActivity();

    /**
     * 获取Fragment对象
     *
     * @return
     */
    Object getFragment();


    /**
     * 获取界面根布局
     *
     * @return
     */
    View getRootView();

    /**
     * 获取界面正文根布局
     *
     * @return
     */
    View getBaseContent();

    /**
     * 根据id查找根布局中的控件
     *
     * @param id
     * @return
     */
    View findViewById(int id);

    /**
     * 获取导航栏控制器
     *
     * @return
     */
    INbControl getNbBar();

    /**
     * 设置自定义导航栏
     *
     * @return
     */
    void setNbBar(INbControl nbBar);

    /**
     * 获取界面异常状态视图
     *
     * @return
     */
    IErrorControl getStatusPage();

    /**
     * 设置自定义界面异常状态视图,需要自己addview
     *
     * @return
     */
    void setStatusPage(IErrorControl statusControl);

    /**
     * Activity或者Fragment的onDestroy
     */
    void onDestroy();

    /**
     * Activity或者Fragment的onResume
     */
    void onResume();

    /**
     * Fragment的onDestroyView,相当于Activity的onStop
     */
    void onDestroyFragmentView();

    /**
     * 是否为Fragment界面
     *
     * @return
     */
    boolean isFragment();
}
