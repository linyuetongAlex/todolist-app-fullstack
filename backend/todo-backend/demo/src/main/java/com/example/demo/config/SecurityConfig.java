package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //告诉 Spring"这个类是用来做配置的"，Spring 启动时会读取里面的内容
public class SecurityConfig {

    @Bean //告诉 Spring"把这个方法的返回值也交给你管理"，之后任何地方只要写 @Autowired private PasswordEncoder passwordEncoder; 就能直接拿到这个对象用，不用自己 new
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}