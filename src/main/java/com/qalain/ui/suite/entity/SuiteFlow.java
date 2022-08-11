package com.qalain.ui.suite.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Data
public class SuiteFlow {
    private String author;

    private String name;

    private String desc;

    private List<FlowStep> flowStepList = new ArrayList<>();

    public void addFlowStep(FlowStep flowStep) {
        if (flowStep != null) {
            flowStepList.add(flowStep);
        }
    }
}
