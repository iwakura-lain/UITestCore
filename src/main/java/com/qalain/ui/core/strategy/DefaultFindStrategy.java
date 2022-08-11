package com.qalain.ui.core.strategy;

import com.qalain.ui.constant.ElementLocatorConstant;
import com.qalain.ui.core.engine.EngineDriver;
import com.qalain.ui.core.entity.ui.BaseElement;
import com.qalain.ui.exceptions.ElementNotFoundException;
import com.qalain.ui.util.ParamParseUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author lain
 * @Description：默认元素查找策略实现
 * @create 2022-01-21
 */
@Component(ElementLocatorConstant.DEFAULT_FIND_STRATEGY)
public class DefaultFindStrategy implements ElementFindStrategy<WebElement> {

    @Autowired
    private EngineDriver engine;

    @Override
    public WebElement find(BaseElement element) {
        By by = null;

        if (StringUtils.isNotBlank(element.getId())) {
            by = By.id(ParamParseUtil.parseWebElementReferenceParam(element.getId()));
        } else if (StringUtils.isNotBlank(element.getName())) {
            by = By.name(ParamParseUtil.parseWebElementReferenceParam(element.getName()));
        } else if (StringUtils.isNotBlank(element.getCss())) {
            by = By.className(ParamParseUtil.parseWebElementReferenceParam(element.getCss()));
        } else if (StringUtils.isNotBlank(element.getLinkText())) {
            by = By.linkText(ParamParseUtil.parseWebElementReferenceParam(element.getLinkText()));
        } else if (StringUtils.isNotBlank(element.getPartialLinkText())) {
            by = By.partialLinkText(ParamParseUtil.parseWebElementReferenceParam(element.getPartialLinkText()));
        } else if (StringUtils.isNotBlank(element.getXpath())) {
            by = By.xpath(ParamParseUtil.parseWebElementReferenceParam(element.getXpath()));
        }
        WebElement webElement = fluentWait(by);
        if (webElement == null) {
            throw new ElementNotFoundException(element.toString());
        }
        return webElement;
    }

    public WebElement fluentWait(By locator) {
        WebDriver driver = engine.getThreadLocalDriver();
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

        return wait.until(driver1 -> driver1.findElement(locator));
    }
}

