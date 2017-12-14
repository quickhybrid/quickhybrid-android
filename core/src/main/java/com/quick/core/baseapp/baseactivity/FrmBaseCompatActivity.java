package com.quick.core.baseapp.baseactivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.quick.core.baseapp.baseactivity.control.PageControl;
import com.quick.core.ui.app.INbControl;
import com.quick.core.ui.app.IPageControl;


/**
 * Created by dailichun on 2017/12/8.
 * 基础Activity 继承android.support.v7.app.AppCompatActivity
 */
public class FrmBaseCompatActivity extends AppCompatActivity implements INbControl.INbOnClick {

    /**
     * 页面控制器
     */
    public IPageControl pageControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉自带的actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

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

    }

    @Override
    public void onNbSearchClear() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        pageControl.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageControl.onDestroy();
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
