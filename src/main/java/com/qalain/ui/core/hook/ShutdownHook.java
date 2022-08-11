package com.qalain.ui.core.hook;

import com.qalain.ui.core.engine.EngineDriver;
import lombok.extern.slf4j.Slf4j;


/**
 * @author lain
 * @Description
 * @create 2022-01-22
 */
@Slf4j
public class ShutdownHook extends Thread {

    private EngineDriver driverEngine;

    public ShutdownHook(EngineDriver driverEngine) {
        this.driverEngine = driverEngine;
    }

    @Override
    public void run() {
        log.info("shutdown hook run：close webdriver");
        driverEngine.close();
    }
}