package com.campustrade.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册请求参数
 * <p>
 * 注意：这里只有客户端该传的字段——绝不包含 role / status，
 * 否则前端可以自己传 role=1 把自己变成管理员（越权漏洞）。
 */
@Data
public class RegisterRequest {//注册入参 + 校验

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度需在 3-20 之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度需在 6-30 之间")
    private String password;

    @Size(max = 30, message = "昵称最长 30 个字符")
    private String nickname;

    /** 手机号可不填；填了必须符合中国大陆手机号格式 */
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    //左：^ 匹配字符串开头，$ 匹配字符串结尾，中间无内容 → 代表空字符串 ""，允许手机号填空、不填写
    //右：^1[3-9]\\d{9}$ 匹配以 1 开头，第二位是 3-9 之间的数字，后面跟着 9 位数字的字符串
    private String phone;
}
