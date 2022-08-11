package com.qalain.ui.core.entity;

import lombok.Data;

/**
 * @author lain
 * @Description 浏览器属性
 * @create 2022-01-21 14:18
 */
@Data
public class DriverInfo {
    /**
     * 浏览器名称
     */
    private String browser;

    /**
     * 远程驱动地址
     */
    private String remoteAddress;

    /**
     * 是否最大化
     */
    private boolean maximize;

    /**
     * 浏览器窗口宽度
     */
    private int width;

    /**
     * 浏览器窗口高度
     */
    private int height;

    /**
     * 工具栏高度
     */
    private int toolbarHeight;

    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 是否为手机模式启动
     */
    private boolean isMobile;

    public DriverInfo() {
        this.timeout = 30000;
    }
}
