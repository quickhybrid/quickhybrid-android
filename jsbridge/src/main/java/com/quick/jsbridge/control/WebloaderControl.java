package com.quick.jsbridge.control;

import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.quick.core.ui.widget.SegActionCallBack;
import com.quick.core.util.common.QuickUtil;
import com.quick.core.util.device.PhotoSelector;
import com.quick.jsbridge.api.AuthApi;
import com.quick.jsbridge.api.DeviceApi;
import com.quick.jsbridge.api.NavigatorApi;
import com.quick.jsbridge.api.PageApi;
import com.quick.jsbridge.api.RuntimeApi;
import com.quick.jsbridge.api.UIApi;
import com.quick.jsbridge.api.UtilApi;
import com.quick.jsbridge.bean.QuickBean;
import com.quick.jsbridge.bridge.JSBridge;
import com.quick.jsbridge.view.IQuickFragment;
import com.quick.jsbridge.view.webview.IActivityResult;
import com.quick.jsbridge.view.webview.QuickWebChromeClient;
import com.quick.jsbridge.view.webview.QuickWebView;
import com.quick.jsbridge.view.webview.QuickWebviewClient;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import quick.com.jsbridge.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dailichun on 2017/12/6.
 * quick容器业务控制器
 */

public class WebloaderControl implements IActivityResult, SegActionCallBack, DownloadListener {
    /**
     * page api页面跳转的requestCode值
     */
    public static int INTENT_REQUEST_CODE = 0x1010;

    /**
     * 拍照requestCode值
     */
    public static int CAMERA_REQUEST_CODE = 0x1012;

    /**
     * 空白页,页面异常以及关闭页面时加载
     */
    public static String BLANK = "about:blank";

    /**
     * 页面之间数据回调key值
     */
    public static final String RESULT_DATA = "resultData";

    public QuickBean bean;

    public QuickWebView wv;

    public AutoCallbackEvent autoCallbackEvent;

    /**
     * 长期回调对象缓存,其中key值是跟前端约定好的别名,value值是port值
     */
    private HashMap<String, String> portMap = new HashMap<>();

    private IQuickFragment quickFragment;

    private PageLoad pageLoad;

    private PhotoSelector photoSelector;

    public WebloaderControl(IQuickFragment quickFragment, QuickBean bean, QuickWebView wv) {
        this.quickFragment = quickFragment;
        this.bean = bean;
        this.wv = wv;
        autoCallbackEvent = new AutoCallbackEvent(wv, portMap);
        photoSelector = new PhotoSelector();
        initWebView();
        registerFrmApi();
    }

    private void registerFrmApi() {
        //注册框架API
        JSBridge.register(AuthApi.RegisterName, AuthApi.class);
        JSBridge.register(DeviceApi.RegisterName, DeviceApi.class);
        JSBridge.register(NavigatorApi.RegisterName, NavigatorApi.class);
        JSBridge.register(PageApi.RegisterName, PageApi.class);
        JSBridge.register(RuntimeApi.RegisterName, RuntimeApi.class);
        JSBridge.register(UIApi.RegisterName, UIApi.class);
        JSBridge.register(UtilApi.RegisterName, UtilApi.class);
    }

    /**
     * 初始化webview
     */
    private void initWebView() {
        pageLoad = new PageLoad(quickFragment);
        wv.setWebViewClient(new QuickWebviewClient(pageLoad));
        wv.setWebChromeClient(new QuickWebChromeClient(pageLoad));
        //监听下载
        wv.setDownloadListener(this);
    }

    /**
     * 开始加载页面
     */
    public void loadPage() {
        if (bean == null) {
            quickFragment.getPageControl().toast(quickFragment.getPageControl().getContext().getString(R.string.status_data_error));
            quickFragment.getPageControl().getActivity().finish();
        } else {
            //设置cookie
            QuickUtil.setCookies(wv.getContext(), bean.pageUrl);
            //加载页面
            wv.loadUrl(bean.pageUrl);
        }
    }

    public void setHasConfig(boolean hasConfig) {
        pageLoad.setHasConfig(hasConfig);
    }

    /**
     * 加载历史非空上一页
     *
     * @param isRefresh 是否刷新，点击返回按钮时isRefresh为false，点击错误页面时isRefresh为true
     */
    public void loadLastPage(boolean isRefresh) {
        List<String> historyUrls = pageLoad.getHistoryUrl();
        if (historyUrls.isEmpty()) {
            if (isRefresh) {
                loadPage();
            } else {
                quickFragment.getPageControl().getActivity().finish();
            }
        } else {
            if (isRefresh) {
                wv.loadUrl(historyUrls.get(historyUrls.size() - 1));
            } else {
                if (historyUrls.size() == 1) {
                    quickFragment.getPageControl().getActivity().finish();
                } else {
                    String url = historyUrls.get(historyUrls.size() - 2);
                    historyUrls.remove(historyUrls.size() - 1);
                    wv.loadUrl(url);
                }
            }
        }
    }

    /**
     * 注册长期回调
     *
     * @param key
     * @param port
     */
    public void addPort(String key, String port) {
        portMap.put(key, port);
    }

    /**
     * 删除长期回调
     *
     * @param key
     */
    public void removePort(String key) {
        portMap.remove(key);
    }

    /**
     * 是否注册
     *
     * @param key
     */
    public boolean containsPort(String key) {
        return portMap.containsKey(key);
    }

    /**
     * 设置图片选择器
     *
     * @param photoSelector
     */
    public void setPhotoSelect(PhotoSelector photoSelector) {
        this.photoSelector = photoSelector;
    }

    /**
     * 获取图片选择器
     */
    public PhotoSelector getPhotoSelect() {
        return photoSelector;
    }

    /**
     * Fragment容器onResume时触发长期回调
     */
    public void onResume() {
        if (autoCallbackEvent.isRegist(AutoCallbackDefined.OnPageResume)) {
            autoCallbackEvent.onPageResume();
        }
        wv.onResume();
    }

    /**
     * Fragment容器onPause时触发长期回调
     */
    public void onPause() {
        if (autoCallbackEvent.isRegist(AutoCallbackDefined.OnPagePause)) {
            autoCallbackEvent.onPagePause();
        }
        wv.onPause();
    }

    /**
     * Fragment容器onDestroyView
     */
    public void onDestroy() {
        if (wv != null) {
            wv.loadUrl(BLANK);
            wv.clearHistory();
        }
    }

    /**
     * Fragment容器onActivityResult时触发长期回调即页面跳转回传值
     *
     * @param requestCode 请求code
     * @param resultCode  返回code
     * @param data        回传数据
     */
    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            final Map<String, Object> object = new HashMap<>();
            object.put("resultCode", resultCode);
            if (requestCode == INTENT_REQUEST_CODE) {
                //页面跳转回传值
                String jsonStr = data == null ? "" : data.getStringExtra(RESULT_DATA);
                object.put(RESULT_DATA, jsonStr);
                autoCallbackEvent.onPageResult(object);
            } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
                //扫描二维码回传值
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                String ewmString = result.getContents();
                object.put(RESULT_DATA, ewmString == null ? "" : ewmString);
                autoCallbackEvent.onScanCode(object);
            } else if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE) {
                //选择或预览图片回传值
                ArrayList<String> photos = null;
                if (data != null) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }
                object.put(RESULT_DATA, photos == null ? "" : photos);
                autoCallbackEvent.onChoosePic(object);

            } else if (requestCode == CAMERA_REQUEST_CODE) {
                //拍照
                if (photoSelector != null) {
                    photoSelector.handleCamera(new PhotoSelector.CompressResult() {
                        @Override
                        public void onCompelete(String path) {
                            object.put(RESULT_DATA, path);
                            autoCallbackEvent.onChoosePic(object);
                        }
                    });
                }

            }
        }

        //浏览器支持file控件选择文件
        pageLoad.getFileChooser().onChooseFileResult(requestCode, resultCode, data);
    }

    /**
     * 切换标题
     *
     * @param i 点击按钮的索引
     */
    @Override
    public void segAction(int i) {
        Map<String, Object> object = new HashMap<>();
        object.put("index", i);
        autoCallbackEvent.onTitleChanged(object);
    }

    /**
     * 监听网页下载，跳转到原生浏览器进行下载
     *
     * @param url
     * @param userAgent
     * @param contentDisposition
     * @param mimetype
     * @param contentLength
     */
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        quickFragment.getPageControl().getActivity().startActivity(intent);
    }
}
