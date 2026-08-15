package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //	标记这是一个实体类，对应一张表
@Table(name = "user") //	指定对应的表名（如果类名和表名不一致时需要）
@Data //自动生成所有字段的 getter/setter，以及 toString()、equals() 等
@NoArgsConstructor // 生成一个无参构造方法（JPA 要求 Entity 必须有无参构造方法，这是规范）
public class User {
    @Id //标记哪个字段是主键
    @Column(name = "user_id", length = 36) //指定字段对应的列名、是否可为空、长度等，要和 SQL 里的约束保持一致
    private String userId;

    @Column(name="username",length = 20,unique = true,nullable = false)
    private String username;

    @Column(name="password_hash",length = 60,nullable = false)
    private String passwordHash;
}
