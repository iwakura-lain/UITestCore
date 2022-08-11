package com.qalain.ui.core.operate;

import com.qalain.ui.core.entity.ui.BaseElement;

/**
 * @author lain
 * @Description 元素状态操作
 * @create 2022-01-21 14:18
 */
public interface StatusOperate {

    /**
     * 是否处于可用状态
     * @param element 操作元素
     * @return true：可用；false：不可用
     */
    boolean isEnabled(BaseElement element);

    /**
     * 是否处于隐展示状态
     * @param element 操作元素
     * @return true：展示；false：隐藏
     */
    boolean isDisplayed(BaseElement element);
}