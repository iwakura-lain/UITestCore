package com.qalain.ui.util;

import org.apache.commons.lang3.BooleanUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
public class ReadPropertiesUtil {
    public static String getProp(String filename, String folder, String propName) throws IOException {
        Properties props = new Properties();
        props.load(ReadPropertiesUtil.class.getClassLoader().getResourceAsStream( folder + "/" + filename + ".properties"));
        return props.getProperty(propName);
    }

    public static String getProp(String filename, String propName) {
        Properties props = new Properties();
        try {
            props.load(ReadPropertiesUtil.class.getClassLoader().getResourceAsStream( filename + ".properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props.getProperty(propName);
    }

    public static boolean getBooleanProp(String filename, String propName) {
        Properties props = new Properties();
        try {
            props.load(ReadPropertiesUtil.class.getClassLoader().getResourceAsStream( filename + ".properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BooleanUtils.toBoolean(props.getProperty(propName));
    }

    public static Map<String, String> properties2Map(String propertiesPath, String propertiesFileName) throws IOException {
        InputStream inputStream = ReadPropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesPath + propertiesFileName);

        Properties properties = new Properties();
        properties.load(inputStream);

        Map<String, String> res = new HashMap<String, String>((Map)properties);

        return res;
    }

    public static String setProperty(String key, String value, String propertiesPath, String propertiesFileName) throws IOException {
        InputStream inputStream = ReadPropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesPath + propertiesFileName);

        Properties properties = new Properties();
        properties.load(inputStream);
        properties.getProperty(key);
        properties.setProperty(key, new String(value));
        InputStream inputStream2 = ReadPropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesPath + propertiesFileName);

        properties.load(inputStream2);
        return value;
    }
}
