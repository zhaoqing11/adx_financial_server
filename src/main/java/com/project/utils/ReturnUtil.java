package com.project.utils;

import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.base.ReturnEntityEnum;

public class ReturnUtil {

    /**
     * 请求成功
     * @return
     */
    public static ReturnEntity success() {
        ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setStatus(ReturnEntityEnum.SUCCESS.getStatus());
        returnEntity.setCause(ReturnEntityEnum.SUCCESS.getMessage());
        return returnEntity;
    }


    /**
     * 请求成功
     * @param datas
     * @return
     */
    public static ReturnEntity success(Object datas) {
        ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setStatus(ReturnEntityEnum.SUCCESS.getStatus());
        returnEntity.setCause(ReturnEntityEnum.SUCCESS.getMessage());
        returnEntity.setDatas(datas);
        return returnEntity;
    }


    /**
     * 请求失败
     * @param cause
     * @return
     */
    public static ReturnEntity fail(Integer status,String cause) {
        ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setStatus(status);
        returnEntity.setCause(cause);
        return returnEntity;
    }

    /**
     * 请求验证失败
     * @param cause
     * @return
     */
    public static ReturnEntity validError(HttpCode httpCode, Object cause) {
        ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setStatus(httpCode.code);
        returnEntity.setCause(cause.toString());
        returnEntity.setDatas(cause);
        return returnEntity;
    }
}
