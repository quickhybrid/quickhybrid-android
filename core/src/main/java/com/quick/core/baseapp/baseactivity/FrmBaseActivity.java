package com.quick.core.baseapp.baseactivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.quick.core.baseapp.baseactivity.control.PageControl;
import com.quick.core.ui.app.INbControl;
import com.quick.core.ui.app.IPageControl;

/**
 * Created by dailichun on 2017/12/6.
 * 基础Activity 继承android.app.Activity
 */

public class FrmBaseActivity extends Activity implements INbControl.INbOnClick {

    /**
     * 页面控制器
     */
    public IPageControl pageControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageControl = new PageControl(this, this);

        pageControl.initBaseView(LayoutInflater.from(this), null);
    }

    public void setLayout(int layoutId) {
        pageControl.setLayout(layoutId);
    }

    public void setLayout(View view) {
        pageControl.setLayout(view);
    }

    public void setTitle(String title) {
        pageControl.setTitle(title);
    }

    public INbControl.ViewHolder getNbViewHolder() {
        return pageControl.getNbBar().getViewHolder();
    }

    public void showLoading() {
        pageControl.showLoading();
    }

    public void hideLoading() {
        pageControl.hideLoading();
    }

    public void toast(String msg) {
        pageControl.toast(msg);
    }

    public Context getContext() {
        return pageControl.getContext();
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void onNbBack() {
        finish();
    }

    @Override
    public void onNbLeft(View view) {

    }

    @Override
    public void onNbRight(View view, int which) {

    }

    @Override
    public void onNbTitle(View view) {

    }

    @Override
    public void onNbSearch(String keyWord) {
        pageControl.getStatusPage().hideStatus();
    }

    @Override
    public void onNbSearchClear() {

    }

    @Override
    protected void onResume() {
        pageControl.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        pageControl.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //切换横竖屏不重新加载界面
    }
}
