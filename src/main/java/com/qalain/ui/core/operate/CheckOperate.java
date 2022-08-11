package com.qalain.ui.core.operate;

import com.qalain.ui.core.entity.ui.BaseElement;

/**
 * @author lain
 * @Description checkbox 操作行为
 * @create 2022-01-21 14:18
 */
public interface CheckOperate {

    /**
     * 根据文本选择
     * @param element 操作元素
     * @param text 文本
     */
    void checkByText(BaseElement element, String text);

    /**
     * 根据值选择
     * @param element 操作元素
     * @param value 值
     */
    void checkByValue(BaseElement element, String value);
}