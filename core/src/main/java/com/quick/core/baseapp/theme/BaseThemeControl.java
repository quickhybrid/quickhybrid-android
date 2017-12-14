package com.quick.core.baseapp.theme;

import com.quick.core.ui.app.IThemeControl;
import com.quick.core.ui.app.ThemeBean;

import java.util.ArrayList;
import java.util.List;

import quick.com.core.R;


/**
 * Created by dailichun on 2017/12/7.
 * 主题配置类
 */
public class BaseThemeControl implements IThemeControl {

    private List<ThemeBean> themes;

    private static BaseThemeControl ourInstance;

    public static BaseThemeControl getInstance() {
        if (ourInstance == null) {
            ourInstance = new BaseThemeControl();
        }
        return ourInstance;
    }

    /**
     * 获取框架默认提供的主题,也可以根据需求自己定义
     *
     * @return
     */
    public static List<ThemeBean> getDefaultThemes() {

        List<ThemeBean> themes = new ArrayList<>();

        ThemeBean defaultItem = new ThemeBean();
        defaultItem.themeId = "theme_default_blue";
        defaultItem.topbarImage = R.color.white;
        defaultItem.topbarBackImage = R.mipmap.img_back_nav_btn;
        defaultItem.topbarFilterColor = R.color.nbbar_bg_blue;
        themes.add(defaultItem);

        return themes;
    }

    /**
     * 初始化设置指定主题
     *
     * @param themeList
     */
    @Override
    public void initTheme(List<ThemeBean> themeList) {
        if (themeList != null && themeList.size() > 0) {
            themes = themeList;
            setThemeId(themes.get(0).themeId);
        }
    }

    /**
     * 设置第一个主题为当前用户选择的主题
     */
    @Override
    public void setThemeId(String id) {

        // 可以保持数据库，默认不做
    }

    @Override
    public String getThemeId() {

        // 默认就是默认主题
        return "theme_default_blue";
    }

    /**
     * 获取设置的主题
     *
     * @return
     */
    @Override
    public List<ThemeBean> getTheme() {
        return themes;
    }

    /**
     * 获取本用户保存主题的key值
     *
     * @return
     */
    @Override
    public String getThemeKey() {
        return "theme_default_blue";
    }

    /**
     * 获取当前主题
     *
     * @return
     */
    @Override
    public ThemeBean getSelectedTheme() {
        if (themes == null) {
            initTheme(getDefaultThemes());
        }
        String themeId = "theme_default_blue";
        ThemeBean cuTheme = themes.get(0);
        for (ThemeBean theme : themes) {
            if (theme.themeId.equals(themeId)) {
                cuTheme = theme;
            }
        }
        return cuTheme;
    }

}
