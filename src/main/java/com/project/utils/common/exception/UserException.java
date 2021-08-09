package com.project.utils.common.exception;

/**
 * 自定义用户异常
 * @author zhao
 */
@SuppressWarnings("all")
public class UserException extends RuntimeException {

    private int statusCode;


    public UserException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
