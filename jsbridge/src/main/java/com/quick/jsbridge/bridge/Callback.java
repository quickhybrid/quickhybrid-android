package com.quick.jsbridge.bridge;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.quick.jsbridge.view.webview.QuickWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by dailichun on 2017/12/6.
 * 回调通道
 */

public class Callback {
    /**
     * 非API错误回调
     */
    public static final String ERROR_PORT = "3000";

    private static String JS_FUNCTION = "javascript:JSBridge._handleMessageFromNative(%s);";

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private String port;

    private WeakReference<QuickWebView> webViewRef;

    public Callback(String port, QuickWebView webView) {
        this.port = port;
        if (webView != null) {
            webViewRef = new WeakReference<>(webView);
        }
    }

    /**
     * 获取port值
     *
     * @return
     */
    public String getPort() {
        return port;
    }

    /**
     * 成功回调
     */
    public void applySuccess() {
        apply(1, "", new JSONObject());
    }

    /**
     * 成功回调
     *
     * @param object
     */
    public void applySuccess(Object object) {
        String json = new Gson().toJsonTree(object).toString();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        apply(1, "", jsonObject);
    }

    /**
     * 成功回调
     *
     * @param map
     */
    public void applySuccess(Map<String, Object> map) {
        apply(1, "", map == null ? null : new JSONObject(map));
    }

    /**
     * 成功回调
     *
     * @param result
     */
    public void applySuccess(JSONObject result) {
        apply(1, "", result);
    }

    /**
     * 所有API或者注册事件的回调方法
     * "{\"responseId\":%s,\"responseData\":%s}";
     *
     * @param responseData
     */
    public void apply(JSONObject responseData) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("responseId", port);
            jsonObject.put("responseData", responseData);
            String execJs = String.format(JS_FUNCTION, String.valueOf(jsonObject));
            callJS(execJs);
        } catch (JSONException e) {
            e.printStackTrace();
            apply(0, e.toString(), new JSONObject());
        }
    }

    /**
     * 所有API或者注册事件的回调方法
     * "{\"code\":%s,\"msg\":\"%s\",\"result\":%s}"
     *
     * @param code
     * @param msg
     * @param result
     */
    public void apply(int code, String msg, JSONObject result) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
            jsonObject.put("msg", msg);
            jsonObject.put("result", result);
            apply(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            apply(0, e.toString(), new JSONObject());
        }
    }

    /**
     * 所有API或者注册事件的回调方法
     *
     * @param code
     * @param msg
     * @param map
     */
    public void apply(int code, String msg, Map<String, Object> map) {
        apply(code, msg, map == null ? null : new JSONObject(map));
    }

    /**
     * 失败回调
     *
     * @param msg
     */
    public void applyFail(String msg) {
        apply(0, msg, new JSONObject());
    }

    /**
     * auth认证失败回调
     *
     * @param jsonObject
     */
    public void applyAuthError(JSONObject jsonObject) {
        applyError("authError", jsonObject);
    }

    /**
     * 本地主动上报错误信息,不走API回调通道
     * 使用场景：页面未调用api时容器捕获到的异常
     * "{\"handlerName\":\"handleError\",\"data\":{\"errorCode\":%s,\"errorUrl\":\"%s\",\"errorDescription\":\"%s\"}}"
     *
     * @param errorUrl
     * @param errorDescription
     */
    public void applyNativeError(String errorUrl, String errorDescription) {
        JSONObject data = new JSONObject();
        try {
            data.put("errorDescription", errorDescription);
            data.put("errorCode", port);
            data.put("errorUrl", errorUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            applyError("handleError", data);
        }
    }

    /**
     * 本地主动上报错误信息
     * 使用场景：页面未调用api时容器捕获到的异常
     * "{\"handlerName\":\"handleError\",\"data\":{\"errorCode\":%s,\"errorUrl\":\"%s\",\"errorDescription\":\"%s\"}}"
     *
     * @param handlerName
     * @param data
     */
    private void applyError(String handlerName, JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("handlerName", handlerName == null ? "" : handlerName);
            jsonObject.put("data", data == null ? "" : data);
            applyError(String.valueOf(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地主动上报错误信息
     * 使用场景：页面未调用api时容器捕获到的异常
     * "{\"handlerName\":\"handleError\",\"data\":{\"errorCode\":%s,\"errorUrl\":\"%s\",\"errorDescription\":\"%s\"}}"
     *
     * @param errorParam
     */
    private void applyError(String errorParam) {
        callJS(String.format(JS_FUNCTION, errorParam));
    }

    /**
     * webview调用js方法
     *
     * @param js
     */
    private void callJS(final String js) {
        if (webViewRef.get() == null) {
            Log.e("Quick", "webview为null");
            return;
        }
        Context context = webViewRef.get().getContext();
        if (checkContext(context)) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webViewRef.get().loadUrl(js);
                }
            });
        } else {
            Log.e("Quick", "Context为null或者未继承框架Activity");
        }
    }

    private boolean checkContext(Context context) {
        if (context == null) {
            return false;
        }
        Activity activity = (Activity) context;

        return !activity.isFinishing();

    }
}
