package com.project.utils.common.base;

public enum HttpCode {

    /**
     * 状态，中文说明
     */
    CODE_200(200, "访问成功"),
    CODE_401(401, "非法请求，参数错误"),
    CODE_402(402, "未授权，accessToken过期或无效"),
    CODE_403(403, "Method使用错误，请查看API"),
    CODE_404(404, "找不到访问地址"),
    CODE_406(406, "没有权限访问"),
    CODE_500(500, "系统内部错误"),
    CODE_501(501, "对象已存在，请检测唯一性"),
    CODE_502(502, "当前版本与服务器版本不一致,请更新应用"),
    CODE_503(503, "对象不存在，请检测请求参数"),
    CODE_504(504, "服务器连接错误，请稍后重试"),
    CODE_505(505, "key校验失败"),
    CODE_506(506, "RPC服务连接失败，请稍后重试");

    public Integer code;
    public String msg;

    HttpCode(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

}
