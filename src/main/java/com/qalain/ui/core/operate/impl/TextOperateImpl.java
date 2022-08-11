package com.qalain.ui.core.operate.impl;

import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.core.operate.TextOperate;
import com.qalain.ui.core.strategy.FindStrategyContext;
import com.qalain.ui.util.ThreadUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
public class TextOperateImpl implements TextOperate {
    @Resource
    private FindStrategyContext findStrategyContext;

    @Override
    public Object getTextValue(BaseElement element) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        return webElement.getText();
    }

    @Override
    public void setTextValue(BaseElement element, Object value) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        webElement.clear();
        webElement.sendKeys(StringUtils.defaultIfEmpty(value.toString(), StringUtils.EMPTY));
        ThreadUtil.sleep(element.getWaitTime());
    }
}
