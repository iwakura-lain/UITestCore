package com.qalain.ui.util;

import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Slf4j
public class FreemarkerTemplateUtil {

    // 模板名称
    private static String defaultTemplateName = "page.ftl";

    private FreemarkerTemplateUtil() {
    }

    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_22);

    static {
        try {
            String basePath = "/template";
            CONFIGURATION.setClassForTemplateLoading(FreemarkerTemplateUtil.class, basePath);
            CONFIGURATION.setDefaultEncoding("UTF-8");
            CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            CONFIGURATION.setCacheStorage(NullCacheStorage.INSTANCE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Template getTemplate(String templateName) throws IOException {
        return CONFIGURATION.getTemplate(templateName);
    }

    public static String process(Map<String, Object> dataMap, String templateName) {
        if (templateName == null) {
            templateName = defaultTemplateName;
        }
        StringWriter out = new StringWriter();
        try {
            getTemplate(templateName).process(dataMap, out);
        } catch (TemplateException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return out.toString();
    }

    /**
     * 根据模板生成文件
     * @param dataMap 数据源
     * @param destFileName 目标文件名称前缀
     * @return
     */
    public static void generatePageCodeFile(Map<String, Object> dataMap,String destFileName) {
        String filePath = destFileName + ".java";
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        try {
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            generateFileByTemplate(file, dataMap, defaultTemplateName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据模板生成文件
     * @param file
     * @param dataMap
     * @throws Exception
     */
    private static void generateFileByTemplate(File file, Map<String, Object> dataMap,String templateName) throws Exception {
        Template template = FreemarkerTemplateUtil.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8), 10240);
        template.process(dataMap, out);
        clearCache();
    }

    public static void clearCache() {
        CONFIGURATION.clearTemplateCache();
    }
}
