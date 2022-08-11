package com.qalain.ui.exceptions;

/**
 * @author lain
 * @Description
 * @create 2022-01-22
 */
public class ElementNotFoundException  extends AutoUiTestException {

    private String message;

    public ElementNotFoundException() {
    }

    public ElementNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return String.format("%s,未找到页面UI元素,%s", super.getMessage(), message);
    }
}