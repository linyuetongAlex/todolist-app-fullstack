package com.example.demo.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class LoginResponse {
    @JsonProperty("user_id")
    private String userId;
    private String username;
    private String token;
}
