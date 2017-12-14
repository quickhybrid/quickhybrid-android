package com.quick.core.util.app;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.quick.core.application.FrmApplication;


/**
 * Created by dailichun on 2017/12/6.
 * application工具类
 */

public class AppUtil {

    public static FrmApplication getApplicationContext() {
        return FrmApplication.getInstance();
    }


    /**
     * ImageLoader组件加载图片默认配置
     * 显示大量的图片，当我们快速滑动GridView，ListView，希望能停止图片的加载时使用如下设置：
     * listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
     * gridView.setOnScrollListener(new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
     *
     * @param loadingId
     * @param failId
     * @param cacheOnMemory
     * @param cacheOnDinsk
     * @return
     */
    public static DisplayImageOptions getImageLoaderOptions(int loadingId, int failId, boolean cacheOnMemory, boolean cacheOnDinsk) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingId) //如果不需要设置则传0，设置该值刷新的时候会有闪烁现象
                .showImageForEmptyUri(failId) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(failId) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(cacheOnMemory) // default  设置下载的图片是否缓存在内存中
                .cacheOnDisk(cacheOnDinsk) // default  设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565) // default 设置图片的解码类型
//                .resetViewBeforeLoading(true)  // default 设置图片在加载前是否重置、复位
//                .delayBeforeLoading(1000)  // 下载前的延迟时间
//                .preProcessor(...)
//                .postProcessor(...)
//                .extraForDownloader(...)
//                .considerExifParams(false) // default
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default 设置图片以如何的编码方式显示
//                .bitmapConfig(Bitmap.Config.RGB_565) // default 设置图片的解码类型
//                .decodingOptions(...)  // 图片的解码设置
//                .displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
//                .handler(new Handler()) // default
                .build();
        return options;
    }

    /**
     * ImageLoader组件加载图片默认配置
     *
     * @param loadingId
     * @return
     */
    public static DisplayImageOptions getImageLoaderOptions(int loadingId) {
        return getImageLoaderOptions(loadingId, loadingId, true, true);
    }
}
