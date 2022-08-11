package com.qalain.ui.util;

import org.apache.commons.lang3.BooleanUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
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
}
