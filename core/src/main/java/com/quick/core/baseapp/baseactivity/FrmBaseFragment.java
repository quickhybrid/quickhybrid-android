package com.quick.core.baseapp.baseactivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quick.core.baseapp.baseactivity.control.PageControl;
import com.quick.core.ui.app.IErrorControl;
import com.quick.core.ui.app.INbControl;
import com.quick.core.ui.app.IPageControl;


/**
 * Created by dailichun on 2017/12/8.
 * 基础Fragment 继承android.app.Fragment
 */
public class FrmBaseFragment extends Fragment implements INbControl.INbOnClick {

    /**
     * 页面控制器
     */
    public IPageControl pageControl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pageControl = new PageControl(this, this);

        return pageControl.initBaseView(inflater, container);
    }

    public void setLayout(int layoutId) {
        pageControl.setLayout(layoutId);
    }

    public void setLayout(View view) {
        pageControl.setLayout(view);
    }

    public void setTitle(String title){
        pageControl.setTitle(title);
    }

    public INbControl.ViewHolder getNbViewHolder(){
        return pageControl.getNbBar().getViewHolder();
    }

    public IErrorControl getStatusItem(){
       return pageControl.getStatusPage();
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

    public View findViewById(int id) {
        return pageControl.findViewById(id);
    }

    @Override
    public void onResume() {
        super.onResume();
        pageControl.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pageControl.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pageControl.onDestroyFragmentView();
    }

    @Override
    public void onNbBack() {
        getActivity().finish();
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

}
