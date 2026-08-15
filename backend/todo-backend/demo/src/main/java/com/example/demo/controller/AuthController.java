package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")   // 类级别：这个Controller下所有接口的公共前缀
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public Response<UserResponse> register(@RequestBody RegisterRequest request){
        User user = authService.register(request.getUsername(), request.getPassword());
        UserResponse userResponse=new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setUsername(user.getUsername());
        return Response.success(userResponse);
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse loginResponse = authService.login(request.getUsername(),request.getPassword());
        return Response.success(loginResponse);
    }

}
