package com.qalain.ui.core.operate.impl;

import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.core.operate.KeyBoardOperate;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
public class KeyBoardOperateImpl implements KeyBoardOperate {
    @Resource
    private EngineDriver engine;

    @Override
    public void enter() {
        new Actions(engine.getThreadLocalDriver()).keyDown(Keys.ENTER).perform();
    }
}
