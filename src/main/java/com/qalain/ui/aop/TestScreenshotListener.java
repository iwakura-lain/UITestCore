package com.qalain.ui.aop;

import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.util.SpringUtil;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
public class TestScreenshotListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        EngineDriver driverEngine = SpringUtil.getBean(EngineDriver.class);
        String testFullName = tr.getTestClass().getName() + "." + tr.getName();
        driverEngine.getScreenshotList(testFullName).add(driverEngine.screenshot());
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        EngineDriver driverEngine = SpringUtil.getBean(EngineDriver.class);
        String testFullName = tr.getTestClass().getName() + "." + tr.getName();
        driverEngine.getScreenshotList(testFullName).add(driverEngine.screenshot());
    }
}