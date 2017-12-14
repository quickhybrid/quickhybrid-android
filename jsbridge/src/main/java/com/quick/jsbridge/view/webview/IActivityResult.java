package com.quick.jsbridge.view.webview;

import android.content.Intent;

/**
 * Created by dailichun on 2017/12/6.
 * quick容器onActivityResult回调接口
 */
public interface IActivityResult {

    /**
     * 参数与onActivityResult一致
     * @param requestCode 请求code
     * @param resultCode 返回code
     * @param data 回传数据
     */
    void onResult(int requestCode, int resultCode, Intent data);

}
