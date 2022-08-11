package com.qalain.ui.util;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
public class SystemUtil {

    private static final String OS = "os.name";

    /**
     * 判断系统是否为linux
     * @return true：是，false：不是
     */
    public static boolean isLinux() {
        return System.getProperty(OS).toLowerCase().contains("linux");
    }

    /**
     * 判断系统是否为windows
     * @return  true：是，false：不是
     */
    public static boolean isWindows() {
        return System.getProperty(OS).toLowerCase().contains("windows");
    }

    /**
     * 判断系统是否为mac
     * @return true：是，false：不是
     */
    public static boolean isMac() {
        return System.getProperty(OS).toLowerCase().contains("mac");
    }
}
