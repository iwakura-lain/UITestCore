package com.qalain.ui.suite.entity;

import lombok.Data;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Data
public class FlowStep {
    private String action;

    private String url;

    private String refId;

    private String value;

    private String expectValue;

    private String customFunction;

    private String invokerContent;

    private Integer waitTime;

    private String tagFiled;

    private String variableName;

    private String srcImgName;

    private String templateImgName;

    private String text;
}
