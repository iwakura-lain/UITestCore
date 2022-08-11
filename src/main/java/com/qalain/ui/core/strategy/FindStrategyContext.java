package com.qalain.ui.core.strategy;

import com.qalain.ui.constant.ElementLocatorConstant;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lain
 * @Description 元素定位上下文 type
 * @create 2022-01-21
 */
@Component
public class FindStrategyContext implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> ElementFindStrategy<T> getStrategy(Class<T> clazz) {
        return (ElementFindStrategy<T>)applicationContext.getBean(ElementLocatorConstant.DEFAULT_FIND_STRATEGY, ElementFindStrategy.class);
    }
}