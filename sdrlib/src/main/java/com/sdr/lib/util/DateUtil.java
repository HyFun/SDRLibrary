package com.sdr.lib.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by HyFun on 2019/05/15.
 * Email: 775183940@qq.com
 * Description: 日期工具类
 */

public class DateUtil {
    public static final String[] WEEKS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    public static final SimpleDateFormat DATE_FORMAT_Y_M_D_H_M_S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_Y_M_D = new SimpleDateFormat("yyyy-MM-dd");

    private DateUtil() {
    }

    /**
     * 格式化时间
     * 时间转换方法，转换成 今天、昨天、前天
     * @param time
     * @return
     */
    public static final String format(long time) {
        // 当前时间的calendar
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        currentCalendar.setTimeInMillis(currentCalendar.getTimeInMillis() / 1000 * 1000);
        // 传过来时间的calendar
        Calendar createCalendar = Calendar.getInstance();
        createCalendar.setTimeInMillis(time);
        // 年
        String year = (currentCalendar.get(Calendar.YEAR) == createCalendar.get(Calendar.YEAR) ? "" : createCalendar.get(Calendar.YEAR) + "年");
        // 月  日
        String month2day = String.format("%02d", createCalendar.get(Calendar.MONTH) + 1) + "月" + String.format("%02d", createCalendar.get(Calendar.DAY_OF_MONTH)) + "日 ";
        // 时  分
        String hour_min = String.format("%02d", createCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", createCalendar.get(Calendar.MINUTE));
        Calendar createDateCalendar = Calendar.getInstance();
        createDateCalendar.clear();
        createDateCalendar.set(createCalendar.get(Calendar.YEAR), createCalendar.get(Calendar.MONTH), createCalendar.get(Calendar.DAY_OF_MONTH));
        long differ = Math.abs(currentCalendar.getTimeInMillis() - createDateCalendar.getTimeInMillis());
        int days = (int) (differ / (24l * 60 * 60 * 1000));
        if (days == 0) {
            return "今天 " + hour_min;
        } else if (days == 1) {
            return "昨天 " + hour_min;
        } else if (days == 2) {
            return "前天 " + hour_min;
        } else {
            return year + month2day + hour_min;
        }
    }


}
