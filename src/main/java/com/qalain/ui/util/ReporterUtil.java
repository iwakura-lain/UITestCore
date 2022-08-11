package com.qalain.ui.util;

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
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.*;

/**
 * @author lain
 * @Description
 * @create 2022-01-25
 */
public class ReporterUtil {

    public static ExtentReports init(ExtentReports extent, String outFolder, String fileName) {
        File reportDir = new File(outFolder);
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            reportDir.mkdir();
        }
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(outFolder + fileName);
        // 设置静态文件的DNS，解决cdn.rawgit.com访问不了的情况
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setCSS(".node.level-1  ul{ display:none;} .node.level-1.active ul{display:block;}");
        //设置网页标题
        htmlReporter.config().setDocumentTitle("自动化UI测试");
        //设置报告标题
        htmlReporter.config().setReportName("自动化UI测试报告");
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);

        //设置系统信息
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("测试环境", EngineProperties.get(EngineConfig.TEST_ENV));
        extent.setSystemInfo("测试人员",EngineProperties.get(EngineConfig.TEST_PERSON));
        return extent;
    }

    public static void buildTestNodes(ExtentTest extenttest, IResultMap tests, Status status, String outFolder, ExtentReports extent) {
        EngineDriver driverEngine = SpringUtil.getBean(EngineDriver.class);
        //存在父节点时，获取父节点的标签
        String[] categories = new String[0];
        if (extenttest != null) {
            List<TestAttribute> categoryList = extenttest.getModel().getCategoryContext().getAll();
            categories = new String[categoryList.size()];
            for (int index = 0; index < categoryList.size(); index++) {
                categories[index] = categoryList.get(index).getName();
            }
        }

        ExtentTest test;
        if (tests.size() > 0) {
            //调整用例排序，按时间排序
            Set<ITestResult> treeSet = new TreeSet<>((o1, o2) -> o1.getStartMillis() < o2.getStartMillis() ? -1 : 1);
            treeSet.addAll(tests.getAllResults());
            for (ITestResult result : treeSet) {
                StringBuilder caseName = new StringBuilder();
                StringBuilder caseParam = new StringBuilder().append("方法入参：");
                caseName.append(result.getName());
                Object[] parameters = result.getParameters();
                String testName = "";
                ITestContext context = result.getTestContext();
                for (Object param : parameters) {
                    String jsonString = JSON.toJSONString(param);
                    testName = jsonString.substring(jsonString.lastIndexOf(":\"")+2, jsonString.lastIndexOf("\""));
                    caseParam.append(jsonString).append("<br/>");
                    break;
                }

                if (caseName.length() == 0) {
                    caseName.append(result.getMethod().getMethodName());
                }
                if (extenttest == null) {
                    test = extent.createTest(caseName.toString()).assignAuthor((String) context.getAttribute(testName));
                } else {
                    //作为子节点进行创建时，设置同父节点的标签一致，便于报告检索。
                    test = extenttest.createNode(caseName.toString()).assignCategory(categories).assignAuthor((String) context.getAttribute(testName));
                }
                for (String group : result.getMethod().getGroups()) {
                    test.assignCategory(group);
                }
                test.info(caseParam.toString());
                //将用例的log输出报告中
                List<String> reporterOutputList = Reporter.getOutput(result);
                for (String output : reporterOutputList) {
                    test.info(output);
                }
                int length = outFolder.split("/").length;
                String testFullName = result.getTestClass().getName() + "." + result.getName();
                List<String> screenshotList =  driverEngine.getScreenshotList(testFullName);
                for (String screenshotPath : screenshotList) {
                    //打印截图日志
                    ScreenCapture sc = new ScreenCapture();
                    int startIndex = SeleniumUtil.getCharacterPosition(screenshotPath, length);
                    sc.setPath(screenshotPath.substring(startIndex+1));
                    sc.setMediaType(MediaType.IMG);
                    test.info("截图", new MediaEntityModelProvider(sc));
                }
                //打印结果日志
                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable());
                } else {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }
                test.getModel().setStartTime(new Date(result.getStartMillis()));
                test.getModel().setEndTime(new Date(result.getEndMillis()));
            }

        }
    }

    public static void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory, ExtentReports extent, String outFolder, String fileName) {
        init(extent, outFolder, fileName);
        boolean createSuiteNode = false;
        if (suites.size() > 1) {
            createSuiteNode = true;
        }
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            //如果suite里面没有任何用例，则跳过，不在报告里生成
            if (result.size() == 0) {
                continue;
            }
            //统计suite下的成功、失败、跳过的总用例数
            int suiteFailSize = 0;
            int suitePassSize = 0;
            int suiteSkipSize = 0;
            ExtentTest suiteTest = null;
            //存在多个suite的情况下，在报告中将同一个一个suite的测试结果归为一类，创建一级节点。
            if (createSuiteNode) {
                suiteTest = extent.createTest(suite.getName()).assignCategory(suite.getName());
            }
            boolean createSuiteResultNode = false;
            if (result.size() > 1) {
                createSuiteResultNode = true;
            }
            for (ISuiteResult suiteResult : result.values()) {
                ExtentTest resultNode;
                ITestContext context = suiteResult.getTestContext();
                if (createSuiteResultNode) {
                    //没有创建suite的情况下，将在SuiteResult的创建为一级节点，否则创建为suite的一个子节点。
                    if (null == suiteTest) {
                        resultNode = extent.createTest(suiteResult.getTestContext().getName());
                    } else {
                        resultNode = suiteTest.createNode(suiteResult.getTestContext().getName());
                    }
                } else {
                    resultNode = suiteTest;
                }
                if (resultNode != null) {
                    resultNode.getModel().setName(suite.getName() + " : " + suiteResult.getTestContext().getName());
                    if (resultNode.getModel().hasCategory()) {
                        resultNode.assignCategory(suiteResult.getTestContext().getName());
                    } else {
                        resultNode.assignCategory(suite.getName(), suiteResult.getTestContext().getName());
                    }
                    resultNode.getModel().setStartTime(suiteResult.getTestContext().getStartDate());
                    resultNode.getModel().setEndTime(suiteResult.getTestContext().getEndDate());
                    //统计SuiteResult下的数据
                    int passSize = suiteResult.getTestContext().getPassedTests().size();
                    int failSize = suiteResult.getTestContext().getFailedTests().size();
                    int skipSize = suiteResult.getTestContext().getSkippedTests().size();
                    suitePassSize += passSize;
                    suiteFailSize += failSize;
                    suiteSkipSize += skipSize;
                    if (failSize > 0) {
                        resultNode.getModel().setStatus(Status.FAIL);
                    }
                    resultNode.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", passSize, failSize, skipSize));
                }
                buildTestNodes(resultNode, context.getFailedTests(), Status.FAIL, outFolder, extent);
                buildTestNodes(resultNode, context.getSkippedTests(), Status.SKIP, outFolder, extent);
                buildTestNodes(resultNode, context.getPassedTests(), Status.PASS, outFolder, extent);
            }
            if (suiteTest != null) {
                suiteTest.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;", suitePassSize, suiteFailSize, suiteSkipSize));
                if (suiteFailSize > 0) {
                    suiteTest.getModel().setStatus(Status.FAIL);
                }
            }

        }
        extent.flush();
    }
}
