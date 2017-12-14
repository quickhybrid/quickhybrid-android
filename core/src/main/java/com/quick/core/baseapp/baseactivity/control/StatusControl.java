package com.quick.core.baseapp.baseactivity.control;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.quick.core.ui.app.IErrorControl;
import com.quick.core.ui.app.IPageControl;
import com.quick.core.ui.widget.DrawableText;

import quick.com.core.R;


/**
 * Created by dailichun on 2017/12/8.
 * 页面异常状态视图
 */
public class StatusControl implements IErrorControl {

    //网络异常
    public static final int STATUS_NET_ERROR = 0;

    //服务器异常
    public static final int STATUS_SERVER_ERROR = 1;

    //访问超时
    public static final int STATUS_TIMEOUT_ERROR = 2;

    //页面加载失败
    public static final int STATUS_PAGE_ERROR = 3;

    public View statusItem;

    public ImageView ivStatus;

    public TextView tvStatus;

    public TextView btnError;

    public DrawableText btnRefresh;

    public View rootView;

    public ScrollView sv;

    public TextView tvError;

    public IPageControl pageControl;

    public LinearLayout llBg;

    private View.OnClickListener refreshListener;

    public StatusControl(IPageControl pageControl) {
        this.pageControl = pageControl;

        initView();
    }

    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(pageControl.getContext());
        this.rootView = inflater.inflate(R.layout.frm_status, null);

        statusItem = rootView.findViewById(R.id.statusItem);

        sv = (ScrollView) rootView.findViewById(R.id.sv);

        llBg = (LinearLayout) rootView.findViewById(R.id.ll_bg);

        ivStatus = (ImageView) rootView.findViewById(R.id.ivStatus);

        tvStatus = (TextView) rootView.findViewById(R.id.tvStatus);

        tvError = (TextView) rootView.findViewById(R.id.tv_error);

        btnRefresh = (DrawableText) rootView.findViewById(R.id.btnRefresh);
        btnRefresh.setClickAnimation(true);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStatus();
                if (refreshListener != null) {
                    refreshListener.onClick(v);
                }
            }
        });

        btnError = (TextView) rootView.findViewById(R.id.btn_error);
        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvError.getText().toString().isEmpty()) {
                    if (btnError.getText().toString().equals(pageControl.getContext().getString(R.string.status_show_error))) {
                        sv.setVisibility(View.VISIBLE);
                        ivStatus.setVisibility(View.GONE);
                        tvStatus.setVisibility(View.GONE);
                        btnError.setText(pageControl.getContext().getString(R.string.status_hide_error));
                    } else {
                        sv.setVisibility(View.GONE);
                        ivStatus.setVisibility(View.VISIBLE);
                        tvStatus.setVisibility(View.VISIBLE);
                        btnError.setText(pageControl.getContext().getString(R.string.status_show_error));
                    }
                }
            }
        });


    }

    public void setStatusIcon(int resid) {
        ivStatus.setImageResource(resid);
    }

    public void setStatusInfo(CharSequence info) {
        tvStatus.setText(info);
    }

    public ImageView getIvStatus() {
        return ivStatus;
    }

    public TextView getTvStatus() {
        return tvStatus;
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void showStatus(int type) {
        int resid = R.mipmap.img_net_none_bg;
        String info = pageControl.getContext().getString(R.string.status_page_error);
        switch (type) {
            case STATUS_NET_ERROR:
                resid = R.mipmap.img_net_none_bg;
                info = pageControl.getContext().getString(R.string.status_network_error);
                break;
            case STATUS_SERVER_ERROR:
                resid = R.mipmap.img_server_wrong_bg;
                info = pageControl.getContext().getString(R.string.status_server_error);
                break;
            case STATUS_TIMEOUT_ERROR:
                resid = R.mipmap.img_net_wrong_bg;
                info = pageControl.getContext().getString(R.string.status_server_timeout);
                break;
            case STATUS_PAGE_ERROR:
                resid = R.mipmap.img_net_none_bg;
                info = pageControl.getContext().getString(R.string.status_page_error);
                break;
        }
        showStatus(resid, info);
    }

    @Override
    public void showStatus(int resid, String info) {
        setStatusIcon(resid);
        setStatusInfo(info);
        pageControl.getBaseContent().setVisibility(View.GONE);
        statusItem.setVisibility(View.VISIBLE);
    }

    @Override
    public void setErrorDescription(String description) {
        tvError.setText(description);
        btnError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideStatus() {
        statusItem.setVisibility(View.GONE);
        pageControl.getBaseContent().setVisibility(View.VISIBLE);
    }

    @Override
    public void setClickButton(String text, View.OnClickListener clickListener) {
        btnRefresh.setText(text);
        btnRefresh.setVisibility(View.VISIBLE);
        this.refreshListener = clickListener;
    }
}
