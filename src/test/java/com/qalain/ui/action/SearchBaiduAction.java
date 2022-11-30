package com.qalain.ui.action;

import com.google.common.collect.ImmutableMap;
import com.qalain.ui.config.EngineConfig;
import com.qalain.ui.constant.AutoTestConstant;
import com.qalain.ui.core.AutoTestProcessor;
import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.core.engine.EngineProperties;
import com.qalain.ui.core.page.Page;
import com.qalain.ui.suite.action.ICustomAction;
import com.qalain.ui.suite.entity.FlowStep;
import com.qalain.ui.util.SeleniumUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v107.network.Network;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Service("baiduSearch")
@Slf4j
public class SearchBaiduAction implements ICustomAction {


    @Override
    public void execute(EngineDriver engineDriver, FlowStep flowStep, WebElement webElement) {
        if (webElement == null) {
            log.error("元素不存在");
        }

        ChromeDriver webDriver = (ChromeDriver)engineDriver.getThreadLocalDriver();
        DevTools devTools = webDriver.getDevTools();
        devTools.createSession();
        devTools.send(new Command<>("Network.enable", ImmutableMap.of()));;
        devTools.addListener(Network.responseReceived(), l -> {
            log.info("Response URL: ");
            log.info(l.getResponse().getUrl());
        });
        devTools.addListener(Network.requestWillBeSent(), l -> {
            log.info("Request URL: ");
            log.info(l.getRequest().getUrl());
        });

        //截图
        String screenPath = EngineProperties.get(EngineConfig.SCREENSHOT_PATH);
        String destPath = screenPath + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".jpg";
        SeleniumUtil.screenshot(webDriver, destPath);
        log.info("截图成功");
        WebElement searchText = webDriver.findElement(By.id("kw"));
        searchText.clear();
        searchText.sendKeys("spring");
    }
}
