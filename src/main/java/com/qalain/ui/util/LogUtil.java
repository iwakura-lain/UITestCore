package com.qalain.ui.util;

import lombok.extern.slf4j.Slf4j;
import org.testng.Reporter;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Slf4j
public class LogUtil {
    /**
     * 记录info日志
     * @param message
     * @param param
     */
    public static void info(String message, Object... param) {
        log.info(message, param);
        message = message.replaceAll("\\{\\}", "%s");
        Reporter.log(String.format(message, param), -1);
    }
}
