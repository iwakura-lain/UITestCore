package com.qalain.ui.suite.action;

import com.qalain.ui.core.engine.EngineDriver;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
public interface ICustomAction {
    /**
     * 自定义操作
     * @param engineDriver engine驱动
     */
    void execute(EngineDriver engineDriver);
}
