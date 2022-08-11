package com.qalain.ui.core.operate.impl;

import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.core.operate.ClickOperate;
import com.qalain.ui.core.strategy.FindStrategyContext;
import com.qalain.ui.util.ThreadUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
public class ClickOperateImpl implements ClickOperate {
    @Resource
    private FindStrategyContext findStrategyContext;

    @Resource
    private EngineDriver engine;

    @Override
    public void click(BaseElement element) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        webElement.click();
        ThreadUtil.sleep(element.getWaitTime());
    }

    @Override
    public void doubleClick(BaseElement element) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        Actions actions = new Actions(engine.getThreadLocalDriver());
        actions.doubleClick(webElement);
        ThreadUtil.sleep(element.getWaitTime());
    }
}
