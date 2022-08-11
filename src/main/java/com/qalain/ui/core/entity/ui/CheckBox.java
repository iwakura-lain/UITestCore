package com.qalain.ui.core.entity.ui;

import com.qalain.ui.core.operate.CheckOperate;
import com.qalain.ui.core.operate.StatusOperate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckBox extends BaseElement {

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    private String value;

    @Resource
    private CheckOperate checkBoxOperate;

    @Resource
    private StatusOperate statusOperate;

    /**
     * 根据传递的文本来选择
     * @param text 文本
     * @return
     */
    public void checkByText(String text) {
        checkBoxOperate.checkByText(this, text);
    }

    /**
     * 根据数据源文本来选择
     * @return
     */
    public void checkByText() {
        checkBoxOperate.checkByText(this, text);
    }

    /**
     * 根据传递的值来选择
     * @param value 值
     * @return
     */
    public void checkByValue(String value) {
        checkBoxOperate.checkByValue(this, value);
    }

    /**
     * 根据数据源的值来选择
     * @return
     */
    public void checkByValue() {
        checkBoxOperate.checkByValue(this, value);
    }
}

