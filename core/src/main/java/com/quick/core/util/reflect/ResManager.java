package com.quick.core.util.reflect;


/**
 * Created by dailichun on 2017/12/7.
 * 资源文件反射方式获取工具类
 */
public class ResManager {

    /**
     * basecore组件packagename
     */
    public static final String CORE_PACKAGENAME = "com.quick.core";

    /**
     * Quick组件packagename
     */
    public static final String QUICK_PACKAGENAME = "com.quick.jsbridge";

    /**
     * 项目packagename
     */
    public static final String APP_PACKAGENAME = "com.quick.quickhybrid";

    public static final String style = "style";
    public static final String string = "string";
    public static final String id = "id";
    public static final String layout = "layout";
    public static final String drawable = "drawable";
    public static final String mipmap = "mipmap";
    public static final String attr = "attr";
    public static final String anim = "anim";
    public static final String raw = "raw";
    public static final String color = "color";
    public static final String animator = "animator";

    /**
     * 通过反射获取资源id，优先从框架module中的寻找资源
     * 尽量避免大量使用该方法,对性能有影响
     *
     * @param className 资源类型
     * @param name      资源文件名字
     * @return
     */
    public static int getResourseIdByName(String className, String name) {
        int id = getResourseIdByName(CORE_PACKAGENAME, className, name);
        if (id == 0) {
            id = getResourseIdByName(QUICK_PACKAGENAME, className, name);
        }
        if (id == 0) {
            id = getResourseIdByName(APP_PACKAGENAME, className, name);
        }
        if (id == 0) {
            new NoSuchFieldException(name).printStackTrace();
        }
        return id;
    }

    /**
     * 通过反射获取资源id
     *
     * @param packageName manifest中的包名，每个module都有单独的包名,用于指定资源所属的module
     * @param className   资源类型
     * @param name        资源文件名字
     * @return
     */
    public static int getResourseIdByName(String packageName, String className, String name) {
        int id = 0;
        try {
            Class<?> cls = Class.forName(packageName + ".R$" + className);
            if (cls != null) {
                id = cls.getField(name).getInt(className);
            }
        } catch (Exception ignored) {
        }

        return id;
    }

    public static int getLayoutInt(String layoutname) {
        return getResourseIdByName(layout, layoutname);
    }

    public static int getStringInt(String stringname) {
        return getResourseIdByName(string, stringname);
    }

    public static int getIdInt(String idname) {
        return getResourseIdByName(id, idname);
    }

    public static int getDrawableInt(String drawablename) {
        return getResourseIdByName(drawable, drawablename);
    }

    public static int getMipmapInt(String mipmapname) {
        return getResourseIdByName(mipmap, mipmapname);
    }

    public static int getAttrInt(String attrname) {
        return getResourseIdByName(attr, attrname);
    }

    public static int getAnimInt(String animname) {
        return getResourseIdByName(anim, animname);
    }

    public static int getRawInt(String rawname) {
        return getResourseIdByName(raw, rawname);
    }

    public static int getColorInt(String colorname) {
        return getResourseIdByName(color, colorname);
    }

    public static int getStyleInt(String stylename) {
        return getResourseIdByName(style, stylename);
    }

    public static int getAnimatorInt(String animatorname) {
        return getResourseIdByName(animator, animatorname);
    }
}
