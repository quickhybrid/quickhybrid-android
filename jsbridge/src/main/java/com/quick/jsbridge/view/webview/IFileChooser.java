package com.quick.jsbridge.view.webview;

import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by dailichun on 2017/12/6.
 * h5中input=file控件选择文件接口
 */
public interface IFileChooser {

    void showFileChooser(ValueCallback uploadMsg, String acceptType);

    void showFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture);

    void showFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);

    /**
     * 文件选择后的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void onChooseFileResult(int requestCode, int resultCode, Intent data);
}
