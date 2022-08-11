package com.qalain.ui.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lain
 * @Description
 * @create 2022-01-22
 */
@Slf4j
public class ThreadUtil {

    /**
     * 睡眠millisecond毫秒
     *
     * @param millisecond 毫秒
     */
    public static void sleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            log.error("等待睡眠时报错", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    private ThreadUtil() {}
}