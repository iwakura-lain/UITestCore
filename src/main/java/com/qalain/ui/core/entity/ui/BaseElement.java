package com.qalain.ui.core.entity.ui;

import lombok.Data;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Data
public class BaseElement {

    private String id;

    private String name;

    private String css;

    private String xpath;

    private String linkText;

    private String partialLinkText;

    /**
     * 元素操作后的等待时间
     */
    private long waitTime;
}
