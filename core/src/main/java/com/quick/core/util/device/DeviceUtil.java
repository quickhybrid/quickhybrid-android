package com.quick.core.util.device;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.quick.core.util.app.AppUtil;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;


/**
 * Created by dailichun on 2017/12/7.
 * 设备工具类
 */
public class DeviceUtil {

    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    /**
     * 设置状态栏是否可见
     *
     * @param visible
     */
    public static void setStatusBarVisibility(Activity activity, boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置状态栏背景色 5.0+系统有效
     *
     * @param activity
     * @param colorResId
     */
    public static void setStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                if (window != null) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(activity.getResources().getColor(colorResId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置dialog界面的状态栏背景色 5.0+系统有效
     *
     * @param dialog
     * @param colorResId
     */
    public static void setStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                if (window != null) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取当前网络状态
     *
     * @param context
     * @return -1:无网络 1：wifi 0：移动网络
     */
    public static int getNetWorkType(Context context) {
        int mNetWorkType = NETWORK_NONE;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            int netType = networkInfo.getType();
            if (netType == ConnectivityManager.TYPE_WIFI) {
                mNetWorkType = NETWORK_WIFI;
            } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                mNetWorkType = NETWORK_MOBILE;
            }
        }
        return mNetWorkType;
    }

    /**
     * 拨打电话
     *
     * @param con
     * @param phonenum
     */
    public static void callPhone(Context con, String phonenum) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phonenum));
        con.startActivity(intent);
    }

    /**
     * 发送消息
     *
     * @param con
     * @param phonenum
     * @param body
     */
    public static void sendMsg(Context con, String phonenum, String body) {
        Uri smsToUri = Uri.parse("smsto:" + phonenum);
        Intent mIntent = new Intent(Intent.ACTION_SENDTO,
                smsToUri);
        mIntent.putExtra("sms_body", body);
        con.startActivity(mIntent);
    }

    /**
     * 发送消息
     *
     * @param con
     * @param phonenum
     */
    public static void sendMsg(Context con, String phonenum) {
        sendMsg(con, phonenum, "");
    }

    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    public static boolean existSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID.
     *
     * @param con
     * @return
     */
    public static String getDeviceId(Context con) {
        TelephonyManager tm = (TelephonyManager) con
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = tm.getDeviceId();
        if (TextUtils.isEmpty(deviceid)) {
            deviceid = Settings.Secure.getString(con.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceid;
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public static String getDeviceInfo() {
        return android.os.Build.MANUFACTURER + "/" + android.os.Build.MODEL + "/android" + android.os.Build.VERSION.RELEASE + "/" + android.os.Build.TIME;
    }

    /**
     * 获取mac 地址
     * 只支持6.0+，并且只有开启wifi才能获取
     *
     * @return
     */
    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 控制键盘显隐，如果输入法在窗口上已经显示，则隐藏，反之则显示
     *
     * @param context
     */
    public static void toggleInputKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideInputKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view =activity.getCurrentFocus();
        if (view!=null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param et
     */
    public static void hideInputKeyboard(View et) {
        InputMethodManager imm = (InputMethodManager) AppUtil.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * 弹出键盘
     *
     * @param et
     */
    public static void showInputKeyboard(View et) {
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) AppUtil.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);
    }

    /**
     * 获取屏幕像素
     *
     * @param con
     * @return
     */
    public static Point getPhonePixel(Context con) {
        WindowManager wm = (WindowManager) con
                .getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

    /**
     * 获取屏幕宽度
     *
     * @param con
     * @return
     */
    public static int getPhoneWidth(Context con) {
        Point point = getPhonePixel(con);
        return point.x;
    }

    /**
     * 获取屏幕高度
     *
     * @param con
     * @return
     */
    public static int getPhoneHeight(Context con) {
        Point point = getPhonePixel(con);
        return point.y;
    }

    /**
     * 调用系统浏览器
     *
     * @param con
     * @param url
     */
    public static void invokeSystemBrowser(Context con, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        con.startActivity(intent);
    }

    /**
     * 是否平板设备
     *
     * @param con
     * @return
     */
    public static boolean isTablet(Context con) {
        return (con.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
