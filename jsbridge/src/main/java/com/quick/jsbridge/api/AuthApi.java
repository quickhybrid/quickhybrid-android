package com.quick.jsbridge.api;

import android.webkit.WebView;


import com.quick.core.util.jsbridge.QuickModulesUtil;
import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.bridge.IBridgeImpl;
import com.quick.jsbridge.bridge.JSBridge;
import com.quick.jsbridge.view.IQuickFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by dailichun on 2017/12/6.
 * auth相关API
 */
public class AuthApi implements IBridgeImpl {

    /**
     * 注册API的别名
     */
    public static String RegisterName = "auth";

    /**
     * 获取token值
     * <p>
     * 返回：
     * access_token
     */
    public static void getToken(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", "test-token-quickhybrid");
        callback.applySuccess(map);
    }

    /**
     * H5页面初始化配置
     * 先验证白名单，验证失败直接显示错误状态页面，验证通过后注册自定义API，注册成功进行成功回调，否则进行失败回调。
     * <p>
     * 参数：
     * jsApiList：自定义API数组[{'ui':'com.quick.jsbridge.UiApi'},{'util':'com.quick.jsbridge.UtilApi'}]
     */
    public static void config(final IQuickFragment webLoader, final WebView wv, final JSONObject param, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //白名单验证
                webLoader.getWebloaderControl().setHasConfig(true);
                //注册自定义API
                boolean falg = true;
                try {
                    JSONArray apiJsonArray = param.optJSONArray("jsApiList");
                    if (apiJsonArray != null) {
                        for (int i = 0; i < apiJsonArray.length(); i++) {
                            String apiName = apiJsonArray.optString(i);
                            String apiPath = QuickModulesUtil.getProperties(wv.getContext(), apiName);

                            if (apiPath != null && apiPath != "") {
                                try {
                                    Class c = Class.forName(apiPath);
                                    JSBridge.register(apiName, c);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                    callback.applyFail(e.toString());
                                    falg = false;
                                    break;
                                }
                            }

                            if (!falg) {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    falg = false;
                    callback.applyFail(e.toString());
                }
                if (falg) {
                    callback.applySuccess();
                }
            }
        }).start();
    }

}
