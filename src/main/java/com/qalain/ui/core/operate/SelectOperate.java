package com.qalain.ui.core.operate;

import com.qalain.ui.core.entity.ui.BaseElement;

/**
 * @author lain
 * @Description select下拉列表操作
 * @create 2022-01-21 14:18
 */
public interface SelectOperate {

    /**
     * 根据文本选择
     * @param element 操作元素
     * @param text 文本
     */
    void selectByText(BaseElement element, String text);

    /**
     * 根据值选择
     * @param element 操作元素
     * @param value 值
     */
    void selectByValue(BaseElement element, String value);

    /**
     * 根据序号选择
     * @param element 操作元素
     * @param index 序号
     */
    void selectByIndex(BaseElement element, int index);

    /**
     * 根据文本取消选择
     * @param element 操作元素
     * @param text 文本
     */
    void deselectByText(BaseElement element, String text);

    /**
     * 根据值取消选择
     * @param element 操作元素
     * @param value 值
     */
    void deselectByValue(BaseElement element, String value);

    /**
     * 根据文本取消选择
     * @param element 操作元素
     * @param index 序号
     */
    void deselectByIndex(BaseElement element, int index);

    /**
     * 取消全部选择
     */
    void deselectAll(BaseElement element);
}