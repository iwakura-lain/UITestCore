package com.qalain.ui.suite.invoker;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
public class JsInvoker {
    public static void execute(WebDriver driver, String scriptContent) {
        if (StringUtils.isBlank(scriptContent)) {
            return;
        }
        JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
        jsDriver.executeScript(scriptContent);
    }
}
