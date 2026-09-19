package com.campustrade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring 启动时要做一件事：找到所有"配置类"，执行里面的 @Bean 方法，把返回值放进容器。
 *
 * @Bean 本身不是一个能让 Spring 发现你的标记——它只是说"这个方法产出一个 Bean"。真正被扫描的是 @Configuration（或者被 @Component 系列标记的类）。
 *
 * 所以链条是：Spring 扫描 → 发现 @Configuration 类 → 遍历里面的方法 → 看到 @Bean → 执行它 → 结果注册进容器
 * 没有 @Configuration，Spring 压根走不到你的方法跟前。
 */

@Configuration    // 配置类
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 密码编码器
    }
}

