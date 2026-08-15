package com.example.demo.repository;

import com.example.demo.entity.Todo;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,String> {
    List<Todo> findByUser(User user);
    Page<Todo> findByUser(User user, Pageable pageable);
    Page<Todo> findByUserAndStatus(User user, Integer status, Pageable pageable);
    @Query("SELECT t FROM Todo t WHERE t.user = :user ORDER BY " +
            "t.status ASC, " +
            "CASE WHEN t.status = 0 THEN t.createTime END ASC, " +
            "CASE WHEN t.status = 1 THEN t.completeTime END DESC")
    Page<Todo> findByUserMixedOrder(@Param("user") User user, Pageable pageable);
}
