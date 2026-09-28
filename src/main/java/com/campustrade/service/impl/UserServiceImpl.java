package com.campustrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campustrade.common.BusinessException;
import com.campustrade.common.JwtUtil;

import com.campustrade.dto.request.LoginRequest;
import com.campustrade.dto.request.RegisterRequest;
import com.campustrade.entity.User;
import com.campustrade.mapper.UserMapper;
import com.campustrade.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/** 表明这是一个 Spring 管理的 Service 层组件来创建Bean，
 类上面必须加 @Service 注解，Spring 才会把它放进容器，@Autowired才能注入成功。
 **/
@Service
public class UserServiceImpl implements UserService {

    /**
     * 字段注入
     * @Autowired
     * private UserMapper userMapper;
     *
     * @Autowired
     * private PasswordEncoder passwordEncoder;
     */

    //构造器注入
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public User register(RegisterRequest req){
        //1.查重用户名
        //2.密码加密
        //3.插入数据库
        //业务逻辑：
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().
                        eq(User::getUsername, req.getUsername())
        );
        /**
         * MP 生成的 SQL：
         * SELECT COUNT(*) FROM user WHERE is_deleted = 0 AND username = ?
         * 注意那个 is_deleted = 0——你实体上标了 @TableLogic，MP 自动给所有查询补上这个条件。你不用写，它也不会查已删除的用户。
         * 用法和 selectList 一模一样（都是 LambdaQueryWrapper），区别只是返回一个数字而不是一堆对象。
         */
        if (count != null && count > 0){
            throw new BusinessException("用户名已被注册");
        }
        User user = new User();
        user.setUsername(req.getUsername());

        //密码加密BCrypt:encode()
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setNickname(req.getNickname());
        user.setPhone(req.getPhone());

        userMapper.insert(user);//将用户数据插入到数据库中
        user.setPassword(null);//将密码设置为 null，不返回给前端
        return user;

    }

    @Override
    public String login(LoginRequest req) {
        //1.按用户名username查用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername,
                                req.getUsername())
        );

        //2.校验用户名 + 密码
        if (user == null || !passwordEncoder.
                matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }//match(明文, 密文)
        //用户名不存在 和 密码错误，必须返回同一个提示，攻击者可以通过错误提示来判断用户名对不对

        //3.检查status：为0=账号被禁用-> 拒绝登录
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(403, "账号已被禁用");
        }

        //4.签发Token 并返回
        return jwtUtil.generate(user.getId(), user.getUsername());

        /**
         *为什么密码校验(步骤2)要放在 status 校验(步骤3)前面? 顺序反过来的话,有人拿用户名去试,禁用账号会提前返回
         * 403、正常账号返回 401,等于告诉攻击者"这个用户名存在"——你辛辛苦苦在坑 2
         * 里防的用户名枚举,从后门漏出去了。所以先拿到密码这关,过了再谈状态。
         */
    }

}
