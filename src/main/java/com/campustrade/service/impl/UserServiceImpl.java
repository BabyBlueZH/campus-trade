package com.campustrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campustrade.common.BusinessException;
import com.campustrade.dto.request.RegisterRequest;
import com.campustrade.entity.User;
import com.campustrade.mapper.UserMapper;
import com.campustrade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** 表明这是一个 Spring 管理的 Service 层组件来创建Bean，
 类上面必须加 @Service 注解，Spring 才会把它放进容器，@Autowired才能注入成功。
 **/
@Service
public class UserServiceImpl implements UserService {

    //字段注入
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    //构造器注入
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder){
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

}
