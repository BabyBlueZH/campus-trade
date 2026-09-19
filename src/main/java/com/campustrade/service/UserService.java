package com.campustrade.service;

import com.campustrade.dto.request.RegisterRequest;
import com.campustrade.entity.User;
import org.springframework.stereotype.Service;

/**
 * register() 要做的三件事：
 *
 * 1.查重：用 userMapper.selectCount(...)
 *      看 username 是否已存在
 *      → 存在就 throw new BusinessException("用户名已被注册")
 * 2.加密：把明文密码换成 passwordEncoder.encode(req.getPassword())
 * 3.落库：组装 User 对象 → userMapper.insert(user)
 *      → 返回前把 password 置为 null（别把哈希传给前端）
 *
 * role 和 status 不用手动设——数据库有默认值 0 和 1，
 *      MP 的 insert 会跳过 null 字段。
 */

public interface UserService {

    /** 注册新用户，返回创建好的用户（不含密码） */
    User register(RegisterRequest req);
}

