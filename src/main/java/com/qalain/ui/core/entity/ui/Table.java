package com.qalain.ui.core.entity.ui;

import com.qalain.ui.core.operate.TableOperate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Table extends BaseElement {

    @Resource
    private TableOperate tableOperate;

    /**
     * 获取表头信息
     * @return
     */
    public List<String> getTableHeader() {
        return tableOperate.getTableHeader(this);
    }

    /**
     * 获取表格内容信息
     * @return
     */
    public List<List<String>> getTableBody() {
        return tableOperate.getTableBody(this);
    }
}
