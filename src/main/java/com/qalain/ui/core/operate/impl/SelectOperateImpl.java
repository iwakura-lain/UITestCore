package com.qalain.ui.core.operate.impl;

import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.core.operate.SelectOperate;
import com.qalain.ui.core.strategy.FindStrategyContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
public class SelectOperateImpl implements SelectOperate {
    @Resource
    private FindStrategyContext findStrategyContext;

    @Override
    public void selectByText(BaseElement element, String text) {
        createSelectElement(element).selectByVisibleText(text);
    }

    @Override
    public void selectByValue(BaseElement element, String value) {
        createSelectElement(element).selectByValue(value);
    }

    @Override
    public void selectByIndex(BaseElement element, int index) {
        createSelectElement(element).selectByIndex(index);
    }

    @Override
    public void deselectByText(BaseElement element, String text) {
        createSelectElement(element).deselectByVisibleText(text);
    }

    @Override
    public void deselectByValue(BaseElement element, String value) {
        createSelectElement(element).deselectByValue(value);
    }

    @Override
    public void deselectByIndex(BaseElement element, int index) {
        createSelectElement(element).deselectByIndex(index);
    }

    @Override
    public void deselectAll(BaseElement element) {
        createSelectElement(element).deselectAll();
    }

    /**
     * 创建Select元素对象
     * @param element 操作元素
     * @return
     */
    private Select createSelectElement(BaseElement element) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        return new Select(webElement);
    }
}
