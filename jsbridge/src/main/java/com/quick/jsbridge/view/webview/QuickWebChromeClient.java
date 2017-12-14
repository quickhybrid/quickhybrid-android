package com.quick.jsbridge.view.webview;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.quick.jsbridge.bridge.JSBridge;

/**
 * Created by dailichun on 2017/12/6.
 * 自定义WebChromeClient, 处理H5的file控件以及接收QUICK协议等
 */

public class QuickWebChromeClient extends WebChromeClient {
    private ILoadPage loadPage;

    public QuickWebChromeClient(ILoadPage loadPage) {
        this.loadPage = loadPage;
    }

    /**
     * Android 3.0+适用
     *
     * @param uploadMsg
     * @param acceptType
     */
    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        loadPage.getFileChooser().showFileChooser(uploadMsg, acceptType);
    }

    /**
     * Android 4.1+适用
     *
     * @param uploadMsg
     * @param acceptType
     * @param capture
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        loadPage.getFileChooser().showFileChooser(uploadMsg, acceptType, capture);
    }

    /**
     * Android 5.0+适用
     *
     * @param webView
     * @param filePathCallback
     * @param fileChooserParams
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        loadPage.getFileChooser().showFileChooser(webView, filePathCallback, fileChooserParams);
        return true;
    }

    /**
     * 监听页面加载进度条
     *
     * @param view
     * @param newProgress
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        loadPage.onProgressChanged(view, newProgress);
    }

    /**
     * 请求定位
     *
     * @param origin
     * @param callback
     */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        loadPage.onReceivedTitle(view.getUrl(),title);
    }

    /**
     * JSBridge协议入口
     *
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param result
     * @return
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        result.confirm(JSBridge.callJava(loadPage.getFragment(), message,loadPage.hasConfig()));
        return true;
    }
}
