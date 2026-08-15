package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TodoRequest {
    private String title;
    private String description;
    private Integer priority;
    private Integer status;
    private LocalDateTime deadline;

}