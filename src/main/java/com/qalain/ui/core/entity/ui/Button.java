package com.qalain.ui.core.entity.ui;

import com.qalain.ui.core.operate.ClickOperate;
import com.qalain.ui.core.operate.HoverOperate;
import com.qalain.ui.core.operate.StatusOperate;
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
public class Button extends BaseElement {

    @Resource
    private ClickOperate clickOperate;

    @Resource
    private HoverOperate hoverOperate;

    @Resource
    private StatusOperate statusOperate;

    /**
     * 单击
     */
    public void click() {

        clickOperate.click(this);
    }

    /**
     * 双击
     */
    public void doubleClick() {
        clickOperate.doubleClick(this);
    }

    /**
     * 悬停
     * @param millisecond 毫秒
     */
    public void hover(long millisecond) {
        hoverOperate.hover(this,millisecond);
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
