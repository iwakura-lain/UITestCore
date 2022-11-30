package com.qalain.ui.core.engine;

import com.qalain.ui.config.EngineConfig;
import com.qalain.ui.constant.BrowserConstant;
import com.qalain.ui.core.entity.DriverInfo;
import com.qalain.ui.exceptions.AutoUiTestException;
import com.qalain.ui.util.SeleniumUtil;
import com.qalain.ui.util.SystemUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lain
 * @Description：驱动创建
 * @create 2022-01-21
 */
@Slf4j
@Component
public class EngineDriver {


    @Getter
    @Setter
    private DriverInfo driverInfo;

    /**
     * Page截图集合
     */
    private Map<String, List<String>> screenshotMap = new HashMap<>();

    /**
     * 浏览器配置映射集合
     */
    private Map<String, MutableCapabilities> browserOptionsMap = new HashMap<>();

    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriver getThreadLocalDriver() {
        return driver.get();
    }

    public ThreadLocal<WebDriver> getWebdriverThreadLocal() {
        return driver;
    }

    /**
     * 初始化驱动引擎
     */
    public void init() {
        createWebDriver();
    }

    /**
     * 创建一个webDriver对象
     *
     * @return
     */
    public void createWebDriver() {
        initBrowserEngine();
        initBrowserCapMap();
        //获取当前浏览器配置信息
        MutableCapabilities browserOptions = browserOptionsMap.get(driverInfo.getBrowser());

        if (browserOptions == null) {
            throw new AutoUiTestException(String.format("未知类型的浏览器引擎名称，%s", driverInfo.getBrowser()));
        }
        //是否录制vnc
        browserOptions.setCapability("se:recordVideo", true);
        browserOptions.setCapability("se:screenResolution", "1920x1080");
        WebDriver webDriver = null;
        log.info("driver remote address:{}", driverInfo.getRemoteAddress());

        //若远程地址不为空，则创建RemoteWebDriver
        if (StringUtils.isNotBlank(driverInfo.getRemoteAddress())) {
            try {
                webDriver = new RemoteWebDriver(new URL(driverInfo.getRemoteAddress()), browserOptions);
            } catch (MalformedURLException e) {
                log.error(e.getMessage(), e);
                throw new AutoUiTestException(e.getMessage());
            }
        } else if (BrowserConstant.CHROME.equals(driverInfo.getBrowser())) {
            log.info("创建一个chrome浏览器");
            webDriver = new ChromeDriver((ChromeOptions) browserOptions);
        } else if (BrowserConstant.IE.equals(driverInfo.getBrowser())) {
            log.info("创建一个ie浏览器");
            webDriver = new InternetExplorerDriver((InternetExplorerOptions) browserOptions);
        } else if (BrowserConstant.FIREFOX.equals(driverInfo.getBrowser())) {
            log.info("创建一个firefox浏览器");
            webDriver = new FirefoxDriver((FirefoxOptions) browserOptions);
        } else if (BrowserConstant.SAFARI.equals(driverInfo.getBrowser())) {
            log.info("创建一个safari浏览器");
            webDriver = new SafariDriver((SafariOptions) browserOptions);
        } else {
            throw new AutoUiTestException("未知浏览器类型，无法创建WebDriver");
        }

        //设置超时等待时间
        if (driverInfo.getTimeout() > 0) {
            webDriver.manage().timeouts().implicitlyWait(driverInfo.getTimeout(), TimeUnit.MICROSECONDS);
        }

        //设置浏览器窗口是否最大化
        if (!SystemUtil.isMac() && driverInfo.isMaximize()) {
            webDriver.manage().window().maximize();
        }

        //设置浏览器窗口宽度
        if (driverInfo.getWidth() > 0) {
            webDriver.manage().window().setSize(new Dimension(driverInfo.getWidth(), webDriver.manage().window().getSize().height));
        }

        //设置浏览器窗口高度
        if (driverInfo.getHeight() > 0) {
            webDriver.manage().window().setSize(new Dimension(webDriver.manage().window().getSize().getWidth(), driverInfo.getHeight()));
        }

        driver.set(webDriver);
    }

    private void initBrowserEngine() {
        setBrowserEngine(EngineConfig.CHROME_DRIVER, "driver/chromedriver", "webdriver.chrome.driver");
        setBrowserEngine(EngineConfig.FIREFOX_DRIVER, "driver/geckodriver", "webdriver.gecko.driver");
        setBrowserEngine(EngineConfig.SAFARI_DRIVER, "driver/safari", "webdriver.safari.driver");
    }

    private void setBrowserEngine(String driverName, String driverPath, String properties) {
        String path = EngineProperties.get(driverName, driverPath);
        URL url = this.getClass().getClassLoader().getResource(path);
        log.info("driver path：{}", path);
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(Objects.requireNonNull(url).toString())) {
            return;
        }
        try {
            File driverFile = new File(URLDecoder.decode(url.getFile(), "utf-8"));
            System.getProperties().put(properties, driverFile.getAbsolutePath());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 打开页面
     *
     * @param url
     */
    public void open(String url) {
        driver.get().get(url);
    }

    /**
     * 关闭driver驱动
     */
    public void close() {
        if (driver.get() != null) {
            driver.get().quit();
            driver = null;
        }
    }

    /**
     * 截图
     */
    public String screenshot() {
        String reportPath = EngineProperties.get(EngineConfig.REPORT_PATH, "test-output/");
        String screenPath = (reportPath.endsWith("/") ? reportPath : reportPath + "/") + EngineProperties.get(EngineConfig.SCREENSHOT_PATH, "screen/");
        String destPath = screenPath + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".jpg";
        SeleniumUtil.screenshot(driver.get(), destPath);
        return destPath;
    }

    /**
     * 根据测试方法全名获取错误截图路径列表
     *
     * @param testFullName 测试方法全名（全类名.方法名）
     * @return 错误截图路径列表
     */
    public List<String> getScreenshotList(String testFullName) {
        screenshotMap.computeIfAbsent(testFullName, k -> new ArrayList<>());
        return screenshotMap.get(testFullName);
    }

    /**
     * 初始化浏览器配置
     */
    private void initBrowserCapMap() {
        ChromeOptions chrome = new ChromeOptions();
        if (SystemUtil.isMac() && driverInfo.isMaximize()) {
            chrome.addArguments("--kiosk");
        }
        String useHeadless = EngineProperties.get(EngineConfig.HEADLESS);
        if (StringUtils.isNotBlank(useHeadless) && useHeadless.equals(Boolean.TRUE.toString())) {
            log.info("设置开启chrome无头浏览器模式");
            chrome.addArguments("-headless");
            chrome.addArguments("window-size=1920,1080");
        }
        if (driverInfo.isMobile()) {
            log.info("设置开启为手机模式");
            chrome.addArguments("--user-agent=Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/%s Mobile Safari/537.36");
            chrome.addArguments("--x-rpc-client_type=5");

            chrome.addArguments("--sec-ch-ua-platform=\"Android\"");
            chrome.addArguments("--disable-features=UserAgentClientHint");
        }


        browserOptionsMap.put(BrowserConstant.CHROME, chrome);

        FirefoxOptions firefox = new FirefoxOptions();
        browserOptionsMap.put(BrowserConstant.FIREFOX, firefox);

        InternetExplorerOptions ie = new InternetExplorerOptions();
        //使得ie每次启动的端口不会随机变化
        ie.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        browserOptionsMap.put(BrowserConstant.IE, ie);

        SafariOptions safari = new SafariOptions();
        browserOptionsMap.put(BrowserConstant.SAFARI, safari);
    }
}

