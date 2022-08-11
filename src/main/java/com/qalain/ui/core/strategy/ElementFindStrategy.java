package com.qalain.ui.core.strategy;

import com.qalain.ui.core.entity.ui.BaseElement;

/**
 * @author lain
 * @Description 元素查找策略接口
 * @create 2022-01-21
 */
public interface ElementFindStrategy<T> {
    T find(BaseElement baseElement);
}
