package com.qalain.ui.util;

import com.qalain.ui.config.EngineConfig;
import com.qalain.ui.core.engine.EngineProperties;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
public class ParamParseUtil {

    /**
     * 解析webELement引用参数
     *
     * @param param    参数内容
     * @return 解析参数后的数据内容
     */
    public static String parseWebElementReferenceParam(String param) {
        if (StringUtils.isNotBlank(param)) {
            Matcher matcher = REFERENCE_PARAM_PATTEARN.matcher(param);
            while (matcher.find()) {
                String referenceParamKey = matcher.group(1);
                Object value = System.getProperty(referenceParamKey, "");
                param = param.replace(matcher.group(), value.toString());
            }
        }
        return param;
    }

    /**
     * 解析引用参数
     * @param param 参数内容
     * @return 解析参数后的数据内容
     */
    public static String parseReferenceParam(String param) {
        if (StringUtils.isNotBlank(param)) {
            Matcher matcher = REFERENCE_PARAM_PATTEARN.matcher(param);
            String env = EngineProperties.get(EngineConfig.TEST_ENV);
            while (matcher.find()) {
                String referenceParamKey = matcher.group(1);
                if (StringUtils.isNotBlank(env)) {
                    referenceParamKey += "." + env;
                }
                Object value = EngineProperties.get(referenceParamKey, "");
                param = param.replace(matcher.group(), value.toString());
            }
        }
        return param;
    }

    /**
     * 引用参数匹配正则
     */
    public static final String REFERENCE_PARAM_REG = "\\$\\{(.+?)\\}";

    /**
     * 引用参数匹配模式
     */
    public static final Pattern REFERENCE_PARAM_PATTEARN = Pattern.compile(REFERENCE_PARAM_REG);
}

