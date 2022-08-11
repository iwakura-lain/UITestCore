package com.qalain.ui.core.operate.impl;

import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.core.operate.StatusOperate;
import com.qalain.ui.core.strategy.FindStrategyContext;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
public class StatusOperateImpl implements StatusOperate {
    @Resource
    private FindStrategyContext findStrategyContext;

    @Override
    public boolean isEnabled(BaseElement element) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        return webElement.isEnabled();
    }

    @Override
    public boolean isDisplayed(BaseElement element) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        return webElement.isDisplayed();
    }
}
