package com.quick.core.ui.app;

import java.util.List;

/**
 * Created by dailichun on 2017/12/7.
 * 主题设置接口，只要对导航栏以及状态栏的全局控制
 */
public interface IThemeControl {

    /**
     * 初始化可选主题
     *
     * @param themeList
     */
    void initTheme(List<ThemeBean> themeList);

    /**
     * 设置当前主题id
     *
     * @param id
     */
    void setThemeId(String id);

    /**
     * 获取当前主题id
     */
    String getThemeId();

    /**
     * 获取所有可选主题
     *
     * @return
     */
    List<ThemeBean> getTheme();

    /**
     * 获取当前主题
     *
     * @return
     */
    ThemeBean getSelectedTheme();

    /**
     * 获取当前用户保存主题的key值
     *
     * @return
     */
    String getThemeKey();

}
