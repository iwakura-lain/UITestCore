package com.qalain.ui.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Slf4j
public class TimeUtil {

    public static enum TimeUnit {
        DAY("day"),
        HOUR("hour"),
        MINUTE("minute"),
        SECOND("second");

        private String value;

        TimeUnit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     * 在当前系统时间之上增加相应时间
     *
     * @param time    增加的时间数值
     * @param unit    时间单位
     * @param pattern 返回的时间字符串格式
     * @return 更改后的时间字符串
     */
    public static String getModifyTimeStr(int time, TimeUnit unit, String pattern) {
        String result = DateFormatUtils.format(getModifyDate(time, unit), pattern);
        log.info("modify time is:{}", time);
        return result;
    }

    public static String getModifyTimeStr(int time, TimeUnit unit) {
        return getModifyTimeStr(time, unit, TIME_PATTERN);
    }

    public static Date getModifyDate(int time, TimeUnit unit) {
        Date currentDate = new Date();
        if (time == 0 || unit == null) {
            return currentDate;
        }
        Date result = currentDate;
        switch (unit) {
            case DAY:
                result = DateUtils.addDays(currentDate, time);
                break;
            case HOUR:
                result = DateUtils.addHours(currentDate, time);
                break;
            case MINUTE:
                result = DateUtils.addMinutes(currentDate, time);
                break;
            case SECOND:
                result = DateUtils.addSeconds(currentDate, time);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 获取当前时间
     * @return 当前时间
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
}
