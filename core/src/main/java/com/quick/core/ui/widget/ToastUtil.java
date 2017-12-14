package com.quick.core.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by dailichun on 2017/12/7.
 * 提示信息工具类
 */
public class ToastUtil {

    public static void toastShort(Context context, String message) {
        if (TextUtils.isEmpty(message) || context==null) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String message) {
        if (TextUtils.isEmpty(message) || context==null) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
