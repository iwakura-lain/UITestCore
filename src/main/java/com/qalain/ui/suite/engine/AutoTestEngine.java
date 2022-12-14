package com.qalain.ui.suite.engine;

import com.qalain.ui.config.EngineConfig;
import com.qalain.ui.util.GFG;
import com.qalain.ui.constant.SuiteConstant;
import com.qalain.ui.core.AutoTestProcessor;
import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.core.engine.EngineProperties;
import com.qalain.ui.core.entity.ui.Text;
import com.qalain.ui.core.page.Page;
import com.qalain.ui.core.strategy.ElementFindStrategy;
import com.qalain.ui.core.strategy.FindStrategyContext;
import com.qalain.ui.generator.CodeGenerator;
import com.qalain.ui.suite.action.ICustomAction;
import com.qalain.ui.suite.adapter.ElementAdapter;
import com.qalain.ui.suite.entity.*;
import com.qalain.ui.suite.invoker.JsInvoker;
import com.qalain.ui.suite.parser.AutoTestSuiteParser;
import com.qalain.ui.util.*;
import com.qalain.ui.util.opencv.TemplateMatchUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.opencv.core.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.internal.TestResult;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Slf4j
public class AutoTestEngine implements ITest {

    public static final String AUTO_TEST_FLOW_DATA = "AUTO_TEST_FLOW_DATA";

    private AutoTestSuiteParser autoTestSuiteParser = AutoTestSuiteParser.getInstance();
    private AutoTestProcessor autoTestProcessor = AutoTestProcessor.getInstance();

    private List<AutoTestData> autoTestDataList;

    private EngineDriver engine;

    private ElementFindStrategy<WebElement> elementFindStrategy;

    private ThreadLocal<String> testName = new ThreadLocal<>();

    private static ThreadLocal<String> authorName = new ThreadLocal<>();

    @BeforeClass
    public void init() {
        //????????????????????????
        boolean isParallel = ReadPropertiesUtil.getBooleanProp("engine", "test.data.parallel");
        int threadPoolSize = Integer.parseInt(ReadPropertiesUtil.getProp("engine", "test.case.run.thread.pool.size"));
        setParallel(isParallel, threadPoolSize);

        //??????????????????UI??????????????????
        String testSuitePath = EngineProperties.get(EngineConfig.TEST_SUITE_PATH);
        //getAllFileNameByBFS(testSuitePath);
        //??????????????????????????????????????????????????????????????????
        if (testSuitePath.contains("xml")) {
            autoTestDataList = autoTestSuiteParser.initWithPart(testSuitePath);
        } else {
            // bfs ??????????????????????????????
            // ?????????????????????
            if (testSuitePath.split(",").length > 1) {
                String[] filePaths = testSuitePath.split(",");
                StringBuffer allXMLFilePath = new StringBuffer();
                for (String filePath : filePaths) {
                    String allFileNameByBFS = getAllFileNameByBFS(filePath);
                    if (StringUtils.isBlank(allFileNameByBFS)) {
                        continue;
                    }
                    allXMLFilePath.append(allFileNameByBFS).append("/").append(",");
                }
                autoTestDataList = autoTestSuiteParser.initWithPart(allXMLFilePath.deleteCharAt(allXMLFilePath.length()-1).toString());
            } else {
                // String suiteFilePath = AutoTestEngine.class.getClassLoader().getResource(testSuitePath).getPath();
                String allFileNameByBFS = getAllFileNameByBFS(testSuitePath);
                autoTestDataList = autoTestSuiteParser.initWithPart(allFileNameByBFS);
            }
        }

        //?????? page ???
        String testPagePath = EngineProperties.get(EngineConfig.TEST_PAGE_PATH);
        for (String s : testPagePath.split(",")) {
            CodeGenerator.generate(s, "src/main/java");
        }

        //??????Engine??????
        engine = autoTestSuiteParser.getEngine();
        //????????????????????????
        FindStrategyContext findStrategyContext = autoTestSuiteParser.getApplicationContext().getBean(FindStrategyContext.class);
        elementFindStrategy = findStrategyContext.getStrategy(WebElement.class);
    }

    @DataProvider(name = AUTO_TEST_FLOW_DATA)
    public Iterator<Object[]> prepareTestData() {
        List<Object[]> dataProvider = new ArrayList<>();
        for (AutoTestData autoTestData : autoTestDataList) {
            for (SuiteFlow suiteFlow : autoTestData.getSuiteFlows()) {
                dataProvider.add(new Object[]{
                        new SuiteTestFlowData(autoTestData.getSuiteDriver(), autoTestData.getElementInfoList(), suiteFlow)
                });
            }
        }
        return dataProvider.iterator();
    }

    @BeforeMethod(alwaysRun = true)
    public void BeforeMethod(Method method, Object[] testData){

        if (testData[0] instanceof SuiteTestFlowData) {
            SuiteTestFlowData suiteTestFlowData = (SuiteTestFlowData) testData[0];
            testName.set(method.getName() + "_" + suiteTestFlowData.getSuiteFlow().getName());
            authorName.set(suiteTestFlowData.getSuiteFlow().getAuthor());
        } else {
            testName.set(method.getName());
        }
        log.info("before:{}",method.getName());
    }

    @Test(dataProvider = AUTO_TEST_FLOW_DATA)
    public void execute(SuiteTestFlowData suiteTestFlowData, ITestContext context) throws InterruptedException {
        log.info("testName:{}", testName.get());
        LogUtil.info("???????????????{}???{}???", suiteTestFlowData.getSuiteFlow().getName(), suiteTestFlowData.getSuiteFlow().getDesc());
        //??????engine???webDriver????????????????????????????????????????????????????????????????????????????????????
        if (engine.getThreadLocalDriver() == null) {
            engine.setDriverInfo(suiteTestFlowData.getSuiteDriver());
            engine.init();
        }
        //??????????????? context ???
        context.setAttribute("author", authorName.get());

        List<FlowStep> flowStepList = suiteTestFlowData.getSuiteFlow().getFlowStepList();
        Map<String, String> extractMap = new HashMap<>();
        for (FlowStep flowStep : flowStepList) {
            ThreadUtil.sleep(suiteTestFlowData.getSuiteDriver().getActionBeforeWaitTime());
            doFlowStep(flowStep, suiteTestFlowData.getSuiteElementMap(), extractMap);
            ThreadUtil.sleep(suiteTestFlowData.getSuiteDriver().getActionAfterWaitTime());
        }
    }

    private void doFlowStep(FlowStep flowStep, Map<String, SuiteElement> elementMap, Map<String, String> extractMap) {
        WebElement webElement = null;
        WebDriver webDriver = engine.getThreadLocalDriver();
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(flowStep.getWaitTime()));
        switch (flowStep.getAction()) {
            case SuiteConstant.Action.OPEN_PAGE:
                engine.open(flowStep.getUrl());
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.CLICK:
                webElement = elementFindStrategy.find(ElementAdapter.getBaseElement(elementMap.get(flowStep.getRefId())));
                Actions clickAction = new Actions(webDriver);
                clickAction.moveToElement(webElement);
                clickAction.click(webElement).build().perform();
                //webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement)).click();
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case  SuiteConstant.Action.EXTRACT:
                webElement = elementFindStrategy.find(ElementAdapter.getBaseElement(elementMap.get(flowStep.getRefId())));
                extractMap.put(flowStep.getVariableName(), webElement.getAttribute(flowStep.getTagFiled()));
                log.info(extractMap.toString());
                break;
            case SuiteConstant.Action.HOVER:
                webElement = elementFindStrategy.find(ElementAdapter.getBaseElement(elementMap.get(flowStep.getRefId())));
                Actions actions = new Actions(webDriver);
                actions.moveToElement(webElement).perform();
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.FILL_VALUE:
                webElement = elementFindStrategy.find(ElementAdapter.getBaseElement(elementMap.get(flowStep.getRefId())));
                if (!StringUtils.isBlank(webElement.getText())) {
                    webElement.clear();
                }
                webElement.sendKeys(StringUtils.defaultIfEmpty(flowStep.getValue(), StringUtils.EMPTY));
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.COMPARE_VALUE:
                if (StringUtils.isNotBlank(flowStep.getVariableName()) && flowStep.getVariableName().charAt(0)=='$') {
                    int index = flowStep.getVariableName().indexOf("$");
                    log.info(extractMap.toString());
                    log.info(flowStep.getVariableName().substring(index));
                    String value = extractMap.get(flowStep.getVariableName().substring(index+1));
                    log.info("????????????{}???????????????{}", value, flowStep.getExpectValue());
                    Assert.assertEquals(value, flowStep.getExpectValue());
                } else {
                    webElement = elementFindStrategy.find(ElementAdapter.getBaseElement(elementMap.get(flowStep.getRefId())));
                    String value = webElement.getAttribute("value");
                    log.info("???????????????{}??????????????????{}", elementMap.get(flowStep.getRefId()), value);
                    Assert.assertEquals(value, flowStep.getExpectValue());
                }

                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.KEY_BOARD_ENTER:
                new Actions(webDriver).sendKeys(Keys.ENTER).build().perform();
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.SWITCH_WINDOW:
                WindowHandleUtil.switchWindow(webDriver);
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.CLOSE_CURRENT_WINDOW:
                webDriver.close();
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.CLOSE_DRIVER:
                engine.getThreadLocalDriver().quit();
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.JS_INVOKER:
                JsInvoker.execute(webDriver, flowStep.getInvokerContent());
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.CUSTOM:
                if (StringUtils.isNotBlank(flowStep.getRefId())) {
                    webElement = elementFindStrategy.find(ElementAdapter.getBaseElement(elementMap.get(flowStep.getRefId())));
                }
                Map<String, ICustomAction> customActionMap = autoTestSuiteParser.getApplicationContext().getBeansOfType(ICustomAction.class);
                ICustomAction customAction = customActionMap.get(flowStep.getCustomFunction());
                if (customAction == null) {
                    log.warn("??????????????????{}?????? customFunction", flowStep.getCustomFunction());
                    break;
                }
                customAction.execute(engine, flowStep, webElement);
                ThreadUtil.sleep(flowStep.getWaitTime());
                break;
            case SuiteConstant.Action.SWITCH_IFRAME:
                webElement = elementFindStrategy.find(ElementAdapter.getBaseElement(elementMap.get(flowStep.getRefId())));
                webDriverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(webElement));
                break;
            case SuiteConstant.Action.REFRESH:
                webDriver.navigate().refresh();
                break;
            case  SuiteConstant.Action.COMPARE_IMG:
                String imgPath = this.getClass().getResource("/").getPath() + EngineProperties.get(EngineConfig.SCREENSHOT_PATH);
                String nowImgName = System.currentTimeMillis() + ".jpg";
                String nowImgPath = imgPath + nowImgName;
                SeleniumUtil.screenshot(webDriver, nowImgPath);
                String res = GFG.compareImgAndGetPercentage(EngineProperties.get(EngineConfig.SCREENSHOT_PATH) + flowStep.getVariableName() + ".jpg", EngineProperties.get(EngineConfig.SCREENSHOT_PATH) + nowImgName);
                log.info(res);
                break;
            case  SuiteConstant.Action.SCREEN:
                String screenPath =this.getClass().getResource("/").getPath() + EngineProperties.get(EngineConfig.SCREENSHOT_PATH);
                String destPath = screenPath + flowStep.getVariableName() + ".jpg";
                SeleniumUtil.screenshot(webDriver, destPath);
                break;
            case  SuiteConstant.Action.MATCH_AND_CLICK:
                // ??????
                String srcImgPath = this.getClass().getResource("/").getPath() + EngineProperties.get(EngineConfig.SRC_IMG_PATH);
                String srcImgName = System.currentTimeMillis() + ".jpg";
                String contentRootPath = srcImgPath + srcImgName;
                SeleniumUtil.screenshot(webDriver, contentRootPath);

                // ???????????????????????????????????????
                Point templateCoordinate = TemplateMatchUtil.templateMatchWithCCORRNORMED(
                        contentRootPath,
                        this.getClass().getResource("/").getPath() + EngineProperties.get(EngineConfig.TEMPLATE_IMG_PATH) + flowStep.getTemplateImgName()
                );

                System.out.println(templateCoordinate.toString());
                System.out.println(webDriver.manage().window().getSize());
                Actions action = new Actions(webDriver);
//                action.getActivePointer().createPointerMove(Duration.ofSeconds(2), PointerInput.Origin.pointer(), (int)templateCoordinate.x, (int)templateCoordinate.y);
//                action.moveByOffset((int)templateCoordinate.x, (int)templateCoordinate.y).click().build().perform();
                //webDriver.manage().window().setSize(new Dimension(2070, 3456));
                action.moveByOffset(0, 0).build().perform();
                action.moveByOffset((int)templateCoordinate.x, (int)templateCoordinate.y).click().build().perform();
            default:
                break;
        }

    }

    @Override
    public String getTestName() {
        return testName.get();
    }

    public static String getAuthorName() {
        return authorName.get();
    }

    @AfterMethod(alwaysRun = true)
    public void setResultTestName(ITestResult result) {
        try {
            Field resultMethod = TestResult.class.getDeclaredField("m_method");
            resultMethod.setAccessible(true);
            resultMethod.set(result, result.getMethod().clone());
            Field methodName = org.testng.internal.BaseTestMethod.class.getDeclaredField("m_methodName");
            methodName.setAccessible(true);
            methodName.set(result.getMethod(), testName.get());
            System.out.println("after: " + testName.get());
            boolean isParallel = ReadPropertiesUtil.getBooleanProp("engine", "test.data.parallel");
            int threadPoolSize = Integer.parseInt(ReadPropertiesUtil.getProp("engine", "test.case.run.thread.pool.size"));
            if (engine.getDriverInfo().getRemoteAddress() != null & isParallel & threadPoolSize > 0) {
                ((RemoteWebDriver)engine.getThreadLocalDriver()).quit();
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            Reporter.log("Exception : " + e.getMessage());
        }
    }

    @AfterClass
    void terminate () {
        engine.getWebdriverThreadLocal().remove();
    }

    private void setParallel(boolean isParallel, int threadPoolSize) {
        Class<AutoTestEngine> engineClass = AutoTestEngine.class;
        try {
            Method testData = engineClass.getDeclaredMethod("prepareTestData");
            DataProvider dataProvider = testData.getAnnotation(DataProvider.class);
            InvocationHandler dataHandler = Proxy.getInvocationHandler(dataProvider);
            Field dataHandlerFields = dataHandler.getClass().getDeclaredField("memberValues");
            dataHandlerFields.setAccessible(true);
            Map members = (Map)dataHandlerFields.get(dataHandler);
            members.put("parallel", isParallel);

            Method execute = engineClass.getDeclaredMethod("execute", SuiteTestFlowData.class, ITestContext.class);
            Test testAnnotation = execute.getAnnotation(Test.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(testAnnotation);
            Field declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
            declaredField.setAccessible(true);
            Map memberValues = (Map)declaredField.get(invocationHandler);
            memberValues.put("threadPoolSize", threadPoolSize);
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String getAllFileNameByBFS(String fileRootPath) {
        if (StringUtils.isBlank(fileRootPath)) {
            return "";
        }
        Queue<String> filePathList = new LinkedList<>();
        StringBuffer xmlFileNames = new StringBuffer();
        filePathList.add(fileRootPath);

        while(!filePathList.isEmpty()) {
            int size = filePathList.size();
            while (size > 0) {
                String filePath = filePathList.poll();
                String suiteFilePath = AutoTestEngine.class.getClassLoader().getResource(filePath).getPath();
                String[] childFile = new File(suiteFilePath).list();
                for (String childFileName : childFile) {
                    String fileOrDir = AutoTestEngine.class.getClassLoader().getResource(filePath+childFileName).getPath();
                    if (new File(fileOrDir).isFile()) {
                        xmlFileNames.append(filePath + childFileName).append(",");
                    } else {
                        filePathList.add(filePath + childFileName + "/");
                    }
                }
                size--;
            }
        }

        return xmlFileNames.deleteCharAt(xmlFileNames.length()-1).toString();
    }
}
