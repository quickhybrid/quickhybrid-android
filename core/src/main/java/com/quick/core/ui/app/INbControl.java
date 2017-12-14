package com.quick.core.ui.app;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quick.core.ui.widget.DrawableText;
import com.quick.core.ui.widget.NbImageView;
import com.quick.core.ui.widget.NbTextView;

/**
 * Created by dailichun on 2017/12/6.
 * 导航栏操作接口。所有的导航栏控制器都必须实现该接口。
 */
public interface INbControl {

    /**
     * 获取导航栏根布局
     *
     * @return
     */
    View getRootView();

    /**
     * 隐藏导航栏
     */
    void hide();

    /**
     * 显示导航栏
     */
    void show();

    /**
     * 隐藏边线
     */
    void hideLine();

    /**
     * 隐藏返回按钮
     */
    void hideNbBack();

    /**
     * 显示返回按钮
     */
    void showNbBack();

    /**
     * 设置最左侧按钮背景图片
     *
     * @param resid
     */
    void setNbBackImage(Object resid);

    /**
     * 设置导航栏背景图或者颜色
     *
     * @param bg
     */
    void setNbBackground(Object bg);

    /**
     * 设置导航栏过滤色,除了标题的图片和文字颜色都会被强制改为过滤色
     *
     * @param filterColor
     */
    void setColorFilter(Object filterColor);

    /**
     * 设置主题
     *
     * @param bean
     */
    void setTheme(ThemeBean bean);

    /**
     * 设置导航栏标题
     *
     * @param title 标题
     */
    void setNbTitle(String title);

    /**
     * 设置导航栏标题
     *
     * @param title  标题
     * @param title2 副标题
     */
    void setNbTitle(String title, String title2);

    /**
     * 设置导航栏自定义标题布局
     *
     * @param view
     */
    void addNbCustomTitleView(View view);

    /**
     * 设置标题是否可点击
     *
     * @param clickable 是否可点击
     * @param arrow     箭头图标
     */
    void setTitleClickable(boolean clickable, int arrow);

    /**
     * 获取控件对象集合
     *
     * @return
     */
    ViewHolder getViewHolder();

    /**
     * 获取搜索条件
     * @return
     */
    String getCondition();

    class ViewHolder {

        //最左侧图标按钮，一般是返回按钮
        public NbImageView nbBack;

        //最左侧图文按钮
        public DrawableText nbLeftTv1;

        //左侧图标按钮，在nbBack右侧
        public NbImageView nbLeftIv2;

        //左侧文字按钮，在nbBack右侧
        public NbTextView nbLeftTv2;

        //右侧图标按钮,框架默认4个
        public NbImageView[] nbRightIvs;

        //最右侧文字按钮,框架默认2个
        public NbTextView[] nbRightTvs;

        //标题父控件
        public View titleParent;

        //主标题
        public TextView nbTitle;

        //副标题
        public TextView nbTitle2;

        //标题箭头，可点击时展示
        public ImageView ivTitleArrow;

        //自定义标题父控件
        public FrameLayout nbCustomTitleLayout;

        //线
        public LinearLayout line;

        //导航栏根布局
        public View nbRoot;

    }

    interface INbOnClick {

        void onNbBack();

        void onNbLeft(View view);

        void onNbRight(View view, int which);

        void onNbTitle(View view);

        void onNbSearch(String keyWord);

        void onNbSearchClear();
    }
}
