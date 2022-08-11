package com.qalain.ui.core.operate.impl;

import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.core.operate.CheckOperate;
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
public class CheckOperateImpl implements CheckOperate{

    @Resource
    private CheckOperate checkBoxOperate;

    @Resource
    private FindStrategyContext findStrategyContext;

    @Override
    public void checkByText(BaseElement element, String text) {
        checkByValue(element, text);
    }

    @Override
    public void checkByValue(BaseElement element, String value) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        if (!webElement.isSelected()) {
            webElement.click();
        }
    }
}
