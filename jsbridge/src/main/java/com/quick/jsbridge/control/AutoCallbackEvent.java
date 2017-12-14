package com.quick.jsbridge.control;

import android.text.TextUtils;

import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.view.webview.QuickWebView;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by dailichun on 2017/12/7.
 * 主动回调事件
 */
public class AutoCallbackEvent implements AutoCallbackDefined {

    private HashMap<String, String> portMap;

    private QuickWebView wv;

    public AutoCallbackEvent(QuickWebView wv, HashMap<String, String> portMap) {
        this.portMap = portMap;
        this.wv = wv;
    }

    @Override
    public void onSwipeRefresh() {
        callJS(OnSwipeRefresh, wv, null);
    }

    @Override
    public void onPageResult(Map<String, Object> object) {
        callJS(OnPageResult, wv, object);
    }

    @Override
    public void onPageResume() {
        callJS(OnPageResume, wv, null);
    }

    @Override
    public void onPagePause() {
        callJS(OnPagePause, wv, null);
    }

    @Override
    public void onClickNbBack() {
        callJS(OnClickNbBack, wv, null);
    }

    @Override
    public void onClickBack() {
        callJS(OnClickBack, wv, null);
    }

    @Override
    public void onClickNbLeft() {
        callJS(OnClickNbLeft, wv, null);
    }

    @Override
    public void onClickNbTitle(int which) {
        Map<String, Object> object = new HashMap<>();
        object.put("which",which);
        callJS(OnClickNbTitle, wv, object);
    }

    @Override
    public void onClickNbRight(int which) {
        Map<String, Object> object = new HashMap<>();
        object.put("which",which);
        callJS(OnClickNbRight+which, wv, object);
    }

    @Override
    public void onSearch(Map<String, Object> object) {
        callJS(OnSearch, wv, object);
    }

    @Override
    public void onTitleChanged(Map<String, Object> object) {
        callJS(OnTitleChanged, wv, object);
    }

    @Override
    public void onScanCode(Map<String, Object> object) {
        callJS(OnScanCode, wv, object);
    }

    @Override
    public void onChoosePic(Map<String, Object> object) {
        callJS(OnChoosePic, wv, object);
    }

    @Override
    public void onChooseFile(Map<String, Object> object) {
        callJS(OnChooseFile, wv, object);
    }

    @Override
    public void onNetChanged(Map<String, Object> object) {
        callJS(OnNetChanged,wv,object);
    }

    @Override
    public void onChooseContact(Map<String, Object> object) {
        callJS(OnChooseContact,wv,object);
    }

    private void callJS(String key, QuickWebView wv, Map<String, Object> object) {
        if (wv == null) {
            return;
        }
        String port = portMap.get(key);
        if (TextUtils.isEmpty(port)) {
            Callback callback = new Callback(Callback.ERROR_PORT, wv);
            callback.applyNativeError(wv.getUrl(), key + "未注册");
        } else {
            Callback callback = new Callback(port, wv);
            callback.applySuccess(object);
        }
    }


    /**
     * 判断事件是否注册
     *
     * @param eventName
     * @return
     */
    public boolean isRegist(String eventName) {
        return !TextUtils.isEmpty(portMap.get(eventName));
    }
}
