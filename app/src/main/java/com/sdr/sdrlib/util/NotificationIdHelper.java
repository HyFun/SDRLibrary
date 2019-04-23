package com.sdr.sdrlib.util;

/**
 * Created by Administrator on 2018/5/14.
 * <p>
 * 获取notification id 的统一类
 */

public final class NotificationIdHelper {
    private static final int DEFAULT_NOTIFY_ID = 10;

    private static NotificationIdHelper notificationIdHelper;

    // 通知栏最多显示20个通知吧
    private int notifyId = DEFAULT_NOTIFY_ID;

    private NotificationIdHelper() {
    }

    public static NotificationIdHelper getInstance() {
        if (notificationIdHelper == null) {
            synchronized (NotificationIdHelper.class) {
                if (notificationIdHelper == null)
                    notificationIdHelper = new NotificationIdHelper();
            }
        }
        return notificationIdHelper;
    }

    public final int getNotifyId() {
        if (notifyId > (DEFAULT_NOTIFY_ID + 20)) {
            notifyId = DEFAULT_NOTIFY_ID;
        } else {
            notifyId++;
        }
        return notifyId;
    }
}
