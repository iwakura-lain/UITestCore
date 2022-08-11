package com.qalain.ui.core.operate;

import com.qalain.ui.core.entity.ui.BaseElement;

/**
 * @author lain
 * @Description 文本框操作
 * @create 2022-01-21 14:18
 */
public interface TextOperate {

    /**
     * 获取文本值
     * @param element 操作元素
     * @return
     */
    Object getTextValue(BaseElement element);

    /**
     * 设置文本值
     * @param element 操作元素
     * @param value 设置的文本值
     */
    void setTextValue(BaseElement element, Object value);
}