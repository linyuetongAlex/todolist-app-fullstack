package com.example.demo.dto;

import lombok.Data;

@Data
public class Response<T> {
    private int code;
    private String msg;
    private T data;

    // 静态工厂方法：成功时调用
    public static <T> Response<T> success(T data) {
        Response<T> res = new Response<>();
        res.setCode(0);
        res.setMsg("success");
        res.setData(data);
        return res;
    }

    // 静态工厂方法：失败时调用
    public static <T> Response<T> error(int code, String msg) {
        Response<T> res = new Response<>();
        res.setCode(code);
        res.setMsg(msg);
        res.setData(null);
        return res;
    }
}