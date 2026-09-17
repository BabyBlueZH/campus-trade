package com.campustrade.common;


import lombok.Getter;

/**
 * 自定义业务异常
 * <p>
 * 业务逻辑出问题时抛出（"用户不存在"、"用户名已被注册"、"商品已下架"），
 * 由 GlobalExceptionHandler 自动转成统一的 Result 返回。
 * <p>
 * 用法：throw new BusinessException(404, "商品不存在");
 */
@Getter
public class BusinessException extends RuntimeException {// 自定义业务异常

    private final int code; // 业务状态码，比如 404、400

    public BusinessException(int code, String message) {
        super(message);// 调用父类的构造方法，将 message 传给 RuntimeException
        this.code = code;// 设置业务状态码
    }

    /** 快捷构造：默认状态码 400（参数/业务错误） */
    public BusinessException(String message) {
        this(400, message);// 调用带参构造方法，传入状态码和 message
    }
}
