package com.qalain.ui.core.operate;

import com.qalain.ui.core.entity.ui.BaseElement;

import java.util.List;

/**
 * @author lain
 * @Description 表格操作
 * @create 2022-01-21 14:18
 */
public interface TableOperate {

    /**
     * 获取表头值
     * @param element
     * @return
     */
    List<String> getTableHeader(BaseElement element);

    /**
     * 获取表格内容值
     * @param element
     * @return
     */
    List<List<String>> getTableBody(BaseElement element);
}