package com.project.utils.common.base;

import org.springframework.stereotype.Component;

/**
 * @description: 返回数据对象
 * @author: zhao
 */
@Component
public class ReturnEntity {

    /**
     * 请求路由状态
     */
    private int status;

    /**
     * 原因及提示信息
     */
    private String cause;

    /**
     * 数据信息
     */
    private Object datas;

    public ReturnEntity() {

        super();

    }

    public int getStatus() {

        return status;

    }

    public void setStatus(int status) {

        this.status = status;

    }

    public String getCause() {

        return cause;

    }

    public void setCause(String cause) {

        this.cause = cause;

    }

    public Object getDatas() {

        return datas;

    }

    public void setDatas(Object datas) {

        this.datas = datas;

    }

    @Override
    public String toString() {

        return "ReturnEntity{" +
                "status=" + status +
                ", cause='" + cause + '\'' +
                ", datas=" + datas +
                '}';

    }
}
