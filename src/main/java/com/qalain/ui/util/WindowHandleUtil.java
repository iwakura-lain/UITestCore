package com.qalain.ui.util;

import org.openqa.selenium.WebDriver;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
public class WindowHandleUtil {
    public static String changeWindow(WebDriver driver) {
        if (driver == null) {
            return null;
        }
        String currentWindow = driver.getWindowHandle();
        for (String window : driver.getWindowHandles()) {
            if (!window.equals(currentWindow)) {
                return window;
            }
        }
        return currentWindow;
    }

    public static void switchWindow(WebDriver driver) {
        if (driver == null) {
            return;
        }
        String currentWindow = driver.getWindowHandle();
        String switchWindow = currentWindow;
        for (String window : driver.getWindowHandles()) {
            if (!window.equals(currentWindow)) {
                switchWindow = window;
            }
        }
        driver.switchTo().window(switchWindow);
    }
}
