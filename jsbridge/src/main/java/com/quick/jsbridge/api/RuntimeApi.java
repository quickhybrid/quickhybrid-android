package com.quick.jsbridge.api;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.webkit.WebView;

import com.quick.core.net.HttpUtil;
import com.quick.core.util.app.AppUtil;
import com.quick.core.util.common.RuntimeUtil;
import com.quick.core.util.gis.CoordMath;
import com.quick.core.util.gis.MyLatLngPoint;
import com.quick.core.util.io.FileUtil;
import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.bridge.IBridgeImpl;
import com.quick.jsbridge.view.IQuickFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import quick.com.jsbridge.BuildConfig;
import quick.com.jsbridge.R;


/**
 * Created by dailichun on 2017/12/7.
 * 运行时API
 */
public class RuntimeApi implements IBridgeImpl {

    /**
     * 注册API的别名
     */
    public static String RegisterName = "runtime";



    /**
     * 打开第三方app
     * <p>
     * 参数：
     * packageName ：applicationId
     * className   ：指定打开的页面类名，可为空
     * actionName  ：manifest中activity设置的actionname
     * scheme      ：manifest中activity设置的scheme
     * data        ：传给页面的参数
     */
    public static void launchApp(final IQuickFragment webLoader, WebView wv, JSONObject param, final Callback callback) {
        String packageName = param.optString("packageName");
        String className = param.optString("className");
        String actionName = param.optString("actionName");
        String scheme = param.optString("scheme");
        String data = param.optString("data");
        try {
            Intent intent = null;
            if (!TextUtils.isEmpty(packageName)) {
                intent = RuntimeUtil.getLaunchAppIntent(webLoader.getPageControl().getActivity(), packageName, className);

            } else if (!TextUtils.isEmpty(actionName)) {
                intent = new Intent(actionName);

            } else if (!TextUtils.isEmpty(scheme)) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme + "://"));
            }

            if (intent != null) {
                if (!TextUtils.isEmpty(data)) {
                    intent.putExtra("data", data);
                }
                webLoader.getPageControl().getActivity().startActivity(intent);
                callback.applySuccess();
            } else {
                callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.status_request_error));
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.applyFail(e.getMessage());
        }
    }

    /**
     * 获取APP版本号
     * <p>
     * 返回：
     * version
     */
    public static void getAppVersion(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        Map<String, Object> object = new HashMap<>();
        object.put("version", RuntimeUtil.getVersionName(webLoader.getPageControl().getActivity()));
        callback.applySuccess(object);
    }

    /**
     * 获取Quick版本号
     * <p>
     * 返回：
     * version
     */
    public static void getQuickVersion(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        Map<String, Object> object = new HashMap<>();
        object.put("version", BuildConfig.VERSION_NAME);
        callback.applySuccess(object);
    }



    /**
     * 获取经纬度以及详细地址信息
     * <p>
     * 参数：
     * isShowDetail：是否显示详细地址信息 1：显示，0：不显示，默认为0
     * coordinate:返回的坐标系类型 1：GCJ02（火星坐标系/高德） 0：WGS84（地球坐标系）
     * 返回：
     * location：经纬度
     * addressComponent：详细地址信息
     */
    public static void getGeolocation(final IQuickFragment webLoader, WebView wv, final JSONObject param, final Callback callback) {
        final boolean isShowDetail = "1".equals(param.optString("isShowDetail", "0"));
        final int coordinate = param.optInt("coordinate", 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                LocationManager lm = (LocationManager) webLoader.getPageControl().getActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
                criteria.setAltitudeRequired(false);//不要求海拔
                criteria.setBearingRequired(false);//不要求方位
                criteria.setCostAllowed(true);//允许有花费
                criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

                //从可用的位置提供器中，匹配以上标准的最佳提供器
                String provider = lm.getBestProvider(criteria, true);
                if (TextUtils.isEmpty(provider)) {
                    callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.toast_gps_not_open));
                    return;
                }
                if (ActivityCompat.checkSelfPermission(AppUtil.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AppUtil.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.toast_no_permission));
                    return;
                }
                Location location = lm.getLastKnownLocation(provider);
                if (location == null) {
                    callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.toast_location_fail));
                    return;
                }
                MyLatLngPoint myLatLngPoint = new MyLatLngPoint(location.getLatitude(), location.getLongitude());
                if (coordinate == 1) {
                    myLatLngPoint = CoordMath.wgs2gcj(myLatLngPoint);
                }
                try {
                    String latlng = myLatLngPoint.getLat() + "," + myLatLngPoint.getLng();
                    Map<String, Object> jsonObject = new HashMap<>();
                    jsonObject.put("longitude", myLatLngPoint.getLng());
                    jsonObject.put("latitude", myLatLngPoint.getLat());

                    if (isShowDetail) {
                        //百度地图服务
                        String bs = HttpUtil.get("http://api.map.baidu.com/geocoder/v2/?location=" + latlng + "&output=json&pois=1&ak=" + webLoader.getPageControl().getContext().getString(R.string.baiduMap_ak));
                        if (bs != null) {
                            JSONObject address = new JSONObject(bs);
                            //获取results节点下的位置信息
                            int status = address.getInt("status");
                            if (status == 0) {
                                address = address.getJSONObject("result");
                                //解析成功"addressComponent":{"country":"中国","country_code":0,"province":"江苏省","city":"苏州市","district":"","adcode":"","street":"","street_number":"","direction":"","distance":""}
                                address = address.getJSONObject("addressComponent");
                                jsonObject.put("addressComponent", address);
                            }
                        }
                    }
                    callback.applySuccess(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.applyFail(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * 清空webview缓存
     */
    public static void clearCache(final IQuickFragment webLoader, final WebView wv, JSONObject param, final Callback callback) {
        wv.clearHistory();
        wv.clearCache(true);
        wv.clearFormData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtil.deleteFile(new File(webLoader.getPageControl().getContext().getCacheDir().getAbsolutePath()));
                webLoader.getPageControl().getContext().deleteDatabase("webview.db");
                webLoader.getPageControl().getContext().deleteDatabase("webviewCache.db");
                callback.applySuccess();
            }
        }).start();
    }

    /**
     * 复制到剪贴板
     * <p>
     * 参数：
     * text：复制信息
     */
    public static void clipboard(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        String text = param.optString("text");
        RuntimeUtil.clipboard(webLoader.getPageControl().getContext(), text);
        callback.applySuccess();
    }

    /**
     * 外部浏览器打开网页
     * <p>
     * 参数：
     * url：页面地址
     */
    public static void openUrl(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        String url = param.optString("url");
        if (!TextUtils.isEmpty(url)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            webLoader.getPageControl().getActivity().startActivity(intent);
        } else {
            callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.status_data_error));
        }
    }

}
