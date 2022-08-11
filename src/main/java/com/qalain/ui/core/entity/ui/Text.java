package com.qalain.ui.core.entity.ui;

import com.qalain.ui.core.operate.ClickOperate;
import com.qalain.ui.core.operate.HoverOperate;
import com.qalain.ui.core.operate.StatusOperate;
import com.qalain.ui.core.operate.TextOperate;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Retention;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Text extends BaseElement {

    /**
     * 文本框的value值
     */
    @Getter
    private String value;

    @Resource
    private TextOperate textOperate;

    @Resource
    private ClickOperate clickOperate;

    @Resource
    private StatusOperate statusOperate;

    public Text() {}

    public Text(String value) {
        this.value = value;
    }

    /**
     * 填充value值到文本框中
     * @return
     */
    public Text fillValue() {
        textOperate.setTextValue(this, value);
        return this;
    }

    /**
     * 设置value给文本框元素
     * @param value 值
     * @return
     */
    public Text setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 获取text文本值
     * @return
     */
    public String getText() {
        Object text = textOperate.getTextValue(this);
        return text == null ? null : text.toString();
    }

    /**
     * 点击文本框
     */
    public void click() {
        clickOperate.click(this);
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

