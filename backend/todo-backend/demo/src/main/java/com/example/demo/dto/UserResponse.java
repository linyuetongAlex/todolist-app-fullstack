package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponse {
    @JsonProperty("user_id")
    private String userId;
    private String username;
}