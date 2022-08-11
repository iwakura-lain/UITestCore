package com.qalain.ui.suite.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Data
public class AutoTestData {
    private SuiteDriver suiteDriver;

    private List<SuiteElement> elementInfoList;

    private List<SuiteFlow> suiteFlows;
}
