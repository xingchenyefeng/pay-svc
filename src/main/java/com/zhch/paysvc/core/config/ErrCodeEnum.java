package com.zhch.paysvc.core.config;

/**
 * @author lumos
 */
public enum ErrCodeEnum {
    /**
     * 正常代码
     */
    SUCCESS("0", "OK"),
    P4000("P4000", "缺少请求头部参数"),
    P4001("P4001", "参数校验失败"),
    P4002("P4002", "参数类型转换异常"),
    P4003("P4003", "处理请求method不匹配"),
    P4004("P4004", "请求方法的参数绑定异常"),
    P4005("P4005", "请求参数异常或者格式不正确"),
    P4006("P4006", "请求参数异常或者格式不正确"),
    P4007("P4007", "认证失败或者已无效，请登录"),
    P4008("P4008", "您没有权限访问此接口"),
    P4009("P4009", "用户不存在"),
    P4010("P4010", "跨域攻击异常"),
    P4011("P4011", "您需要购买此课程"),
    P5000("P5000", "程序异常"),
    P5001("P5001", "系统开小差了，有人要扣奖金了"),
    P5002("P5002", "未知异常");


    private final String code;
    private final String message;

    ErrCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
