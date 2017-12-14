package com.quick.jsbridge.bridge;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.quick.jsbridge.api.NavigatorApi;
import com.quick.jsbridge.api.PageApi;
import com.quick.jsbridge.api.UIApi;
import com.quick.jsbridge.view.IQuickFragment;
import com.quick.jsbridge.view.webview.QuickWebView;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dailichun on 2017/12/6.
 * 定义交互通道
 */

public class JSBridge {

    /**
     * Quick约定的schem值,不可更改
     */
    private static final String QUICK_SCHEME = "QuickHybridJSBridge";

    /**
     * 注册方法缓存对象
     */
    private static Map<String, HashMap<String, Method>> exposedMethods = new HashMap<>();

    /**
     * 将api注册到缓存中
     *
     * @param apiModelName API别名
     * @param clazz        模块中定义的api
     */
    public static void register(String apiModelName, Class<? extends IBridgeImpl> clazz) {
        try {
            exposedMethods.put(apiModelName, getAllMethod(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否注册到缓存中
     *
     * @param apiModelName API别名
     */
    public static boolean isRegister(String apiModelName) {
        return exposedMethods.containsKey(apiModelName);
    }

    /**
     * 获取api类中所有符合要求的api方法并缓存
     *
     * @param injectedCls
     * @return
     * @throws Exception
     */
    private static HashMap<String, Method> getAllMethod(Class injectedCls) throws Exception {
        HashMap<String, Method> mMethodsMap = new HashMap<>();
        Method[] methods = injectedCls.getDeclaredMethods();
        for (Method method : methods) {
            String name;
            if (method.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC) || (name = method.getName()) == null) {
                continue;
            }
            Class[] parameters = method.getParameterTypes();
            if (null != parameters && parameters.length == 4) {
                if (parameters[1] == WebView.class && parameters[2] == JSONObject.class && parameters[3] == Callback.class) {
                    mMethodsMap.put(name, method);
                }
            }
        }
        return mMethodsMap;
    }

    /**
     * 通过反射调用API
     *
     * @param webLoader
     * @param url
     * @param hasConfig
     * @return
     */
    public static String callJava(IQuickFragment webLoader, String url, boolean hasConfig) {
        if (webLoader == null) {
            return "QuickFragment is null";
        }

        Callback callback = null;
        String error = null;
        String methodName = null;
        String apiName = null;
        String param = null;
        String port;
        QuickWebView webView = webLoader.getQuickWebView();

        boolean parseSuccess = false;
        while (!parseSuccess) {
            if (url.contains("#")) {
                error = "url不能包涵特殊字符'#'";
                break;
            }
            if (!url.startsWith(QUICK_SCHEME)) {
                error = "scheme错误";
                break;
            }
            if (TextUtils.isEmpty(url)) {
                error = "url不能为空";
                break;
            }
            Uri uri = Uri.parse(url);
            if (uri == null) {
                error = "url解析失败";
                break;
            }
            apiName = uri.getHost();
            if (TextUtils.isEmpty(apiName)) {
                error = "API_Nam为空";
                break;
            }
            port = uri.getPort() + "";
            if (TextUtils.isEmpty(port)) {
                error = "port为空";
                break;
            }
            callback = new Callback(port, webView);
            methodName = uri.getPath();
            methodName = methodName.replace("/", "");
            if (TextUtils.isEmpty(methodName)) {
                error = "方法名为空";
                break;
            }
            param = uri.getQuery();
            if (TextUtils.isEmpty(param)) {
                param = "{}";
            }
            parseSuccess = true;
        }

        //参数解析失败
        if (!parseSuccess) {
            if (callback == null) {
                new Callback(Callback.ERROR_PORT, webView).applyNativeError(url,error);
            } else {
                callback.applyFail(error);
            }
            return error;
        }
        //解析成功 反射方式调用本地api
        if (exposedMethods.containsKey(apiName)) {
            if (chechConfig(callback, hasConfig, apiName, methodName)) {
                HashMap<String, Method> methodHashMap = exposedMethods.get(apiName);
                if (methodHashMap != null && methodHashMap.size() != 0 && methodHashMap.containsKey(methodName)) {
                    Method method = methodHashMap.get(methodName);
                    if (method != null) {
                        try {
                            method.invoke(null, webLoader, webView, new JSONObject(param), callback);
//                            LogUtil.logBuried(apiName + "." + methodName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.applyFail(e.toString());
                        }
                    }
                } else {
                    //未找到该方法
                    error = apiName + "." + methodName + "未找到";
                    callback.applyFail(error);
                    return error;
                }
            }
        } else {
            //未注册API
            error = apiName + "未注册";
            callback.applyFail(error);
            return error;
        }
        return null;
    }

    /**
     * 验证config
     *
     * @param callback
     * @param hasConfig
     * @param apiName
     * @return
     */
    private static boolean chechConfig(Callback callback, boolean hasConfig, String apiName, String methodName) {
        if (hasConfig) {
            return true;
        } else {
            if (apiName.equals(UIApi.RegisterName) || apiName.equals(PageApi.RegisterName)
                    || apiName.equals(NavigatorApi.RegisterName)
                    || methodName.contains("getQuickVersion")
                    || methodName.contains("getAppVersion")
                    || methodName.contains("config")) {
                return true;
            } else {
                callback.applyFail("没有权限");
                //未通过配置
                return false;
            }
        }
    }
}
