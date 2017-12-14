package com.quick.core.util.device;

import android.content.Context;


/**
 * Created by dailichun on 2017/12/7.
 * 分辨率转化工具类
 */
public class DensityUtil {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context con, float dpValue) {
		final float scale = con.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  
	} 

	/** 
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	 */  
	public static int px2dip(Context con, float pxValue) {
		final float scale = con.getResources().getDisplayMetrics().density;  
		return (int) (pxValue / scale + 0.5f);  
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * @param pxValue
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context con, float pxValue) {
		final float fontScale = con.getResources().getDisplayMetrics().scaledDensity;  
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * @param spValue
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context con, float spValue) {
		final float fontScale = con.getResources().getDisplayMetrics().scaledDensity;  
		return (int) (spValue * fontScale + 0.5f);
	}
}
