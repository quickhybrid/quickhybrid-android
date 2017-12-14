package com.quick.jsbridge.bridge;

/**
 * Created by dailichun on 2017/12/6.
 * 约束JSBridge.register方法第二个参数必须是该接口的实现类.
 *
 * API的定义必须满足以下条件：
 * 1.实现IBridgeImpl
 * 2.方法必须是public static类型
 * 3.固定4个参数QuickFragment，WebView，JSONObject，Callback
 * 4.回调统一采用 callback.apply(),参数通过JSBridge.getResponse()获取
 * 注意：
 * 耗时操作在多线程中实现
 * UI操作在主线程实现
 * 所有API不管成功与否只进行一次回调
 * 长期以及延时回调事件只在触发事件时回调
 */

public interface IBridgeImpl {
}
