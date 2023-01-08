package com.qalain.ui.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Slf4j
public class SeleniumUtil {

    /**
     * 截图操作
     *
     * @param driver   driver引擎
     * @param destPath 截图目标路径
     */
    public static void screenshot(WebDriver driver, String destPath) {
        if (driver == null) {
            return;
        }
        //new File(destPath).deleteOnExit();
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File srcFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(destPath));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取/字符在字符串传中第n次出现的位置
     * @param str 操作的字符串
     * @param n 第n次
     * @return 第n次出现/字符的下标
     */
    public static int getCharacterPosition(String str, int n) {
        Matcher slashMatcher = pattern.matcher(str);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == n) {
                break;
            }
        }
        return slashMatcher.start();
    }

    private static final Pattern pattern = Pattern.compile("/");
}

