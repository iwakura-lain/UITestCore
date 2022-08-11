package com.qalain.ui.util;

import com.qalain.ui.core.page.Page;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
public class ScrollUtil {
    static WebDriver driver;

    /**
     * 滑动
     * 输入参数：滚动条所在弹出框的页面对象
     * @param page
     * @param pixel
     */
    public static void down(Page page, int pixel){
        driver = page.getEngine().getThreadLocalDriver();
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,"+pixel+")");
        ThreadUtil.sleep(2000);
    }

    public static void down(WebDriver driver, int pixel) {
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(0," + pixel + ")", new Object[0]);
        ThreadUtil.sleep(2000L);
    }
}
