package com.qalain.ui.core.operate.impl;

import ch.qos.logback.core.util.TimeUtil;
import com.qalain.ui.constant.SuiteConstant;
import org.openqa.selenium.interactions.Actions;
import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.core.operate.HoverOperate;
import com.qalain.ui.core.strategy.FindStrategyContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import org.testng.util.TimeUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
public class HoverOperateImpl implements HoverOperate {

    @Resource
    private FindStrategyContext findStrategyContext;

    @Resource
    private EngineDriver engine;

    @Override
    public void hover(BaseElement element, long timeMills) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        WebDriver driver = engine.getThreadLocalDriver();
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).perform();
        try {
            TimeUnit.MILLISECONDS.sleep(timeMills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
