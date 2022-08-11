package com.qalain.ui.suite.entity;

import com.qalain.ui.core.entity.DriverInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SuiteDriver extends DriverInfo {
    private long actionBeforeWaitTime;

    private long actionAfterWaitTime;
}
