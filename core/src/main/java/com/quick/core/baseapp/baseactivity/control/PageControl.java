package com.quick.core.baseapp.baseactivity.control;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.quick.core.baseapp.baseactivity.FrmBaseActivity;
import com.quick.core.baseapp.baseactivity.FrmBaseFragment;
import com.quick.core.ui.app.IErrorControl;
import com.quick.core.ui.app.INbControl;
import com.quick.core.ui.app.IPageControl;
import com.quick.core.ui.widget.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import quick.com.core.R;

/**
 * Created by dailichun on 2017/12/6.
 * {@link FrmBaseActivity} 和{@link FrmBaseFragment} 界面的具体实现。框架核心控制器。
 */
public class PageControl implements IPageControl {

    public static final String SCREEN_ORIENTATION = "orientation";

    public static final String PAGE_STYLE = "pageStyle";

    public static final String PAGE_TITLE = "pageTitle";

    /**
     * 不加载导航栏
     */
    public static final int NBBAR_STYLE_NONE = -1;

    /**
     * 默认导航栏
     */
    public static final int NBBAR_STYLE_DEFAULT = 1;

    /**
     * 搜索导航栏
     */
    public static final int NBBAR_STYLE_SEARCH = 2;

    /**
     * 自定义布局根视图最外层控件
     */
    private LinearLayout rootLayout;

    /**
     * 自定义布局根视图
     */
    private View rootView;

    /**
     * 基础导航栏
     */
    private INbControl nbBar;

    /**
     * 自定义布局容器
     */
    private FrameLayout baseContent;

    /**
     * 异常状态视图
     */
    private IErrorControl statusControl;

    /**
     * 加载对话框
     */
    private ProgressDialog mProgressHUD;


    /**
     * 注解解绑对象
     */
    private Unbinder unbinder;

    private android.app.Fragment fragment;

    private android.support.v4.app.Fragment v4Fragment;

    private Activity activity;

    private INbControl.INbOnClick nbOnClick;

    private Bundle bundle;

    public PageControl(Activity activity, INbControl.INbOnClick nbOnClick) {
        this.activity = activity;
        this.nbOnClick = nbOnClick;
        this.bundle = activity.getIntent().getExtras();
        setOrientation();
    }

    public PageControl(android.app.Fragment fragment, INbControl.INbOnClick nbOnClick) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        this.nbOnClick = nbOnClick;
        this.bundle = fragment.getArguments();
    }

    public PageControl(android.support.v4.app.Fragment fragment, INbControl.INbOnClick nbOnClick) {
        this.v4Fragment = fragment;
        this.activity = fragment.getActivity();
        this.nbOnClick = nbOnClick;
        this.bundle = fragment.getArguments();
    }

    @Override
    public void setOrientation() {
        //默认竖屏
        int orientation = getActivity().getIntent().getIntExtra(SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (orientation > -2 && orientation < 15)
            getActivity().setRequestedOrientation(orientation);
    }

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        //初始化布局

        rootView = inflater.inflate(R.layout.frm_base, null);

        //最外层根布局
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);

        //初始化导航栏
        initNbBar();

        //初始化正文布局
        baseContent = (FrameLayout) findViewById(R.id.baseContent);

        if (!isFragment()) {
            //Activity需要在这边设置整个页面布局
            activity.setContentView(rootView);
        }

        // 为了与Quick参数保持一致改为了pageTitle
        if (bundle != null) {
            setTitle(bundle.getString(PAGE_TITLE));
        }

        //初始化异常状态页
        initStatuPage();

        return rootView;
    }

    @Override
    public void showLoading() {
        showLoading("");
    }

    @Override
    public void showLoading(String message) {
        if (mProgressHUD == null) {
            mProgressHUD = new ProgressDialog(getActivity(), R.style.AlertDialogCustom);
        }
        if (mProgressHUD.isShowing()){
            mProgressHUD.dismiss();
        }
        mProgressHUD.setMessage(TextUtils.isEmpty(message) ? activity.getString(R.string.loading) : message);
        mProgressHUD.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressHUD != null && mProgressHUD.isShowing()) {
            mProgressHUD.dismiss();
        }
    }

    @Override
    public void toast(String msg) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ToastUtil.toastShort(getActivity(), msg);
        }
    }



    @Override
    public void setLayout(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(layoutId, null);
        setLayout(view);
    }

    @Override
    public void setLayout(View view) {
        getBaseContent().addView(view);
        if (isFragment()) {
            //支持注解绑定
            unbinder = ButterKnife.bind(getFragment(), view);
        } else {
            //支持注解绑定,必须在setContentView之后执行
            ButterKnife.bind(activity);
        }
    }

    @Override
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title) && nbBar != null) {
            nbBar.setNbTitle(title);
        }
    }

    @Override
    public void setTitle(String[] title) {

    }

    @Override
    public INbControl.INbOnClick getNbOnClick() {
        return nbOnClick;
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public Object getFragment() {
        return fragment == null ? v4Fragment : fragment;
    }


    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public FrameLayout getBaseContent() {
        return baseContent;
    }

    @Override
    public View findViewById(int id) {
        return rootView.findViewById(id);
    }

    @Override
    public INbControl getNbBar() {
        return nbBar;
    }

    @Override
    public void setNbBar(INbControl nbBar) {
        if (this.nbBar != null && rootLayout.getChildAt(0) == this.nbBar.getRootView()) {
            rootLayout.removeViewAt(0);
        }
        this.nbBar = nbBar;
        rootLayout.addView(nbBar.getRootView(), 0);
        if (bundle != null) {
            setTitle(bundle.getString(PAGE_TITLE));
        }
    }

    @Override
    public IErrorControl getStatusPage() {
        return statusControl;
    }

    @Override
    public void setStatusPage(IErrorControl statusControl) {
        for (int i = 0; i < rootLayout.getChildCount(); i++) {
            if (rootLayout.getChildAt(i) == this.statusControl.getRootView()) {
                rootLayout.removeView(this.statusControl.getRootView());
            }
        }
        this.statusControl = statusControl;
    }

    @Override
    public void onDestroy() {
        // 可以取消一些事件
    }

    @Override
    public void onResume() {
        // 可以用来检查一些权限
    }

    @Override
    public void onDestroyFragmentView() {
        if (unbinder != null) {
            //解绑注解
            unbinder.unbind();
        }
    }

    /**
     * 不管是否为Fragment，Activity都是不为空的
     *
     * @return
     */
    @Override
    public boolean isFragment() {
        return fragment != null || v4Fragment != null;
    }

    /**
     * 根据导航栏类型初始化导航栏
     *
     * @return
     */
    private void initNbBar() {
        int nbStyle = NBBAR_STYLE_DEFAULT;
        if (bundle != null) {
            nbStyle = bundle.getInt(PAGE_STYLE, NBBAR_STYLE_DEFAULT);
        }
        if (nbStyle == NBBAR_STYLE_DEFAULT) {
            nbBar = new NbControl(getContext(), nbOnClick);
        }
        if (nbBar != null) {
            rootLayout.addView(nbBar.getRootView(), 0);
        }
    }

    /**
     * 初始化异常状态页面
     *
     * @return
     */
    private void initStatuPage() {
        statusControl = new StatusControl(this);
        rootLayout.addView(statusControl.getRootView());
    }
}
