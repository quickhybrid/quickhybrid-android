package com.quick.jsbridge.view.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.quick.jsbridge.view.IQuickFragment;

import java.util.List;


/**
 * Created by dailichun on 2017/12/6.
 * webview加载页面生命周期接口
 */
public interface ILoadPage {

    /**
     * 获取fragment容器对象
     *
     * @return
     */
    IQuickFragment getFragment();

    /**
     * 获取选择文件的实现对象
     *
     * @return
     */
    IFileChooser getFileChooser();

    /**
     * 是否加载图片
     *
     * @param url
     * @return
     */
    boolean isLoadImage(String url);

    /**
     * 加载资源
     *
     * @param url
     * @return
     */
    WebResourceResponse shouldInterceptRequest(WebView view, String url);

    /**
     * 重定向或者跳转连接
     *
     * @param view
     * @param url
     * @return
     */
    void forwardUrl(WebView view, String url);

    /**
     * 页面加载完毕
     *
     * @param view
     * @param url
     */
    void onPageFinished(WebView view, String url);

    /**
     * 页面开始加载
     *
     * @param view
     * @param url
     * @param favicon
     */
    void onPageStarted(WebView view, String url, Bitmap favicon);

    /**
     * 获取标题
     *
     * @param url
     * @param title
     */
    void onReceivedTitle(String url, String title);

    /**
     * 加载进度
     *
     * @param view
     * @param newProgress
     */
    void onProgressChanged(WebView view, int newProgress);

    /**
     * 页面加载异常
     *
     * @param view
     * @param url
     * @param errorCode
     * @param errorDescription
     */
    void onReceivedError(WebView view, String url, int errorCode, String errorDescription);

    /**
     * 证书加载异常
     *
     * @param view
     * @param handler
     * @param error
     */
    void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);

    /**
     * 获取历史记录
     *
     * @return
     */
    List<String> getHistoryUrl();

    /**
     * 返回是否已初始化配置
     */
    boolean hasConfig();

    /**
     * 设置是否已初始化配置
     */
    void setHasConfig(boolean isConfig);
}
