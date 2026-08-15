package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "todo")
@Data
@NoArgsConstructor
public class Todo {

    @Id
    @Column(name = "todo_id", length = 36)
    private String todoId;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column(name="title",length = 20,nullable = false)
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="priority")
    private Integer priority;

    @Column(name="status")
    private Integer status;

    @Column(name="create_time")
    private LocalDateTime createTime;

    @Column(name="update_time")
    private LocalDateTime updateTime;

    @Column(name="complete_time")
    private LocalDateTime completeTime;

    @Column(name="deadline")
    private LocalDateTime deadline;

}