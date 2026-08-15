package com.example.demo.dto;
import com.example.demo.entity.Todo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TodoResponse {

    @JsonProperty("todo_id")
    private String todoId;

    private String title;

    private String description;

    private Integer priority;

    private Integer status;

    private LocalDateTime deadline;

    @JsonProperty("create_time")
    private LocalDateTime createTime;
    @JsonProperty("update_time")
    private LocalDateTime updateTime;
    @JsonProperty("complete_time")
    private LocalDateTime completeTime;

    // 新增：把一个Todo实体，转换成TodoResponse
    public static TodoResponse from(Todo todo) {
        TodoResponse response = new TodoResponse();
        response.setTodoId(todo.getTodoId());
        response.setTitle(todo.getTitle());
        response.setDescription(todo.getDescription());
        response.setPriority(todo.getPriority());
        response.setStatus(todo.getStatus());
        response.setDeadline(todo.getDeadline());
        response.setCreateTime(todo.getCreateTime());
        response.setUpdateTime(todo.getUpdateTime());
        response.setCompleteTime(todo.getCompleteTime());
        return response;
    }
}