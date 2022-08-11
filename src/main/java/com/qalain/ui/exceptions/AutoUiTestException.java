package com.qalain.ui.exceptions;

/**
 * @author lain
 * @Description
 * @create 2022-01-21
 */
public class AutoUiTestException extends RuntimeException {

    private String message;

    public AutoUiTestException(String message) {
        this.message = message;
    }

    public AutoUiTestException() {}

    @Override
    public String getMessage() {
        return super.getMessage() + "-" + message;
    }
}
