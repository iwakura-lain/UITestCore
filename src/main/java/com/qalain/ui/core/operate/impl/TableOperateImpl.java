package com.qalain.ui.core.operate.impl;

import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.core.operate.TableOperate;
import com.qalain.ui.core.strategy.FindStrategyContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
@Component
public class TableOperateImpl implements TableOperate {
    @Resource
    private FindStrategyContext findStrategyContext;

    @Override
    public List<String> getTableHeader(BaseElement element) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        List<String> tableHeader = new ArrayList<>();
        WebElement thElement = webElement.findElement(By.tagName("th"));
        List<WebElement> tdElements = thElement.findElements(By.tagName("td"));
        for (WebElement tdElement : tdElements) {
            tableHeader.add(tdElement.getText());
        }
        return tableHeader;
    }

    @Override
    public List<List<String>> getTableBody(BaseElement element) {
        WebElement webElement = findStrategyContext.getStrategy(WebElement.class).find(element);
        List<List<String>> tableBody = new ArrayList<>();
        List<WebElement> trElements = webElement.findElements(By.tagName("tr"));
        for (WebElement trElement : trElements) {
            List<String> currentTrResult = new ArrayList<>();
            List<WebElement> tdElements = trElement.findElements(By.tagName("td"));
            for (WebElement tdElement : tdElements) {
                currentTrResult.add(tdElement.getText());
            }
            tableBody.add(currentTrResult);
        }
        return tableBody;
    }
}
