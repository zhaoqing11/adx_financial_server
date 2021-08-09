package com.project.utils.common.base;

public enum ReturnEntityEnum {

    SUCCESS(200, "成功");


    private String message;

    private int status;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    private ReturnEntityEnum(int status, String message) {

        this.status = status;

        this.message = message;

    }
}
