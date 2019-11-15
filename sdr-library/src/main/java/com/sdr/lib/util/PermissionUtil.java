package com.sdr.lib.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by HyFun on 2019/11/11.
 * Email: 775183940@qq.com
 * Description: 检测权限并引导开启的
 */
public class PermissionUtil {
    public static class Check {

        /**
         * 判断是否有悬浮窗权限
         *
         * @param context
         * @return
         */
        public static boolean haveFloatPermission(Context context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                return true;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                try {
                    Class cls = Class.forName("android.content.Context");
                    Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                    declaredField.setAccessible(true);
                    Object obj = declaredField.get(cls);
                    if (!(obj instanceof String)) {
                        return false;
                    }
                    String str2 = (String) obj;
                    obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                    cls = Class.forName("android.app.AppOpsManager");
                    Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                    declaredField2.setAccessible(true);
                    Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                    int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                    return result == declaredField2.getInt(cls);
                } catch (Exception e) {
                    return false;
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                    if (appOpsMgr == null)
                        return false;
                    int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                            .getPackageName());
                    return Settings.canDrawOverlays(context) || mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
                } else {
                    return Settings.canDrawOverlays(context);
                }
            }
        }
    }

    /**
     * 跳转授权页面
     */
    public static class Navigate {
        public static void navigateToFloat(Context context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return;
            }
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
