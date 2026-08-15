package com.example.demo.repository;

import com.example.demo.entity.Todo;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,String> {
    List<Todo> findByUser(User user);
    Page<Todo> findByUser(User user, Pageable pageable);
}
