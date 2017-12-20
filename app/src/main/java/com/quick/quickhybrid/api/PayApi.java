package com.quick.quickhybrid.api;

import android.webkit.WebView;

import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.bridge.IBridgeImpl;
import com.quick.jsbridge.view.IQuickFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by dailichun on 2017/12/6.
 * 测试的支付组件（自定义）API
 */
public class PayApi implements IBridgeImpl {

    /**
     * 注册API的别名
     */
    public static String RegisterName = "pay";

    /**
     * 测试支付
     * <p>
     * 返回：
     * access_token
     */
    public static void testPay(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "pay success");
        callback.applySuccess(map);
    }


}
