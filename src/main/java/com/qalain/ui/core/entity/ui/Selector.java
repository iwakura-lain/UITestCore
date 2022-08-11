package com.qalain.ui.core.entity.ui;

import com.qalain.ui.core.operate.SelectOperate;
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
public class Selector extends BaseElement {

    /**
     * 文本值
     */
    @Getter
    @Setter
    private String text;

    /**
     * 序号
     */
    @Getter
    @Setter
    private int index;

    /**
     * 值
     */
    @Getter
    @Setter
    private String value;

    @Resource
    private SelectOperate selectOperate;

    @Resource
    private StatusOperate statusOperate;

    /**
     * 根据文本选择
     */
    public void selectByText() {
        selectOperate.selectByText(this, text);
    }

    /**
     * 根据值选择
     */
    public void selectByValue() {
        selectOperate.selectByValue(this, value);
    }

    /**
     * 根据文本取消选择
     */
    public void deselectByText() {
        selectOperate.deselectByText(this, text);
    }

    /**
     * 根据值取消选择
     */
    public void deselectByValue() {
        selectOperate.deselectByValue(this, value);
    }

    /**
     * 根据序号取消选择
     */
    public void deselectByIndex() {
        selectOperate.deselectByIndex(this, index);
    }

    /**
     * 取消全部选择
     */
    public void deselectAll() {
        selectOperate.deselectAll(this);
    }

    /**
     * 判断文本框是否可用
     * @return true：可用；false：不可用
     */
    public boolean isEnabled() {
        return statusOperate.isEnabled(this);
    }

    /**
     * 判断文本框是否显示
     * @return true：显示；false：隐藏
     */
    public boolean isDisplayed() {
        return statusOperate.isDisplayed(this);
    }
}

