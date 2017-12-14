package com.quick.jsbridge.view.webview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by dailichun on 2017/12/6.
 * 自定义WebViewClient
 */

public class QuickWebviewClient extends WebViewClient {
    private ILoadPage loadPage;

    public QuickWebviewClient(ILoadPage loadPage) {
        this.loadPage = loadPage;
    }

    /**
     * 加载资源
     * android5.0以下支持，注意：5.0+系统也会执行该方法
     *
     * @param view
     * @param url
     * @return
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return super.shouldInterceptRequest(view, url);
        } else {
            return loadPage.shouldInterceptRequest(view, url);
        }
    }

    /**
     * 加载资源
     * android5.0+支持
     *
     * @param view
     * @param request
     * @return
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        WebResourceResponse resourceResponse = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            resourceResponse = loadPage.shouldInterceptRequest(view, request.getUrl().toString());
        }
        if (resourceResponse == null) {
            return super.shouldInterceptRequest(view, request);
        } else {
            return resourceResponse;
        }
    }

    /**
     * 重定向或者A标签
     * 7.0系统以下支持
     *
     * @param view
     * @param url
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        loadPage.forwardUrl(view, url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    /**
     * 重定向或者A标签
     * 7.0+系统支持
     *
     * @param view
     * @param request
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String url = request.getUrl().toString();
            if (request.isRedirect()) {
                loadPage.forwardUrl(view, url);
            }
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    /**
     * 页面开始加载
     *
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        loadPage.onPageStarted(view, url, favicon);
    }

    /**
     * 页面加载完毕
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        loadPage.onPageFinished(view, url);
    }

    /**
     * 页面报错
     * android6.0+支持
     *
     * @param view
     * @param request
     * @param error
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        loadPage.onReceivedError(view, request.getUrl().toString(), error.getErrorCode(), error.getDescription().toString());
    }

    /**
     * 页面报错
     * 兼容android6.0系统以下
     *
     * @param view
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        loadPage.onReceivedError(view, failingUrl, errorCode, description);
    }

    /**
     * 页面加载资源时报错，但对页面没有什么影响，例如favour.icon not found异常
     * android5.0+系统支持
     *
     * @param view
     * @param request
     * @param errorResponse
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        loadPage.onReceivedError(view, request.getUrl().toString(), errorResponse.getStatusCode(), errorResponse.getReasonPhrase());
    }

    /**
     * 加载SSL证书异常，一般在请求https页面时捕获
     *
     * @param view
     * @param handler
     * @param error
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        loadPage.onReceivedSslError(view, handler, error);
    }

    /**
     * 更新页面访问历史记录
     *
     * @param view
     * @param url
     * @param isReload
     */
    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
    }
}
