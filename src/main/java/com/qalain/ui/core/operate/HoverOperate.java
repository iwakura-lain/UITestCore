package com.qalain.ui.core.operate;

import com.qalain.ui.core.entity.ui.BaseElement;

/**
 * @author lain
 * @Description 悬停操作
 * @create 2022-01-21 14:18
 */
public interface HoverOperate {

    /**
     * 悬停timemill毫秒
     * @param element 操作元素
     * @param timeMills 毫秒
     */
    void hover(BaseElement element, long timeMills);
}
