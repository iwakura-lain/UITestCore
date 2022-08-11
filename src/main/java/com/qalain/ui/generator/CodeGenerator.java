package com.qalain.ui.generator;

import com.qalain.ui.config.EngineConfig;
import com.qalain.ui.core.engine.EngineProperties;
import com.qalain.ui.util.FreemarkerTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qalain.ui.core.entity.ui.*;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Slf4j
public class CodeGenerator {
    private static Map<String, String> fieldTypeMap = new HashMap<>(4);

    static {
        fieldTypeMap.put("text", Text.class.getSimpleName());
        fieldTypeMap.put("button", Button.class.getSimpleName());
        fieldTypeMap.put("select", Selector.class.getSimpleName());
        fieldTypeMap.put("checkbox", CheckBox.class.getSimpleName());
        fieldTypeMap.put("table", Table.class.getSimpleName());
    }

    /**
     * 生成Java代码的Page类
     * @param xmlFilePath xml配置文件
     * @param outputDir 输出目录（包路径）
     */
    public static void generate(String xmlFilePath, String outputDir) {
        if (StringUtils.isBlank(xmlFilePath)) {
            log.error("xml配置文件路径为空，无法生成");
            return;
        }
        ClassLoader classLoader = CodeGenerator.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(xmlFilePath)) {
            Document document = new SAXReader().read(inputStream);
            Element rootElement = document.getRootElement();
            Element pagesElement = rootElement.element("pages");
            if (pagesElement == null) {
                return;
            }
            //包路径
            String pagePackage = pagesElement.attributeValue("pagePackage");
            List pageElements = pagesElement.elements("page");
            for (Object pageElement : pageElements) {
                PageData pageData = new PageData();
                pageData.setPackageName(pagePackage);
                if (pageElement instanceof Element) {
                    Element page = (Element) pageElement;
                    String pageClass = page.attributeValue("pageClass", "");
                    if (StringUtils.isBlank(pageClass)) {
                        log.error("page标签下未找到pageClass属性");
                        continue;
                    }
                    pageData.setName(pageClass);
                    pageData.setComment(page.attributeValue("comment", ""));
                    //获取page下的field
                    List fieldElementList = page.elements("field");
                    for (Object fieldElement : fieldElementList) {
                        if (fieldElement instanceof Element) {
                            Element field = (Element) fieldElement;
                            String type = fieldTypeMap.get(field.attributeValue("type"));
                            pageData.addField(type, field.attributeValue("name"), field.attributeValue("comment", ""));
                            if (!pageData.isContainsText() && Text.class.getSimpleName().equals(type)) {
                                pageData.setContainsText(true);
                            }
                            if (!pageData.isContainsButton() && Button.class.getSimpleName().equals(type)) {
                                pageData.setContainsButton(true);
                            }
                            if (!pageData.isContainsCheckBox() && CheckBox.class.getSimpleName().equals(type)) {
                                pageData.setContainsCheckBox(true);
                            }
                            if (!pageData.isContainsSelect() && Selector.class.getSimpleName().equals(type)) {
                                pageData.setContainsSelect(true);
                            }
                            if (!pageData.isContainsTable() && Table.class.getSimpleName().equals(type)) {
                                pageData.setContainsTable(true);
                            }
                        }
                    }
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("page", pageData);
                    String destFileName = pagePackage + "." + pageClass;
                    destFileName = outputDir + "/" +  destFileName.replaceAll("[.]", "/");
                    FreemarkerTemplateUtil.generatePageCodeFile(dataMap, destFileName);
                    FreemarkerTemplateUtil.clearCache();
                }
            }
        } catch (IOException | DocumentException e) {
            log.error("解析配置文件【{}】出现异常", xmlFilePath, e);
        }
    }

    /**
     * 用来给 maven 调用的
     * @param args
     */
    public static void main(String[] args) {
        String testPagePath = EngineProperties.get(EngineConfig.TEST_PAGE_PATH);
        for (String s : testPagePath.split(",")) {
            CodeGenerator.generate(s, "src/main/java");
        }
    }
}
