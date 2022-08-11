package com.qalain.ui.core.engine;

import com.qalain.ui.config.EngineConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author lain
 * @Description：获取 Engine.properties 的 value
 * @create 2022-01-21 14:16
 */
@Slf4j
public class EngineProperties {
    private static Properties properties = new Properties();

    static {
        try (InputStream in = EngineProperties.class.getResourceAsStream("/" + EngineConfig.ENGINE_CONFIG_PATH);
             BufferedReader bf = new BufferedReader(new InputStreamReader(in))) {
            properties.load(bf);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
