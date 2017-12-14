package com.quick.core.util.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by dailichun on 2017/12/6.
 * quick工具类
 */

public class QuickUtil {

    /**
     * 将jsnon数据传入Intent传递给本地页面
     *
     * @param jsonObject
     * @param intent
     * @return
     * @throws JSONException
     */
    public static Intent putIntentExtra(JSONObject jsonObject, Intent intent) throws JSONException {
        if (jsonObject == null || intent == null) {
            return null;
        }
        Iterator<String> it = jsonObject.keys();
        while (it.hasNext()) {
            String key = it.next();
            Object valueObj = jsonObject.get(key);
            if (valueObj instanceof Boolean) {
                intent.putExtra(key, (boolean) valueObj);
            } else if (valueObj instanceof String) {
                intent.putExtra(key, valueObj.toString());
            } else if (valueObj instanceof Integer) {
                intent.putExtra(key, (int) valueObj);
            } else if (valueObj instanceof Double) {
                intent.putExtra(key, (Double) valueObj);
            } else if (valueObj instanceof Float) {
                intent.putExtra(key, (Float) valueObj);
            } else if (valueObj instanceof Byte) {
                intent.putExtra(key, (Byte) valueObj);
            } else if (valueObj instanceof Short) {
                intent.putExtra(key, (Short) valueObj);
            } else if (valueObj instanceof Long) {
                intent.putExtra(key, (Long) valueObj);
            } else {
                intent.putExtra(key, valueObj.toString());
            }
        }
        return intent;
    }

    /**
     * 将jsnon数据传入Intent传递给本地页面
     *
     * @param jsonArray
     * @param intent
     * @return
     * @throws JSONException
     */
    public static Intent putIntentExtra(JSONArray jsonArray, Intent intent) throws JSONException {
        if (jsonArray == null || intent == null) {
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            putIntentExtra((JSONObject) jsonArray.get(i), intent);
        }
        return intent;
    }

    /**
     * 本地页面回传数据给Quick页面
     *
     * @param activity
     * @param json
     */
    public static void quickResult(Activity activity, String json) {
        Intent intent = new Intent();
        intent.putExtra("resultData", json);
        activity.setResult(Activity.RESULT_OK, intent);
    }

    /**
     * 给webview设置cookie
     *
     * @param context
     * @param url
     */
    public static void setCookies(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                CookieSyncManager.createInstance( context);
            }
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();
            // 自动注入cookie，这个在使用cookie作为用户校验时有用
            cookieManager.setCookie(url, "JSESSIONID=" + QuickUtil.getToken());
            CookieSyncManager.getInstance().sync();
        }
    }

    public static String getToken() {
        // cookie注入的token可以通过对应的应用获取
        // 一般需要结合登陆，这里只预留一个接口，默认写死
        return "quickhybrid-test-cookie";
    }
}
