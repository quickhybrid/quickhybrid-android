package com.quick.jsbridge.api;

import android.app.Fragment;
import android.content.Intent;
import android.webkit.WebView;


import com.google.zxing.integration.android.IntentIntegrator;
import com.quick.core.baseapp.component.FileChooseActivity;
import com.quick.core.baseapp.component.scan.ScanCaptureActivity;
import com.quick.core.util.common.JsonUtil;
import com.quick.core.util.device.PhotoSelector;
import com.quick.core.util.io.FileUtil;
import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.bridge.IBridgeImpl;
import com.quick.jsbridge.control.AutoCallbackDefined;
import com.quick.jsbridge.control.WebloaderControl;
import com.quick.jsbridge.view.IQuickFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import me.iwf.photopicker.PhotoPicker;
import quick.com.jsbridge.R;

/**
 * Created by dailichun on 2017/12/6.
 */
public class UtilApi implements IBridgeImpl {

    /**
     * 注册API的别名
     */
    public static String RegisterName = "util";

    /**
     * 打开二维码
     * <p>
     * 参数：
     * videoUrl：视频地址
     */
    public static void scan(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        Object fragment = webLoader.getPageControl().getFragment();
        IntentIntegrator integrator = null;
        if (fragment instanceof Fragment) {
            integrator = IntentIntegrator.forFragment((Fragment) fragment);
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            integrator = IntentIntegrator.forSupportFragment((android.support.v4.app.Fragment) fragment);
        }
        if (integrator != null) {
            integrator.setCaptureActivity(ScanCaptureActivity.class);
            integrator.initiateScan();
            webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnScanCode, callback.getPort());
        }
    }


    /**
     * 选择文件
     */
    public static void selectFile(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        try {
            param.putOpt("className", FileChooseActivity.class.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PageApi.openLocal(webLoader, wv, param, callback);
    }


    /**
     * 多图片选择(配合上传文件API同时使用)
     * <p>
     * 参数：
     * photoCount:可选图片的最大数,默认为9
     * showCamera:是否允许拍照，1：允许；0：不允许，默认不允许
     * showGif：是否显示gif图片，1：显示；0：不显示，默认不显示
     * previewEnabled：是否允许预览，1：允许，0：不允许，默认允许
     * selectedPhotos:已选图片,json数组
     */
    public static void selectImage(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        int photoCount = param.optInt("photoCount", 9);
        boolean showCamera = "1".equals(param.optString("showCamera", "0"));
        boolean showGif = "1".equals(param.optString("showGif", "0"));
        boolean previewEnabled = "1".equals(param.optString("previewEnabled", "1"));
        String[] items = new String[]{};
        JSONArray itemsJsonObject = param.optJSONArray("selectedPhotos");
        items = JsonUtil.parseJSONArray(itemsJsonObject, items);
        ArrayList<String> selectedPhotos = new ArrayList<>(Arrays.asList(items));
        if (photoCount < 1) {
            callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.status_request_error));
        } else {
            webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnChoosePic, callback.getPort());
            PhotoPicker.PhotoPickerBuilder builder = PhotoPicker.builder()
                    //设置最多选择数量
                    .setPhotoCount(photoCount)
                    //是否允许拍照
                    .setShowCamera(showCamera)
                    //是否显示gif图片
                    .setShowGif(showGif)
                    //设置已选图片
                    .setSelected(selectedPhotos)
                    //设置是否允许预览
                    .setPreviewEnabled(previewEnabled);
            Intent intent = builder.getIntent(webLoader.getPageControl().getActivity());
            Object fragment = webLoader.getPageControl().getFragment();
            if (fragment instanceof Fragment) {
                ((Fragment) fragment).startActivityForResult(intent, PhotoPicker.REQUEST_CODE);
            } else if (fragment instanceof android.support.v4.app.Fragment) {
                ((android.support.v4.app.Fragment) fragment).startActivityForResult(intent, PhotoPicker.REQUEST_CODE);
            }
        }
    }



    /**
     * 打开摄像机拍照
     * <p>
     * 参数：
     * selectedPhotos:图片地址，支持网络图片，手机本地图片
     * index：默认显示图片序号
     * showDeleteButton:是否显示删除按钮，1：显示，0：不显示，默认不显示。如果显示按钮则自动注册回调事件。
     */
    public static void cameraImage(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        int width = param.optInt("width", 720);
        int quality = param.optInt("quality", 70);
        webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnChoosePic, callback.getPort());
        PhotoSelector photoSelector = webLoader.getWebloaderControl().getPhotoSelect();
        photoSelector.setdQuality(quality);
        photoSelector.setWidth(width);
        photoSelector.requestSysCamera(webLoader, WebloaderControl.CAMERA_REQUEST_CODE);
    }

    /**
     * 打开文件
     * <p>
     * 参数：
     * path:文件本地路径
     */
    public static void openFile(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        String path = param.optString("path");
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            callback.applyFail(webLoader.getPageControl().getContext().getString(R.string.status_request_error));
        } else {
            FileUtil.openFile(webLoader.getPageControl().getActivity(), file);
            callback.applySuccess();
        }
    }

}
