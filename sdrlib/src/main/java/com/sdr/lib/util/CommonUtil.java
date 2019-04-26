package com.sdr.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.sdr.lib.ui.viewbigimage.ViewBigImageActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HyFun on 2018/10/11.
 * Email:775183940@qq.com
 */

public class CommonUtil {
    private CommonUtil() {
    }

    /**
     * 获取应用包信息
     *
     * @param context
     * @return
     */
    public static final PackageInfo getPackageInfo(Context context) {
        PackageManager manager = context.getPackageManager();

        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 1);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    /**
     * 将dp转换成系统使用的px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return 转换后的px值
     */
    public final static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 判断网络是否连通
     */
    public static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                @SuppressWarnings("static-access")
                ConnectivityManager cm = (ConnectivityManager) context
                        .getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                return info != null && info.isConnected();
            } else {
                /**如果context为空，就返回false，表示网络未连接*/
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 跳转到app安装的页面
     *
     * @param context
     * @param file
     */
    public final static void installApk(Context context, File file) {
        String fileName = file.getName();
        String stuff = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!"apk".equals(stuff.toLowerCase())) return;
        // 判断是否为8.0授权了没有
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                Toast.makeText(context, "请授权该应用的未知来源", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, getPackageInfo(context).packageName + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static final int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static final int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 设置背景透明度
     *
     * @param bgAlpha
     * @param activity
     */
    public final static void setTransformBg(float bgAlpha, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }


    /**
     * 查看多张图片
     *
     * @param context
     * @param isLocal
     * @param position
     * @param imageList
     */
    public static void viewImageList(Context context, boolean isLocal, int position, List imageList) {
        ViewBigImageActivity.startImageList(context, isLocal, position, imageList);
    }

    /**
     * 查看一张图片
     *
     * @param context
     * @param image
     */
    public static void viewImage(Context context, boolean isLocal, Object image) {
        List imageList = new ArrayList<>();
        imageList.add(image);
        viewImageList(context, isLocal, 0, imageList);
    }


    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
