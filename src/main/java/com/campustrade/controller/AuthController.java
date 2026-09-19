package com.campustrade.controller;


import com.campustrade.common.Result;
import com.campustrade.dto.request.RegisterRequest;
import com.campustrade.entity.User;
import com.campustrade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController //RESTful 风格的控制器
@RequestMapping("/api/auth")    // URL 前缀
public class AuthController {

    /**
     * 字段注入
     * @Autowired
     * private UserService userService;
     */

    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    //@RequestMapping 不限定方法 —— GET、POST、PUT、DELETE 全都会命中这个接口。后果：
    //语义错误（RESTful 要求注册用 POST）
    //安全隐患：GET /api/auth/register?username=xx&password=yy 也能注册成功。
    // 而 GET 会被浏览器预取、爬虫抓取、日志记录——密码直接进日志
    public Result<User> register(@RequestBody @Valid RegisterRequest req) {
        return Result.success(userService.register(req));
    }
}

//（@Valid 作用：
// 会触发 DTO 上的校验，失败自动被你的 GlobalExceptionHandler 接住）
//这个注解会自动校验 `RegisterRequest` DTO 里的字段规则（比如非空、长度限制），
// 如果校验失败，异常会被全局异常处理器捕获，
