package com.qalain.ui.suite.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Data
public class SuiteTestFlowData {
    private SuiteDriver suiteDriver;

    private Map<String, SuiteElement> suiteElementMap;

    private SuiteFlow suiteFlow;

    public SuiteTestFlowData() {}

    public SuiteTestFlowData(SuiteDriver suiteDriver, List<SuiteElement> suiteElementList, SuiteFlow suiteFlow) {
        this.suiteDriver = suiteDriver;
        this.suiteFlow = suiteFlow;
        this.suiteElementMap = new HashMap<>();
        for (SuiteElement suiteElement : suiteElementList) {
            this.suiteElementMap.put(suiteElement.getRefId(), suiteElement);
        }
    }

    @Override
    public String toString() {
        if (suiteFlow != null) {
            return suiteFlow.getName() + "【" + suiteFlow.getDesc() + "】";
        }
        return JSON.toJSONString(this);
    }
}
