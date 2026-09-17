package com.campustrade.common;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;


/**
 * 全局异常处理器
 * <p>
 * 统一拦截 Controller 抛出的异常，转成 {@link Result} 结构返回，
 * 避免每种异常都要在 Controller 里单独 try-catch。
 */
@Slf4j    // 日志   ==private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
@RestControllerAdvice    // 统一处理异常

public class GlobalExceptionHandler {

    /** 业务异常 —— 自己抛的，有明确的 code 和 message */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    /** 请求体校验失败（@RequestBody + @Valid） */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return Result.badRequest(msg);
    }

    /** URL 参数校验失败（@RequestParam + 类上 @Validated） */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining("; "));
        return Result.badRequest(msg);
    }

    /**
     * 接口不存在 —— URL 写错时走这里
     * 不处理的话会被下面的兜底方法当成 500，误导排查方向
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNotFound(NoResourceFoundException e) {
        return Result.notFound("接口不存在: " + e.getResourcePath());
    }

    /** 兜底 —— 没被上面任何方法接住的异常都走这里 */
    @ExceptionHandler(Exception.class)    // 捕获所有异常
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);              // 堆栈打给开发者看
        return Result.serverError("服务器内部错误"); // 不暴露细节给前端
    }
}
