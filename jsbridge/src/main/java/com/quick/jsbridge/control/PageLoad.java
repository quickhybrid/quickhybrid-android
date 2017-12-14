package com.quick.jsbridge.control;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.quick.core.baseapp.baseactivity.control.StatusControl;
import com.quick.core.ui.app.INbControl;
import com.quick.core.util.device.DeviceUtil;
import com.quick.core.util.io.IOUtil;
import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.view.IQuickFragment;
import com.quick.jsbridge.view.webview.IFileChooser;
import com.quick.jsbridge.view.webview.ILoadPage;


import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import quick.com.jsbridge.R;


/**
 * Created by dailichun on 2017/12/7.
 * webview加载页面过程
 */
public class PageLoad implements ILoadPage {

    private String[] imgeSuffix = new String[]{".bmp", ".dib", ".gif", ".jfif", ".jpe", ".jpeg", ".jpg", ".png", ".tif", ".tiff"};

    private IQuickFragment fragment;

    private Timer timer;

    /**
     * 历史访问记录
     */
    private List<String> historyUrl = new ArrayList<>();

    /**
     * 页面加载超时时间，默认20秒
     */
    private int TIME_OUT = 20000;

    private IFileChooser fileChooser;

    private boolean isConfig = false;

    PageLoad(IQuickFragment fragment) {
        this.fragment = fragment;
        fileChooser = new FileChooser(fragment);
    }

    @Override
    public IQuickFragment getFragment() {
        return fragment;
    }

    @Override
    public IFileChooser getFileChooser() {
        return fileChooser;
    }

    @Override
    public boolean isLoadImage(String url) {
        for (String suffix : imgeSuffix) {
            if (url.toLowerCase().endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse response = null;
        if (isLoadImage(url) && url.startsWith("http://localhost/")) {
            try {
                String newUrl = url.replace("http://localhost/", "");
                if (newUrl.startsWith("file/")) {
                    newUrl = newUrl.replace("file/", "file:///");
                } else if (newUrl.startsWith("assets/")) {
                    newUrl = newUrl.replace("assets/", "assets://");
                }
                final PipedOutputStream out = new PipedOutputStream();
                PipedInputStream in = new PipedInputStream(out);

                ImageLoader.getInstance().loadImage(newUrl, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        if (bitmap != null) {
                            try {
                                out.write(IOUtil.bitmap2Bytes(bitmap));
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                response = new WebResourceResponse("image/png", "UTF-8", in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        else {
        //为每个请求设置cookie，由于iOS没有对应方案所以暂时放弃设置
//            QuickUtil.setCookies(view.getContext(), url);
//        }
        return response;
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        cancelTimeKeeper();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (WebloaderControl.BLANK.equals(url)) {
            return;
        }
        addHistoryUrl(url);
        fragment.getPageControl().getStatusPage().hideStatus();
        setTimeKeeper(view);
    }

    @Override
    public void onReceivedTitle(String url, String title) {
        Pattern p = Pattern.compile("(\\w+\\.){2,4}\\w+(\\:\\d{1,9})?\\/\\w");
        if (WebloaderControl.BLANK.equals(url) || p.matcher(title).find()) {
            return;
        }
        if (!TextUtils.isEmpty(title) && !url.endsWith(title)) {
            fragment.getPageControl().setTitle(title);
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        fragment.getProgressBar().setProgress(newProgress);
        if (newProgress == 100) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragment.getProgressBar().setVisibility(View.GONE);
                }
            }, 500);
        } else {
            if (fragment.getProgressBar().getVisibility() == View.GONE) {
                fragment.getProgressBar().setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void forwardUrl(final WebView view, final String url) {
        //播打电话
        if (url.startsWith("tel:")) {
            String phoneNo = url.substring(4);
            DeviceUtil.callPhone(fragment.getPageControl().getActivity(), phoneNo);
            return;
        }
        //发短信
        if (url.startsWith("sms:")) {
            String phoneNo = url.substring(4);
            DeviceUtil.sendMsg(fragment.getPageControl().getActivity(), phoneNo);
            return;
        }
        // TODO: 刷新token

        //只支持http或者https协议的页面跳转
        if (url.startsWith("http")) {
            view.loadUrl(url);
        }
        //内部跳转后显示close按钮
        if (fragment.getPageControl().getNbBar() != null) {
            INbControl.ViewHolder viewHolder = fragment.getPageControl().getNbBar().getViewHolder();
            if (!viewHolder.nbLeftIv2.isShown() && !viewHolder.nbLeftTv1.isShown()) {
                viewHolder.nbLeftIv2.setTag("close");
                viewHolder.nbLeftIv2.setVisibility(View.VISIBLE);
                viewHolder.nbLeftIv2.setImageResource(R.mipmap.img_exit_nav_btn);
            }
        }
    }

    @Override
    public void onReceivedError(WebView view, String url, int errorCode, String errorDescription) {
        if (url.endsWith("favicon.ico") && errorCode == 404) {
            //过滤页面图标无法找到的错误，这个错误在6.0+系统的每个页面都会产生，但实际上移动端不需要设置该图标
            return;
        }
        fragment.getProgressBar().setProgress(0);
        fragment.getPageControl().hideLoading();
        //将错误信息回调给前端
        new Callback(String.valueOf(errorCode), fragment.getQuickWebView()).applyNativeError(url, errorDescription);
        //6.0+ajax请求接口非200也会走这边
//        view.loadUrl(WebloaderControl.BLANK);
        if (url.equals(fragment.getQuickBean().pageUrl) || url.equals(fragment.getQuickBean().pageUrl + "/")) {
            //如果当前页面报错则
            fragment.getPageControl().getStatusPage().showStatus(StatusControl.STATUS_PAGE_ERROR);
        }
        cancelTimeKeeper();
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //忽略证书错误
        handler.proceed();
    }

    @Override
    public List<String> getHistoryUrl() {
        return historyUrl;
    }

    @Override
    public boolean hasConfig() {
        return isConfig;
    }

    @Override
    public void setHasConfig(boolean isConfig) {
        this.isConfig = isConfig;
    }

    /**
     * 保存访问记录
     *
     * @param url
     */
    private void addHistoryUrl(String url) {
        if (historyUrl.isEmpty() || !historyUrl.get(historyUrl.size() - 1).equals(url)) {
            historyUrl.add(url);
        }
    }

    /**
     * 设置计时器
     */
    private void setTimeKeeper(final View view) {
        cancelTimeKeeper();
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                timer.purge();
                timer = null;
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        fragment.getPageControl().hideLoading();
                        fragment.getProgressBar().setProgress(0);
                        ((WebView) view).loadUrl(WebloaderControl.BLANK);
                        fragment.getPageControl().getStatusPage().showStatus(StatusControl.STATUS_TIMEOUT_ERROR);
                    }
                });
            }
        }, TIME_OUT, 1);
    }

    /**
     * 取消计时
     */
    private void cancelTimeKeeper() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}
