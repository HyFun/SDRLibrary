package com.sdr.lib.support;

import android.app.Activity;

import java.util.HashSet;
import java.util.Set;

/**
 * 一键退出App
 *
 * @author quchao
 * @date 2017/11/28
 */

public class ActivityCollector {

    private ActivityCollector() {
    }

    private static ActivityCollector activityCollector;

    public synchronized static ActivityCollector getInstance() {
        if (activityCollector == null) {
            activityCollector = new ActivityCollector();
        }
        return activityCollector;
    }

    private Set<Activity> allActivities = new HashSet<>();

    /**
     * 添加一个activity
     * 在onCreate中调用
     *
     * @param act
     */
    public void addActivity(Activity act) {
        if (!allActivities.contains(act)) {
            allActivities.add(act);
        }
    }

    /**
     * 移除一个activity
     * 在onDestory中调用
     *
     * @param act
     */
    public void removeActivity(Activity act) {
        if (allActivities != null && allActivities.contains(act)) {
            allActivities.remove(act);
        }
    }

    /**
     * 关闭所有的activity
     */
    public void closeAllActivities() {
        if (allActivities == null) return;
        for (Activity activity : allActivities) {
            activity.finish();
        }
        allActivities.clear();
    }

    /**
     * 强制退出
     */

    public void forceExit() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
