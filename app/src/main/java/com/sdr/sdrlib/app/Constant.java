package com.sdr.sdrlib.app;

import java.text.SimpleDateFormat;

/**
 * Created by HyFun on 2019/04/29.
 * Email: 775183940@qq.com
 * Description:
 */

public interface Constant {
    interface Date {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat FORMAT_YEAR_MON_DAY = new SimpleDateFormat("yyyy-MM-dd");
    }
}
