package com.quick.jsbridge.api;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.quick.core.application.FrmApplication;
import com.quick.core.baseapp.baseactivity.control.PageControl;
import com.quick.core.util.common.QuickUtil;
import com.quick.jsbridge.bean.QuickBean;
import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.bridge.IBridgeImpl;
import com.quick.jsbridge.control.AutoCallbackDefined;
import com.quick.jsbridge.control.WebloaderControl;
import com.quick.jsbridge.view.IQuickFragment;
import com.quick.jsbridge.view.QuickWebLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import quick.com.jsbridge.R;


/**
 * Created by dailichun on 2017/12/6.
 */
public class PageApi implements IBridgeImpl {

    /**
     * 注册API的别名
     */
    public static String RegisterName = "page";

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
     * 打开新的H5页面
     * <p>
     * 参数：
     * 其他参数见{@link QuickBean}属性
     */
    public static void open(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        QuickBean bean = new Gson().fromJson(String.valueOf(param), QuickBean.class);
        Intent intent = new Intent();
        intent.setClass(webLoader.getPageControl().getContext(), QuickWebLoader.class);
        if (bean == null) {
            callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.status_request_error));
        } else {
            intent.putExtra("bean", bean);
            intent.putExtra(PageControl.SCREEN_ORIENTATION, bean.orientation);
            //注册长期回调，如果下一个页面回传数据会主动回调
            webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnPageResult, callback.getPort());
            Object fragment = webLoader.getPageControl().getFragment();
            if (fragment instanceof Fragment) {
                ((Fragment) fragment).startActivityForResult(intent, WebloaderControl.INTENT_REQUEST_CODE);
            } else if (fragment instanceof android.support.v4.app.Fragment) {
                ((android.support.v4.app.Fragment) fragment).startActivityForResult(intent, WebloaderControl.INTENT_REQUEST_CODE);
            }
        }
    }

    /**
     * 打开原生页面
     * <p>
     * 参数：
     * className: 原生页面activity类名
     * isOpenExist：是否打开已存在的页面,为1时:如果页面已经存在，则直接打开且关闭页面上层所有的其他页面；如果不存在，则打开一个新页面
     * data:        传递下一页面的数据,json格式
     */
    public static void openLocal(IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        String activityName = param.optString("className");
        String isOpenExist = param.optString("isOpenExist");
        String data = param.optString("data");
        try {
            Class clz = Class.forName(activityName);
            Intent intent = new Intent(webLoader.getPageControl().getContext(), clz);
            intent.putExtra("from", "quick");
            if ("1".equals(isOpenExist)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            if (data.startsWith("[")) {
                QuickUtil.putIntentExtra(new JSONArray(data), intent);
            } else if (data.startsWith("{")) {
                QuickUtil.putIntentExtra(new JSONObject(data), intent);
            }
            Object fragment = webLoader.getPageControl().getFragment();
            if (fragment instanceof Fragment) {
                ((Fragment) fragment).startActivityForResult(intent, WebloaderControl.INTENT_REQUEST_CODE);
            } else if (fragment instanceof android.support.v4.app.Fragment) {
                ((android.support.v4.app.Fragment) fragment).startActivityForResult(intent, WebloaderControl.INTENT_REQUEST_CODE);
            }
            webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnPageResult, callback.getPort());
        } catch (Exception e) {
            e.printStackTrace();
            callback.applyFail(e.toString());
        }
    }

    /**
     * 关闭当前Activity
     * <p>
     * 参数：
     * resultData:    回传上个页面的值,如果为空则不回传
     */
    public static void close(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        int popPageNumber = 1;
        if (param.has("popPageNumber")) {
            popPageNumber = param.optInt("popPageNumber", 1);
        }
        if (popPageNumber > 1) {
            List<Activity> activityList = FrmApplication.getInstance().activityList;
            Activity[] closeActivitys = new Activity[popPageNumber];
            if (activityList != null) {
                for (int i = 0; i < popPageNumber; i++) {
                    if (i >= activityList.size() - 1) {
                        break;
                    }
                    closeActivitys[i] = activityList.get(activityList.size() - i - 1);
                }
            }
            if (closeActivitys.length > 0) {
                for (Activity activity : closeActivitys) {
                    activity.finish();
                }
                return;
            }
        }
        String jsonStr = param.optString(WebloaderControl.RESULT_DATA);
        if (TextUtils.isEmpty(jsonStr)) {
            webLoader.getPageControl().getActivity().finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra(WebloaderControl.RESULT_DATA, jsonStr);
            webLoader.getPageControl().getActivity().setResult(Activity.RESULT_OK, intent);
            webLoader.getPageControl().getActivity().finish();
        }

    }

    /**
     * 重载页面
     */
    public static void reload(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        wv.reload();
        callback.applySuccess();
    }


}
