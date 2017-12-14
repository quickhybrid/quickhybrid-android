package com.quick.jsbridge.api;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;
import android.webkit.WebView;

import com.quick.core.util.device.DeviceUtil;
import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.bridge.IBridgeImpl;
import com.quick.jsbridge.view.IQuickFragment;

import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by dailichun on 2017/12/7.
 * 设备相关API
 */
public class DeviceApi implements IBridgeImpl {

    /**
     * 注册API的别名
     */
    public static String RegisterName = "device";

    /**
     * 设置横竖屏
     * <p>
     * 参数：
     * orientation：1表示竖屏，0表示横屏，其他表示跟随系统
     */
    public static void setOrientation(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        int orientation = param.optInt("orientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (orientation >= ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED && orientation <= ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
            webLoader.getPageControl().getActivity().setRequestedOrientation(orientation);
            callback.applySuccess();
        } else {
            callback.applyFail("orientation值超出范围，请设置-1到14");
        }
    }

    /**
     * 获取设备唯一编码
     * <p>
     * 返回：
     * deviceId
     */
    public static void getDeviceId(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        Map<String, Object> object = new HashMap<>();
        object.put("deviceId", DeviceUtil.getDeviceId(webLoader.getPageControl().getActivity()));
        callback.applySuccess(object);
    }


    /**
     * 获取设备基础信息
     * <p>
     * 返回：
     * UAInfo
     * pixel
     * deviceId
     * netWorkType
     */
    public static void getVendorInfo(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        Map<String, Object> object = new HashMap<>();
        //设备厂商以及型号
        String type = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        object.put("uaInfo", "android " + type);
        //设备分辨率
        Point point = DeviceUtil.getPhonePixel(webLoader.getPageControl().getActivity());
        object.put("pixel", point.x + "*" + point.y);
        //唯一标识(机器码或者MAC地址)
        object.put("deviceId", DeviceUtil.getDeviceId(webLoader.getPageControl().getActivity()));
        //网络状态 -1:无网络1：wifi 0：移动网络
        object.put("netWorkType", DeviceUtil.getNetWorkType(webLoader.getPageControl().getActivity()));
        callback.applySuccess(object);
    }

    /**
     * 打电话
     * <p>
     * 参数：
     * phoneNum：电话号码
     */
    public static void callPhone(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        String phoneNum = param.optString("phoneNum");
        DeviceUtil.callPhone(webLoader.getPageControl().getActivity(), phoneNum);
        callback.applySuccess();
    }

    /**
     * 发短信
     * <p>
     * 参数：
     * phoneNum：电话号码
     * message:短信内容
     */
    public static void sendMsg(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        String phoneNum = param.optString("phoneNum");
        String message = param.optString("message");
        DeviceUtil.sendMsg(webLoader.getPageControl().getActivity(), phoneNum, message);
        callback.applySuccess();
    }

    /**
     * 控制键盘显隐，如果输入法在窗口上已经显示，则隐藏，反之则显示
     */
    public static void closeInputKeyboard(final IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        wv.postDelayed(new Runnable() {
            public void run() {
                DeviceUtil.hideInputKeyboard(webLoader.getPageControl().getActivity());
                callback.applySuccess();
            }
        }, 200);
    }


    /**
     * 获取当前网络状态
     * <p>
     * 返回：
     * netWorkType：-1:无网络，1：wifi，0：移动网络
     */
    public static void getNetWorkInfo(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        Map<String, Object> object = new HashMap<>();
        object.put("netWorkType", DeviceUtil.getNetWorkType(webLoader.getPageControl().getActivity()));
        callback.applySuccess(object);
    }



    /**
     * 手机震动
     * <p>
     * duration：持续时间
     */
    public static void vibrate(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        long time = param.optLong("duration", 1000);
        Vibrator vib = (Vibrator) webLoader.getPageControl().getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(time);
    }
}
