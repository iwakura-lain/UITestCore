package com.qalain.ui.config;

/**
 * @author lain
 * @Description engine.properties 参数项
 * @create 2022-01-21
 */
public class EngineConfig {
    /**
     * engine配置文件名称
     */
    public static final String ENGINE_CONFIG_PATH = "engine.properties";

    /**
     * 自定义的包扫描路径
     */
    public static final String COMPONENT_SCAN_PACKAGE = "scan.base.package";

    /**
     * 测试报告存放路径
     */
    public static final String REPORT_PATH = "report.path";

    /**
     * 异常截图存放路径
     */
    public static final String SCREENSHOT_PATH = "screenshot.path";

    /**
     * 测试人员
     */
    public static final String TEST_PERSON = "test.person";

    /**
     * 测试环境
     */
    public static final String TEST_ENV = "test.env";

    /**
     * 谷歌driver驱动
     */
    public static final String CHROME_DRIVER = "chrome.driver";

    /**
     * 火狐driver驱动
     */
    public static final String FIREFOX_DRIVER = "firefox.driver";

    /**
     * safari driver驱动
     */
    public static final String SAFARI_DRIVER = "safari.driver";

    /**
     * ui自动化流程文件路径配置
     */
    public static final String TEST_SUITE_PATH = "test.suite.path";

    /**
     * page xml path
     */
    public static final String TEST_PAGE_PATH = "test.page.path";

    /**
     * 是否使用无头浏览器
     */
    public static final String HEADLESS = "headless";

    /**
     * 远程服务模式
     */
    public static final String REMOTE_ADDRESS = "driver.remote.address";
}
