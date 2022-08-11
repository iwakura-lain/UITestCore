package com.qalain.ui.suite.entity;

import lombok.Data;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Data
public class SuiteElement {
    /**
     * 元素唯一标识
     */
    private String refId;

    /**
     * 元素类型
     */
    private String type;

    /**
     * 元素描述
     */
    private String comment;

    /**
     * id定位
     */
    private String id;

    /**
     * name定位
     */
    private String name;

    /**
     * css定位
     */
    private String css;

    /**
     * xpath定位
     */
    private String xpath;

    /**
     * a标签的文本定位
     */
    private String linkText;

    /**
     * a标签的部分文本定位
     */
    private String partialLinkText;

    private long waitTime;
}
