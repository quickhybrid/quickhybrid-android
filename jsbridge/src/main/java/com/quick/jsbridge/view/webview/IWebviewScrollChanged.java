package com.quick.jsbridge.view.webview;

/**
 * Created by dailichun on 2017/12/6.
 * webview滚动监听接口
 */
public interface IWebviewScrollChanged {

    void onScroll(final int l, final int t, final int oldl,
                  final int oldt);
}
