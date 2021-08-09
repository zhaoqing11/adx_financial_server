package com.project.entity.base;

/**
 * 用户相关枚举
 *
 * @author zhao
 * @create 2018-01-24 下午 4:09
 */
@SuppressWarnings("all")
public enum UserEnum {

    /** 状态码范围 1-11**/
    SELECT_ERROR(1,"查询出错!"),

    USER_ACCOUNT_IS_NULL(2,"账号为空!"),

    USER_PASSWORD_IS_NULL(3,"密码为空!"),

    USER_NOT_EXIST(4,"用户不存在!"),

    USER_PASSWORD_ERROR(5,"密码错误"),

    USER_UPDATE_PARAMERROR(6,"参数异常"),

    USER_ROLE_IS_UNLAWFULNESS(7,"用户权限非法"),

    USER_LOGIN_EXIST(8,"用户未登录"),

    USER_SELECT_NOTEXIST_ERROR(9,"没有查询到用户信息"),

    USER_NICK_ERROR(10,"用户名中不能包含关键字nick"),

    USER_IS_DISABLED(11,"用户已被禁用，请联系管理员"),
    ;

    public static final int SUPER_MANAGER = 0;

    public static final String CACHE_USERID = "cacheUserId";

    public static final String CACHE_USERDISABLE = "cacheUserDisable";

    public static final String CACHE_USERACCOUNT = "cacheUserName";

    public static final String CACHE_USERREALNAME = "cacheRealName";

    public static final String CACHE_USERROLETYPE = "cacheUserRoleType";

    public static final String CACHE_USERAUTHLIST = "cacheUserAuthList";

    public static final String CACHE_USERMOBLIE = "cacheUserMoblie";

    public static final String CACHE_USER_TOKEN = "cacheTokenId";

    public static final String CACHE_USER_API = "cacheUserApi";


    private String message;

    private int status;

    public String getMessage() {

        return message;

    }

    public int getStatus() {

        return status;

    }

    private UserEnum(int status, String message) {

        this.status = status;

        this.message = message;

    }

}
