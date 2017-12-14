package com.quick.jsbridge.api;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quick.core.ui.app.INbControl;
import com.quick.core.ui.widget.DrawableText;
import com.quick.core.ui.widget.segbar.ActionBarSeg;
import com.quick.core.ui.widget.segbar.SegActionCallBack;
import com.quick.core.util.app.AppUtil;
import com.quick.core.util.common.JsonUtil;
import com.quick.core.util.device.DeviceUtil;
import com.quick.jsbridge.bridge.Callback;
import com.quick.jsbridge.bridge.IBridgeImpl;
import com.quick.jsbridge.control.AutoCallbackDefined;
import com.quick.jsbridge.view.IQuickFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import quick.com.jsbridge.R;


/**
 * Created by dailichun on 2017/12/6.
 */
public class NavigatorApi implements IBridgeImpl {

    /**
     * 注册API的别名
     */
    public static String RegisterName = "navigator";

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
     * 隐藏导航栏
     */
    public static void hide(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        webLoader.getPageControl().getNbBar().hide();
        callback.applySuccess();
    }

    /**
     * 显示导航栏
     */
    public static void show(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        webLoader.getPageControl().getNbBar().show();
        callback.applySuccess();
    }

    /**
     * 显示状态栏
     */
    public static void showStatusBar(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        DeviceUtil.setStatusBarVisibility(webLoader.getPageControl().getActivity(), true);
        callback.applySuccess();
    }

    /**
     * 隐藏状态栏
     */
    public static void hideStatusBar(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        DeviceUtil.setStatusBarVisibility(webLoader.getPageControl().getActivity(), false);
        callback.applySuccess();
    }

    /**
     * 隐藏导航栏返回按钮,只能在首页使用
     */
    public static void hideBackBtn(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        webLoader.getPageControl().getNbBar().hideNbBack();
        callback.applySuccess();
    }

    /**
     * 监听系统返回按钮
     */
    public static void hookSysBack(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnClickBack, callback.getPort());
    }

    /**
     * 监听导航栏返回按钮
     */
    public static void hookBackBtn(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {

        webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnClickNbBack, callback.getPort());
    }

    /**
     * 设置标题
     * <p>
     * 参数：
     * title:   标题
     * subTitle:副标题
     * clickable：是否可点击，1：是，并且显示小箭头 其他：否
     * direction：箭头方向 top：向上 bottom：向下
     */
    public static void setTitle(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        String title = param.optString("title");
        String subTitle = param.optString("subTitle");
        boolean clickable = "1".equals(param.optString("clickable", "0"));
        String direction = param.optString("direction", "bottom");
        webLoader.getPageControl().getNbBar().getViewHolder().nbCustomTitleLayout.removeAllViews();
        webLoader.getPageControl().getNbBar().getViewHolder().titleParent.setVisibility(View.VISIBLE);
        webLoader.getPageControl().getNbBar().setNbTitle(title, subTitle);
        if ("bottom".equals(direction)) {
            webLoader.getPageControl().getNbBar().setTitleClickable(clickable, R.mipmap.img_arrow_black_down);
        } else {
            webLoader.getPageControl().getNbBar().setTitleClickable(clickable, R.mipmap.img_arrow_black_up);
        }
        if (clickable) {
            webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnClickNbTitle, callback.getPort());
        } else {
            webLoader.getWebloaderControl().removePort(AutoCallbackDefined.OnClickNbTitle);
        }
    }

    /**
     * 设置多标题
     * <p>
     * 参数：
     * titles:  标题,数组
     */
    public static void setMultiTitle(final IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        JSONArray itemsJsonObject = param.optJSONArray("titles");
        String[] titles = null;
        titles = JsonUtil.parseJSONArray(itemsJsonObject, titles);
        webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnClickNbTitle, callback.getPort());
        webLoader.getPageControl().getNbBar().getViewHolder().ivTitleArrow.setVisibility(View.GONE);
        webLoader.getPageControl().getNbBar().getViewHolder().nbCustomTitleLayout.removeAllViews();
        webLoader.getPageControl().getNbBar().addNbCustomTitleView(new ActionBarSeg(webLoader.getPageControl().getActivity(), titles, new SegActionCallBack() {

            @Override
            public void segAction(int i) {
                webLoader.getWebloaderControl().autoCallbackEvent.onClickNbTitle(i);
            }
        }).create());
    }

    /**
     * 设置导航栏最右侧按钮
     * <p>
     * 参数：
     * isShow：是否显示，0：隐藏 其他：显示
     * text：文字按钮
     * imageUrl:图片按钮，格式为url地址,优先级高
     */
    public static void setRightBtn(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        int which = param.optInt("which", 0);
        boolean isShow = !"0".equals(param.optString("isShow"));
        String text = param.optString("text");
        String imageUrl = param.optString("imageUrl");
        INbControl.ViewHolder holder = webLoader.getPageControl().getNbBar().getViewHolder();
        if (isShow) {
            if (!TextUtils.isEmpty(imageUrl)) {
                //设置图片
                holder.nbRightIvs[which].setVisibility(View.INVISIBLE);
                holder.nbRightTvs[which].setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(imageUrl, holder.nbRightIvs[which], AppUtil.getImageLoaderOptions(0, 0, true, true));
                holder.nbRightIvs[which].setVisibility(View.VISIBLE);
            } else {
                //设置文字
                holder.nbRightIvs[which].setVisibility(View.INVISIBLE);
                holder.nbRightTvs[which].setVisibility(View.VISIBLE);
                holder.nbRightTvs[which].setText(text);
            }
            webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnClickNbRight + which, callback.getPort());
        } else {
            //隐藏按钮
            holder.nbRightTvs[which].setVisibility(View.INVISIBLE);
            holder.nbRightIvs[which].setVisibility(View.INVISIBLE);
            webLoader.getWebloaderControl().removePort(AutoCallbackDefined.OnClickNbRight + which);
        }
    }

    /**
     * 设置导航栏左侧按钮
     * <p>
     * 参数：
     * isShow：是否显示，0：隐藏 其他：显示
     * text：文字按钮
     * imageUrl:图片按钮，格式为url地址,优先级高
     * isShowArrow:是否显示箭头，最左侧文字才能显示，会替换返回按钮 1:是 其他：否
     * direction：箭头方向 top：向上 bottom：向下
     */
    public static void setLeftBtn(IQuickFragment webLoader, WebView wv, JSONObject param, Callback callback) {
        boolean isShow = !"0".equals(param.optString("isShow"));
        String text = param.optString("text");
        String imageUrl = param.optString("imageUrl");
        boolean isShowArrow = "1".equals(param.optString("isShowArrow", "0"));
        String direction = param.optString("direction", "bottom");
        INbControl.ViewHolder holder = webLoader.getPageControl().getNbBar().getViewHolder();
        if (isShow) {
            if (!TextUtils.isEmpty(imageUrl)) {
                //设置图片
                holder.nbLeftIv2.setVisibility(View.GONE);
                holder.nbLeftTv2.setVisibility(View.GONE);
                holder.nbLeftTv1.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(imageUrl, holder.nbLeftIv2, AppUtil.getImageLoaderOptions(0, 0, true, true));
                holder.nbLeftIv2.setVisibility(View.VISIBLE);
            } else {
                //设置文字
                if (isShowArrow) {
                    holder.nbBack.setVisibility(View.GONE);
                    holder.nbLeftTv1.setText(text);
                    if ("bottom".equals(direction)) {
                        holder.nbLeftTv1.setDrawable(R.mipmap.img_arrow_blue_down, DrawableText.DIRECTION_RIGHT);
                    } else {
                        holder.nbLeftTv1.setDrawable(R.mipmap.img_arrow_blue_up, DrawableText.DIRECTION_RIGHT);
                    }
                    holder.nbLeftTv1.setVisibility(View.VISIBLE);
                    holder.nbLeftIv2.setVisibility(View.GONE);
                } else {
                    holder.nbLeftIv2.setVisibility(View.GONE);
                    holder.nbLeftTv2.setVisibility(View.VISIBLE);
                    holder.nbLeftTv2.setText(text);
                }
            }
            webLoader.getWebloaderControl().addPort(AutoCallbackDefined.OnClickNbLeft, callback.getPort());
        } else {
            //隐藏按钮
            holder.nbLeftTv2.setVisibility(View.GONE);
            holder.nbLeftIv2.setVisibility(View.GONE);
            holder.nbLeftTv1.setVisibility(View.GONE);
            webLoader.getWebloaderControl().removePort(AutoCallbackDefined.OnClickNbLeft);
        }
    }

}
