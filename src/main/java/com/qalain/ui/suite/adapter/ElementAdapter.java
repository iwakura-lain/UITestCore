package com.qalain.ui.suite.adapter;

import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.suite.entity.SuiteElement;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
public class ElementAdapter {
    public static BaseElement getBaseElement(SuiteElement suiteElement) {
        if (suiteElement == null) {
            return null;
        }
        BaseElement baseElement = new BaseElement();
        baseElement.setId(suiteElement.getId());
        baseElement.setName(suiteElement.getName());
        baseElement.setCss(suiteElement.getCss());
        baseElement.setLinkText(suiteElement.getLinkText());
        baseElement.setPartialLinkText(suiteElement.getPartialLinkText());
        baseElement.setXpath(suiteElement.getXpath());
        baseElement.setWaitTime(suiteElement.getWaitTime());
        return baseElement;
    }
}
