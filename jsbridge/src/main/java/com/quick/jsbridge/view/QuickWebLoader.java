package com.quick.jsbridge.view;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import com.quick.core.baseapp.baseactivity.FrmBaseActivity;
import com.quick.jsbridge.bean.QuickBean;
import com.quick.jsbridge.control.AutoCallbackDefined;
import com.quick.jsbridge.control.WebloaderControl;


import quick.com.jsbridge.R;


/**
 * Created by dailichun on 2017/12/7.
 * 如果需要自定义quick容器请继承QuickWebLoader，在布局文件中必须定义QuickFragment的容器FrameLayout控件
 */
public class QuickWebLoader extends FrmBaseActivity {

    public QuickFragment fragment;

    public QuickBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏Activity的导航栏
        pageControl.getNbBar().hide();

        initQuickBean(savedInstanceState);

        setLayout(R.layout.quick_activity);

        addFragment(R.id.frgContent);

    }

    /**
     * 初始化页面参数
     *
     * @param savedInstanceState
     */
    public void initQuickBean(Bundle savedInstanceState) {
        if (null != savedInstanceState && savedInstanceState.containsKey("bean")) {
            bean = (QuickBean) savedInstanceState.getSerializable("bean");
        } else if (getIntent().hasExtra("bean")) {
            bean = (QuickBean) getIntent().getSerializableExtra("bean");
        }

        if (bean == null) {
            toast(getString(R.string.status_data_error));
            finish();
            return;
        }
    }

    /**
     * 添加QuickFragment容器
     *
     * @param containerViewId FrameLayout的视图id
     */
    public void addFragment(int containerViewId) {
        //初始化页面
        fragment = QuickFragment.newInstance(bean);
        getFragmentManager().beginTransaction().add(containerViewId, fragment).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (bean != null) {
            outState.putSerializable("bean", bean);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("bean")) {
            bean = (QuickBean) savedInstanceState.getSerializable("bean");
        }
    }

    @Override
    public void onBackPressed() {
        WebloaderControl control = fragment.getWebloaderControl();
        if (control != null) {
            if (control.autoCallbackEvent.isRegist(AutoCallbackDefined.OnClickBack)) {
                control.autoCallbackEvent.onClickBack();
            } else {
                control.loadLastPage(false);
            }
        } else {
            super.onNbBack();
        }
    }


    public static void go(Context context, QuickBean bean) {
        Intent intent = new Intent(context, QuickWebLoader.class);
        intent.putExtra("bean", bean);
        context.startActivity(intent);
    }

    public static void go(Context context, String url) {
        go(context, new QuickBean(url));
    }
}
