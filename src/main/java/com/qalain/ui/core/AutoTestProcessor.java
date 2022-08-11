package com.qalain.ui.core;

import com.qalain.ui.config.EngineConfig;
import com.qalain.ui.constant.AutoTestConstant;
import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.core.engine.EngineProperties;
import com.qalain.ui.core.entity.DriverInfo;
import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.core.entity.ui.CheckBox;
import com.qalain.ui.core.entity.ui.Selector;
import com.qalain.ui.core.entity.ui.Text;
import com.qalain.ui.core.hook.ShutdownHook;
import com.qalain.ui.core.page.Page;
import com.qalain.ui.exceptions.AutoUiTestException;
import com.qalain.ui.util.BeanUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lain
 * @Description
 * @create 2022-01-22
 */
@Slf4j
public class AutoTestProcessor {

    private static AutoTestProcessor autoTestProcessor;
    /**
     * 保存page对象的集合
     */
    private Map<String, Page> pageMap = new HashMap<>();

    /**
     * spring上下文对象
     */
    private ApplicationContext applicationContext;

    /**
     * driver引擎
     */
    @Getter
    private EngineDriver engine;

    private AutoTestProcessor() {
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

    public static AutoTestProcessor getInstance() {
        if (autoTestProcessor != null) {
            return autoTestProcessor;
        }
        autoTestProcessor = new AutoTestProcessor();
        return autoTestProcessor;
    }

    public <T> T getPage(Class<T> clazz) {
        return (T)pageMap.get(clazz.getName());
    }

    /**
     * 配置文件初始化解析
     *
     * @param xmlFileName 配置文件名称
     */
    public void init(String xmlFileName) {
        loadConfig(xmlFileName);
    }


    /**
     * 加载配置文件
     *
     * @param xmlFileName 配置文件名称
     */
    private void loadConfig(String xmlFileName) {
        if (StringUtils.isBlank(xmlFileName)) {
            log.warn("配置文件名称为空，无法解析");
            return;
        }
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(xmlFileName)) {
            Document document = new SAXReader().read(inputStream);
            parseConfig(document);
        } catch (IOException | DocumentException e) {
            log.error("解析配置文件【{}】错误", xmlFileName, e);
        }
    }

    /**
     * 解析xml配置文件内容
     *
     * @param document document对象
     */
    private void parseConfig(Document document) {
        //获取根节点autoTestUi
        Element rootElement = document.getRootElement();
        //当引擎的driver信息为空时，说明此前并没有进行过加载，此时加载driver引擎参数
        if (engine.getDriverInfo() == null) {
            Element engineElement = rootElement.element("engine");
            this.engine.setDriverInfo(parseDriver(engineElement));
            this.engine.init();
        }
        //解析includePage标签中对应的page文件
        List includePageList = rootElement.elements("includePage");
        for (Object includePageObj : includePageList) {
            if (includePageObj instanceof Element) {
                Element includePage = (Element) includePageObj;
                String pageConfig = includePage.attributeValue("pageConfig");
                loadConfig(pageConfig);
            }
        }
        //解析pages标签中的page内容
        Element pagesElement = rootElement.element("pages");
        if (pagesElement != null) {
            String pagePackage = pagesElement.attributeValue("pagePackage", "");
            if (StringUtils.isNotBlank(pagePackage)) {
                pagePackage = pagePackage.trim() + ".";
            }
            List pageList = pagesElement.elements("page");
            for (Object page : pageList) {
                if (page instanceof Element) {
                    Element pageElement = (Element) page;
                    parsePage(pagePackage, pageElement);
                }
            }
        }
    }

    /**
     * 解析浏览器引擎Driver
     *
     * @param engineElement 浏览器引擎驱动信息Element对象
     * @return 驱动对象
     */
    private DriverInfo parseDriver(Element engineElement) {
        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setBrowser(engineElement.attributeValue("browser"));
        driverInfo.setRemoteAddress(engineElement.attributeValue("remoteAddress"));
        driverInfo.setMaximize(Boolean.valueOf(engineElement.attributeValue("maximize")));
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
        return driverInfo;
    }

    private void parsePage(String pagePackage, Element pageElement) {
        String pageClassStr = pageElement.attributeValue("pageClass");
        if (StringUtils.isBlank(pageClassStr)) {
            log.warn("不能在page标签上找到pageClass属性值");
            return;
        }
        pageClassStr = pagePackage + pageClassStr;
        Class pageClass = null;
        try {
            pageClass = Class.forName(pageClassStr);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new AutoUiTestException(e.getMessage());
        }

        Object pageInstance = applicationContext.getBean(pageClass);
        String pageUrl = pageElement.attributeValue("url");

        if (StringUtils.isNotBlank(pageUrl)) {
            BeanUtil.invokeMethod(pageClass,pageInstance,"setUrl", new Class[]{String.class}, new Object[]{pageUrl});
        }
        //设置page对象各个字段对应的属性值属性
        List fieldsElement = pageElement.elements("field");
        for (Object fieldElement : fieldsElement) {
            if (fieldElement instanceof Element) {
                Element field = (Element) fieldElement;
                String fieldName = field.attributeValue("name");
                if (StringUtils.isBlank(fieldName)) {
                    continue;
                }
                Method filedGetMethod = BeanUtils.findMethod(pageClass, "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                if (filedGetMethod != null) {
                    try {
                        BaseElement baseElement = (BaseElement) filedGetMethod.invoke(pageInstance);
                        baseElement.setId(field.attributeValue("byId"));
                        baseElement.setName(field.attributeValue("byName"));
                        baseElement.setCss(field.attributeValue("byCss"));
                        baseElement.setXpath(field.attributeValue("byXpath"));
                        baseElement.setLinkText(field.attributeValue("byLinkText"));
                        baseElement.setPartialLinkText(field.attributeValue("byPartialLinkText"));
                        String waitTime = StringUtils.defaultIfEmpty(field.attributeValue("waitTime"), "1000");
                        baseElement.setWaitTime(Long.parseLong(waitTime));
                        String value = field.attributeValue("value");
                        if (baseElement instanceof Text) {
                            ((Text)baseElement).setValue(value);
                        } else if (baseElement instanceof Selector) {
                            ((Selector)baseElement).setValue(value);
                        } else if (baseElement instanceof CheckBox) {
                            ((CheckBox)baseElement).setValue(value);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        //将构建完数据的Page对象，放入PageMap集合中
        pageMap.put(pageClassStr, (Page) pageInstance);
    }
}
