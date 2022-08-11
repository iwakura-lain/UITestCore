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
import com.qalain.ui.util.ReporterUtil;
import com.qalain.ui.util.SeleniumUtil;
import com.qalain.ui.util.SpringUtil;
import org.springframework.stereotype.Component;
import org.testng.*;
import org.testng.xml.XmlSuite;


import java.io.File;
import java.util.*;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Component
public class MailReporterListener implements IReporter {

    private static final String OUTPUT_FOLDER = "test-output/";

    private static final String FILE_NAME = "report.html";

    private static final String BR = "<br/>";

    private ExtentReports extent;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        extent = ReporterUtil.init(extent, OUTPUT_FOLDER, FILE_NAME);
        ReporterUtil.generateReport(xmlSuites, suites, outputDirectory, extent, OUTPUT_FOLDER, FILE_NAME);
    }
}
