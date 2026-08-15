package com.example.demo.service;

import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User register(String username, String password) {
        // 校验 username 是否为空，为空则 throw new BusinessException(40001, "username不能为空");
        if (username.isEmpty()){
            throw new BusinessException(40001, "username不能为空");
        }
        // 校验 username 长度是否超过20
        if (username.length()>20){
            throw new BusinessException(40002, "username 超过20字符");
        }

        // 校验 password 是否为空
        if (password.isEmpty()){
            throw new BusinessException(40004, "password 为空");
        }

        // 校验 password 长度是否在8-30之间
        if (password.length()<8||password.length()>30){
            throw new BusinessException(40005, "password 长度不合法");
        }

        //密码复杂度校验（正则表达式）
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).+$")) {
            throw new BusinessException(40007, "password 不符合规则（必须包含字母、数字和特殊字符）");
        }

        // 检查用户名是否已存在（用 userRepository.findByUsername）
        //         提示：Optional有个方法 isPresent() 可以判断是否存在
        if(userRepository.findByUsername(username).isPresent()){
            throw new BusinessException(40006, "username 已存在");
        }

        // 生成新User对象，userId用 UUID.randomUUID().toString()
        //         密码要用 passwordEncoder.encode(password) 加密后存入 passwordHash
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));

        // 调用 userRepository.save(user) 存入数据库，并返回
        return userRepository.save(user);
    }

    public LoginResponse login(String username, String password){
        //40001: username 为空
        if (username.isEmpty()){
            throw new BusinessException(40001, "username不能为空");
        }

        //40004: password 为空
        if (password.isEmpty()){
            throw new BusinessException(40004, "password 为空");
        }

        //根据username查找用户
        //找到用户后，校验密码是否正确
        Optional<User> userOpt = userRepository.findByUsername(username);
        //40010: 用户名或密码错误
        if (userOpt.isEmpty()) {
            throw new BusinessException(40010, "用户名或密码错误");
        }
        User user = userOpt.get();  // 这里再用.get()是安全的，因为上面已经确认存在了
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BusinessException(40010, "用户名或密码错误");
        }

        //校验成功，生成token
        String token = jwtUtil.generateToken(user.getUserId());

        LoginResponse loginResponse=new LoginResponse();
        loginResponse.setUserId(user.getUserId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setToken(token);

        return loginResponse;
    }
}