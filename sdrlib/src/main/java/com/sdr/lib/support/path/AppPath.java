package com.sdr.lib.support.path;

import android.os.Environment;

import com.sdr.lib.SDRLibrary;

import java.io.File;

/**
 * Created by HyFun on 2018/12/18.
 * Email: 775183940@qq.com
 * Description:
 */

public class AppPath {
    // app内部存储位置   更改缓存存储位置 防止华为手机自动清除缓存  用户信息就会被删
    private final static String APP_CACHE = SDRLibrary.getInstance().getApplication().getFilesDir().getAbsolutePath();

    // 存储用户信息的地址
    private final static String USER_INFO_CACHE = APP_CACHE + File.separator + "user";

    // sd卡中应用的缓存地址
    private final static String APP_SD_CACHE = SDRLibrary.getInstance().getApplication().getExternalCacheDir().getAbsolutePath();

    // sd 卡中缓存json的地址
    private final static String DATA_CACHE = APP_SD_CACHE + File.separator + "string";

    // 长期保存的数据  只有卸载才能清除
    private final static String STATIC_CACHE = APP_SD_CACHE + File.separator + "static";

    // ------------------------------------------------------------------------------
    // sd 卡中的路径
    private final static String APP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SDR" + File.separator + SDRLibrary.getInstance().getApplication().getPackageName();

    // sd 卡中的文件
    private final static String FILE_PATH = APP_PATH + File.separator + "file";
    // sd卡中原图片
    private final static String IMAGE_PATH = APP_PATH + File.separator + "image";
    private final static String IMAGE_CACHE_PATH = APP_PATH + File.separator + "image_cache";


    // 存储用户信息的地址
    public static String getUserInfoCache() {
        File file = new File(USER_INFO_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return USER_INFO_CACHE;
    }

    // sd 卡中缓存json的地址
    public static String getDataCache() {
        File file = new File(DATA_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return DATA_CACHE;
    }

    //
    public static String getStaticCache() {
        File file = new File(STATIC_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return STATIC_CACHE;
    }
    // --------------------------------------以下路径需要申请读写sd卡权限----------------------------------------

    //sd 卡中的文件
    public static String getFilePath() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return FILE_PATH;
    }

    // sd卡中原图片
    public static String getImagePath() {
        File file = new File(IMAGE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return IMAGE_PATH;
    }

    // sd卡中的缓存图片
    public static String getImageCachePath() {
        File file = new File(IMAGE_CACHE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return IMAGE_CACHE_PATH;
    }
}
