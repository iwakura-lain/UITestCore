package com.qalain.ui.suite.parser;

import com.qalain.ui.config.EngineConfig;
import com.qalain.ui.constant.AutoTestConstant;
import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.core.engine.EngineProperties;
import com.qalain.ui.core.hook.ShutdownHook;
import com.qalain.ui.suite.entity.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Slf4j
public class AutoTestSuiteParser {
    /**
     * spring上下文对象
     */
    private ApplicationContext applicationContext;

    /**
     * driver引擎
     */
    @Getter
    private EngineDriver engine;

    private AutoTestSuiteParser() {
        String customScanPackage = EngineProperties.get(EngineConfig.COMPONENT_SCAN_PACKAGE);
        if (StringUtils.isNotBlank(customScanPackage)) {
            applicationContext = new AnnotationConfigApplicationContext(AutoTestConstant.DEFAULT_SCAN_PACKAGE, customScanPackage);
        } else {
            applicationContext = new AnnotationConfigApplicationContext(AutoTestConstant.DEFAULT_SCAN_PACKAGE);
        }
        log.info("初始化spring容器完成，scan-custom-basePackage：{}", customScanPackage);
        engine = applicationContext.getBean(EngineDriver.class);
        //添加程序结束前的回调处理（关闭引擎驱动）
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(engine));
    }

    public List<AutoTestData> init(String testSuitePaths) {
        if (StringUtils.isBlank(testSuitePaths)) {
            log.warn("自动化测试流程文件路径为空，无法解析");
            return Collections.emptyList();
        }
        List<AutoTestData> autoTestDataList = new ArrayList<>();
        String[] testSuitePathList = StringUtils.split(testSuitePaths, ",");
        for (String testSuitePath : testSuitePathList) {
            AutoTestData autoTestData = loadAutoTestData(testSuitePath);
            if (autoTestData == null) {
                log.warn("自动化测试流程文件【{}】解析数据为空", testSuitePath);
                continue;
            }
            autoTestDataList.add(autoTestData);
        }
        return autoTestDataList;
    }

    public AutoTestData loadAutoTestData(String testSuitePath) {
        AutoTestData autoTestData = null;
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(testSuitePath)) {
            Document document = new SAXReader().read(inputStream);
            autoTestData = parseConfig(document);
        } catch (IOException | DocumentException e) {
            log.error("解析自动化测试流程文件【{}】错误", testSuitePath, e);
        }
        return autoTestData;
    }

    private AutoTestData parseConfig(Document document) {
        AutoTestData autoTestData = new AutoTestData();
        //获取根节点autoTestUiSuite
        Element rootElement = document.getRootElement();
        Element configElement = rootElement.element("uiConfig");
        //1.解析SuiteDriver配置信息
        SuiteDriver suiteDriver = parseSuiteDriver(configElement);
        autoTestData.setSuiteDriver(suiteDriver);
        //2.解析Ui元素信息
        Element uiElements = rootElement.element("uiElements");
        List<SuiteElement> suiteElements = parseSuiteElements(uiElements);
        autoTestData.setElementInfoList(suiteElements);
        //3.解析自动化流程信息
        Element flowsElement = rootElement.element("uiFlows");
        List<SuiteFlow> suiteFlows = parseSuiteFlows(flowsElement);
        autoTestData.setSuiteFlows(suiteFlows);
        return autoTestData;
    }

    private List<SuiteFlow> parseSuiteFlows(Element flowsElement) {
        if (flowsElement == null) {
            return Collections.emptyList();
        }
        List<SuiteFlow> suiteFlowList = new ArrayList<>();
        List<Element> flowElementList = flowsElement.elements("uiFlow");
        for (Element flowElement : flowElementList) {
            SuiteFlow suiteFlow = new SuiteFlow();
            suiteFlow.setAuthor(flowElement.elementText("author"));
            suiteFlow.setName(flowElement.elementText("name"));
            suiteFlow.setDesc(flowElement.elementText("desc"));
            Element uiStepsElement = flowElement.element("uiSteps");
            if (uiStepsElement == null) {
                suiteFlowList.add(suiteFlow);
                continue;
            }
            List<Element> uiStepElementList = uiStepsElement.elements("uiStep");
            for (Element uiStepElement : uiStepElementList) {
                FlowStep flowStep = new FlowStep();
                flowStep.setAction(uiStepElement.attributeValue("action"));
                flowStep.setUrl(uiStepElement.attributeValue("url"));
                flowStep.setRefId(uiStepElement.attributeValue("refId"));
                flowStep.setValue(uiStepElement.attributeValue("value"));
                flowStep.setExpectValue(uiStepElement.attributeValue("expectValue"));
                flowStep.setCustomFunction(uiStepElement.attributeValue("customFunction"));
                flowStep.setWaitTime(uiStepElement.attributeValue("waitTime") == null ? 0 : Integer.parseInt(uiStepElement.attributeValue("waitTime")));
                flowStep.setInvokerContent(StringUtils.defaultIfBlank(uiStepElement.attributeValue("jsCode"), uiStepElement.getText()));
                suiteFlow.addFlowStep(flowStep);
            }
            suiteFlowList.add(suiteFlow);
        }
        return suiteFlowList;
    }

    private List<SuiteElement> parseSuiteElements(Element uiElements) {
        if (uiElements == null) {
            return Collections.emptyList();
        }
        List<SuiteElement> suiteElementList = new ArrayList<>();
        List<Element> uiElementList = uiElements.elements("uiElement");
        for (Element uiElement : uiElementList) {
            SuiteElement suiteElement = new SuiteElement();
            suiteElement.setRefId(uiElement.attributeValue("id"));
            suiteElement.setType(uiElement.attributeValue("type"));
            suiteElement.setComment(uiElement.attributeValue("comment"));
            suiteElement.setId(uiElement.attributeValue("byId"));
            suiteElement.setName(uiElement.attributeValue("byName"));
            suiteElement.setCss(uiElement.attributeValue("byCss"));
            suiteElement.setXpath(uiElement.attributeValue("byXpath"));
            suiteElement.setLinkText(uiElement.attributeValue("byLinkText"));
            suiteElement.setPartialLinkText(uiElement.attributeValue("byPartialLinkText"));
            String waitTime = StringUtils.defaultIfEmpty(uiElement.attributeValue("waitTime"), "1000");
            suiteElement.setWaitTime(Long.parseLong(waitTime));
            suiteElementList.add(suiteElement);
        }
        return suiteElementList;
    }

    private SuiteDriver parseSuiteDriver(Element configElement) {
        SuiteDriver driverInfo = new SuiteDriver();
        Element engineElement = configElement.element("engine");
        driverInfo.setBrowser(engineElement.attributeValue("browser"));

        String remoteAddress = engineElement.attributeValue("remoteAddress");
        if (StringUtils.isBlank(remoteAddress)) {
            remoteAddress = EngineProperties.get(EngineConfig.REMOTE_ADDRESS);
        }

        driverInfo.setRemoteAddress(remoteAddress);
        driverInfo.setMaximize(Boolean.parseBoolean(engineElement.attributeValue("maximize")));
        String timeoutStr = engineElement.attributeValue("timeout");
        try {
            if (StringUtils.isNotBlank(timeoutStr)) {
                driverInfo.setTimeout(Long.parseLong(timeoutStr));
            }
        } catch (NumberFormatException e) {
            log.warn("解析timeout值失败，{}", timeoutStr);
        }

        String widthStr = engineElement.attributeValue("width");
        String heightStr = engineElement.attributeValue("height");
        try {
            if (StringUtils.isNotBlank(widthStr)) {
                driverInfo.setWidth(Integer.parseInt(widthStr));
            }
            if (StringUtils.isNotBlank(heightStr)) {
                driverInfo.setHeight(Integer.parseInt(heightStr));
            }
        } catch (NumberFormatException e) {
            log.warn("解析浏览器窗口width：{}或height：{}值失败", widthStr, heightStr);
        }

        // 设置isMobile
        String isMobile = engineElement.attributeValue("isMobile");
        try {
            if (StringUtils.isNotBlank(isMobile)) {
                driverInfo.setMobile(Boolean.parseBoolean(isMobile));
            }
        } catch (Exception e) {
            log.warn("设置为手机模式失败");
        }

        Element actionBeforeWaitTimeElement = configElement.element("actionBeforeWaitTime");
        if (actionBeforeWaitTimeElement != null) {
            String time = actionBeforeWaitTimeElement.attributeValue("time");
            try {
                driverInfo.setActionBeforeWaitTime(Long.parseLong(time));
            } catch (NumberFormatException e) {
                log.warn("解析actionBeforeWaitTime的time值失败，{}", time);
            }
        }
        Element actionAfterWaitTimeElement = configElement.element("actionAfterWaitTime");
        if (actionAfterWaitTimeElement != null) {
            String time = actionAfterWaitTimeElement.attributeValue("time");
            try {
                driverInfo.setActionAfterWaitTime(Long.parseLong(time));
            } catch (NumberFormatException e) {
                log.warn("解析actionBeforeWaitTime的time值失败，{}", time);
            }
        }
        return driverInfo;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static AutoTestSuiteParser getInstance() {
        return new AutoTestSuiteParser();
    }
}
