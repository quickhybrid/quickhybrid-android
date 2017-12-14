package com.quick.jsbridge.view.webview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import quick.com.jsbridge.BuildConfig;

/**
 * Created by dailichun on 2017/12/6.
 * 自定义webview
 */

public class QuickWebView extends WebView {
    public ProgressBar progressbar;

    private IWebviewScrollChanged mOnScrollChangedCallback;

    public QuickWebView(Context context) {
        super(context);
        settingWebView();
    }

    public QuickWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        settingWebView();
    }

    public QuickWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        settingWebView();
    }

    /**
     * 是否存在滚动条
     *
     * @return
     */
    public boolean existVerticalScrollbar() {
        return computeVerticalScrollRange() > computeVerticalScrollExtent();
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t, oldl, oldt);
        }
    }

    /**
     * 获取滚动条监听事件
     *
     * @return
     */
    public IWebviewScrollChanged getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    /**
     * 设置滚动监听
     */
    public void setOnScrollChangedCallback(
            final IWebviewScrollChanged onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }


    /**
     * 初始化设置
     */
    public void settingWebView() {
        WebSettings settings = getSettings();
        String ua = settings.getUserAgentString();
        // 设置浏览器UA,JS端通过UA判断是否属于Quick环境
        settings.setUserAgentString(ua + " QuickHybridJs/" + BuildConfig.VERSION_NAME);
        // 设置支持JS
        settings.setJavaScriptEnabled(true);
        // 设置是否支持meta标签来控制缩放
        settings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        settings.setLoadWithOverviewMode(true);
        // 设置内置的缩放控件（若SupportZoom为false，该设置项无效）
        settings.setBuiltInZoomControls(true);
        // 设置缓存模式
        // LOAD_DEFAULT 根据HTTP协议header中设置的cache-control属性来执行加载策略
        // LOAD_CACHE_ELSE_NETWORK 只要本地有无论是否过期都从本地获取
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        // 设置AppCache 需要H5页面配置manifest文件(官方已不推介使用)
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 强制开启android webview debug模式使用Chrome inspect(https://developers.google.com/web/tools/chrome-devtools/remote-debugging/)
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
    }
}
