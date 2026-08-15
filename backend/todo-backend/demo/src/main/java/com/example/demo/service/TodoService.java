package com.example.demo.service;

import com.example.demo.dto.TodoRequest;
import com.example.demo.dto.TodoResponse;
import com.example.demo.entity.Todo;
import com.example.demo.entity.User;
import com.example.demo.exception.BusinessException;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public Todo createTodo(String userId, String title, String description, Integer priority, LocalDateTime deadline) {
//        code=40001: title 为空
        if (title.isEmpty()){
            throw new BusinessException(40001, "title不能为空");
        }
//        code=40002: title 超过20字符
        if (title.length()>20){
            throw new BusinessException(40002, "ttitle 超过20字符");
        }

        Todo todo=new Todo();
//        code=40101: 未登录或 token 失效
        User user=userRepository.findById(userId).orElseThrow(
                ()->new BusinessException(40101, "用户不存在或token失效")
        );
        //        code=0: 创建成功
        todo.setUser(user);
        todo.setTodoId(UUID.randomUUID().toString());
        todo.setCreateTime(LocalDateTime.now());
        todo.setUpdateTime(LocalDateTime.now());
        todo.setTitle(title);
        todo.setPriority(priority);
        todo.setDeadline(deadline);
        todo.setDescription(description);
        todo.setStatus(0);
        return todoRepository.save(todo);
    }

    public Page<Todo> getTodoList(String userId, int page, int pageSize) {
        // 1. 用userId查出User对象
        User user=userRepository.findById(userId).orElseThrow(
                ()->new BusinessException(40101, "用户不存在或token失效")
        );
        // 2. 构造 Pageable： PageRequest.of(page - 1, pageSize)   注意页码要减1
        Pageable pageable= PageRequest.of(page - 1, pageSize, Sort.by("createTime").ascending());
        // 3. 调用 todoRepository.findByUser(user, pageable)
        // 4. 返回结果
        return  todoRepository.findByUserMixedOrder(user,pageable);
    }

    public Page<Todo> getTodoListByStatus(String userId, int page, int pageSize,Integer status) {
        // 1. 用userId查出User对象
        User user=userRepository.findById(userId).orElseThrow(
                ()->new BusinessException(40101, "用户不存在或token失效")
        );
        // 查未完成任务（或者不分状态查全部）→ 按 createTime 升序, 查已完成任务 → 按 completeTime 降序
        Sort sort = status != null && status == 1
                ? Sort.by("completeTime").descending()
                : Sort.by("createTime").ascending();
        // 2. 构造 Pageable： PageRequest.of(page - 1, pageSize)   注意页码要减1
        Pageable pageable= PageRequest.of(page - 1, pageSize,sort);
        // 3. 调用 todoRepository.findByUserAndStatus(user,status,pageable)
        // 4. 返回结果
        return  todoRepository.findByUserAndStatus(user,status,pageable);
    }


    public Todo updateTodo(String userId, String todoId, TodoRequest request) {
        // 1. 查询Todo，找不到 → 40401
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessException(40401, "todo_id 不存在或无权限访问"));

        // 2. 权限校验：这个todo是不是属于当前登录用户？
        if (!userId.equals(todo.getUser().getUserId())){
            throw new BusinessException(40401, "todo_id 不存在或无权限访问");
        }

        // 3. 判断body是否完全为空（所有字段都是null）→ 40003
//        str == null：判断这个变量压根没有指向任何对象（没有被赋值，或者说"什么都没有"）
//        str.isEmpty()：判断这个字符串对象存在，但内容是空字符串 ""（长度为0）
        if (request.getTitle() == null && request.getDescription() == null
                && request.getPriority() == null && request.getDeadline() == null
                && request.getStatus() == null) {
            throw new BusinessException(40003, "body 为空，没有任何字段被更新");
        }

        // 4. 逐个字段：不为null才更新
        if (request.getTitle() != null) {
            if (request.getTitle().length()>20){
                throw new BusinessException(40002, "title 超过20字符");
            }
            todo.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            todo.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            todo.setPriority(request.getPriority());
        }
        if (request.getDeadline() != null) {
            todo.setDeadline(request.getDeadline());
        }
        // status变了
        if (request.getStatus() != null && !request.getStatus().equals(todo.getStatus())) {
            //   status变成1 → 设置completeTime为当前时间
            if (request.getStatus().equals(1)){
                todo.setCompleteTime(LocalDateTime.now());
            }
            //   status变成0 → completeTime设为null
            if(request.getStatus().equals(0)){
                todo.setCompleteTime(null);
            }
            todo.setStatus(request.getStatus());
        }

        // 5. 更新updateTime
        todo.setUpdateTime(LocalDateTime.now());

        // 6. save并返回
        return todoRepository.save(todo);
    }

    public void deleteTodo(String userId,String todoId){
        // 1. 查询Todo，找不到 → 40401
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessException(40401, "todo_id 不存在或无权限访问"));
        // 2. 权限校验：这个todo是不是属于当前登录用户？
        if (!userId.equals(todo.getUser().getUserId())){
            throw new BusinessException(40401, "todo_id 不存在或无权限访问");
        }
        todoRepository.delete(todo);
    }
}
