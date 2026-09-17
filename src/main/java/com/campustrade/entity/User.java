package com.campustrade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体 —— 对应数据库 user 表
 */
@Data
@TableName("user")
public class User {

    /** 主键，数据库自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 登录名（唯一） */
    private String username;

    /** 密码（BCrypt 加密后存储，绝不存明文） */
    private String password;

    private String nickname;
    private String phone;
    private String avatar;

    /** 0-普通用户 1-管理员 */
    private Integer role;

    /** 0-禁用 1-正常 */
    private Integer status;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除 1-已删除（MP 自动过滤） */
    @TableLogic
    private Integer isDeleted;
}
