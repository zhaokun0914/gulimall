package com.atguigu.common.exception;

/**
 * 错误码和错误信息定义类。
 * 错误码为 5 位数字：前两位表示业务场景，后三位表示错误码。
 * 例如 10001：10-通用，001-参数格式校验失败。
 *
 * 业务场景：
 *  10：通用
 *  11：商品
 *  12：订单
 *  13：购物车
 *  14：物流
 */
public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VAILD_EXCEPTION(10001, "参数格式校验失败");

    private final int code;
    private final String msg;

    BizCodeEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
