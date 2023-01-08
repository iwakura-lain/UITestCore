package com.qalain.ui.report;

import com.alibaba.fastjson.JSON;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.model.MediaType;
import com.aventstack.extentreports.model.ScreenCapture;
import com.aventstack.extentreports.model.TestAttribute;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.qalain.ui.config.EngineConfig;
import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.core.engine.EngineProperties;
import com.qalain.ui.suite.engine.AutoTestEngine;
import com.qalain.ui.suite.entity.AutoTestData;
import com.qalain.ui.util.ExtentManager;
import com.qalain.ui.util.ReporterUtil;
import com.qalain.ui.util.SeleniumUtil;
import com.qalain.ui.util.SpringUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;
import org.testng.*;
import org.testng.xml.XmlSuite;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Component
public class ExtentTestNGIReporterListener implements ITestListener, IReporter {

    /**
     * 报告生成路径
     */
    private static final String OUTPUT_FOLDER = EngineProperties.get(EngineConfig.REPORT_PATH, "test-output/");

    /**
     * 报告生成文件名称前缀
     */
    private static final String REPORT_PREFIX_NAME = "report_";

    /**
     * 报告生成文件名称后缀
     */
    private static final String REPORT_SUFFIX_NAME = ".html";

    private ExtentReports extentReport = ExtentManager.getInstance("test-output/report.html");
    //private ExtentReports extentReportWithDate = ExtentManager.getInstance("test-output/report.html");

    private static ThreadLocal test = new ThreadLocal();

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        String fileName = REPORT_PREFIX_NAME + DateFormatUtils.format(new Date(), "yyyy-MM-dd_HH-mm-ss") + REPORT_SUFFIX_NAME;

        extentReport = ReporterUtil.init(extentReport, OUTPUT_FOLDER, "report.html");
        //extentReportWithDate = ReporterUtil.init(extentReport, OUTPUT_FOLDER, fileName);

        ReporterUtil.generateReport(xmlSuites, suites, outputDirectory, extentReport, OUTPUT_FOLDER, "report.html");
        ReporterUtil.generateReport(xmlSuites, suites, outputDirectory, extentReport, OUTPUT_FOLDER, fileName);

        extentReport = ExtentManager.getInstance("test-output/report.html");
        //extentReportWithDate = ExtentManager.getInstance("test-output/" + fileName);
    }

    @Override
    public synchronized void onStart(ITestContext context) {
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        extentReport.flush();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        test.set(extentReport.createTest(result.getTestName().split("_")[1]));
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        ((ExtentTest) test.get()).assignAuthor(AutoTestEngine.getAuthorName()).pass(result.getTestName() + "测试通过");
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        ((ExtentTest) test.get()).assignAuthor(AutoTestEngine.getAuthorName()).fail(result.getThrowable());
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        ((ExtentTest) test.get()).assignAuthor(AutoTestEngine.getAuthorName()).skip(result.getThrowable());
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }
}


