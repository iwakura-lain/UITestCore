package com.qalain.ui.core.operate;

import com.qalain.ui.core.entity.ui.BaseElement;

/**
 * @author lain
 * @Description 点击操作
 * @create 2022-01-21 14:18
 */
public interface ClickOperate {

    /**
     * 单击
     * @param element 操作元素
     */
    void click(BaseElement element);

    /**
     * 双击
     * @param element 操作元素
     */
    void doubleClick(BaseElement element);
}