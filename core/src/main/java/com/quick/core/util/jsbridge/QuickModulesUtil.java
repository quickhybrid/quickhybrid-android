package com.quick.core.util.jsbridge;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by dailichun on 2017/12/18.
 */

public class QuickModulesUtil {
    // 存储属性
    static Properties props = null;

    public static String getProperties(Context c, String proName){
        if (props == null) {
            try{
                props = new Properties();
                // 方法一：通过activity中的context攻取setting.properties的FileInputStream
                InputStream in = c.getAssets().open("modules.properties");
                props.load(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String value = "";

        if (props != null) {
            value = props.getProperty(proName);
        }

        if (value != null) {
            return  value;
        }

        return "";
    }
}
