package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.Todo;
import com.example.demo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public Response<TodoResponse> createTodo(@RequestBody TodoRequest request, Authentication authentication) {
        // 1. 从authentication里取出userId
        String userId = (String) authentication.getPrincipal();
        // 2. 调用todoService.createTodo(...)，注意参数顺序和数量要对上Service方法定义
        Todo todo=todoService.createTodo(userId,request.getTitle(),request.getDescription(),request.getPriority(),request.getDeadline());
        // 3. 把返回的Todo，转换成TodoResponse
        TodoResponse todoResponse=TodoResponse.from(todo);
        // 4. 用Response.success(...)包装返回
        return Response.success(todoResponse);
    }

//    @GetMapping
//    public Response<PageResponse<TodoResponse>> getTodoList(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int pageSize,
//            Authentication authentication) {
//        // 1. 取出userId
//        String userId = (String) authentication.getPrincipal();
//        // 2. 调用todoService.getTodoList(...)，拿到 Page<Todo>
//        Page<Todo> todoPage=todoService.getTodoList(userId,page,pageSize);
//        // 3. 把 Page<Todo> 里的每一个Todo，转换成TodoResponse
//        // 4. 用 PageResponse.of(...) 组装，再用 Response.success(...) 包装返回
//        List<TodoResponse> responseList=todoPage.getContent()
//                .stream()//把List转换成一个"数据流"，可以对流式地做一系列操作
//                .map(TodoResponse::from)//对流里的每一个元素，都调用 TodoResponse.from(...)
//                .collect(Collectors.toList());//处理完的流，重新收集成一个 List
//
//        PageResponse<TodoResponse> pageResponse = PageResponse.of(responseList, todoPage.getTotalElements(), page, pageSize);
//        return Response.success(pageResponse);
//    }

    @GetMapping
    public Response<PageResponse<TodoResponse>> getTodoList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status,
            Authentication authentication) {
        // 1. 取出userId
        String userId = (String) authentication.getPrincipal();

        Page<Todo> todoPage;
        if (status!=null){
            todoPage=todoService.getTodoListByStatus(userId,page,pageSize,status);
        } else{
            // 2. 调用todoService.getTodoList(...)，拿到 Page<Todo>
            todoPage=todoService.getTodoList(userId,page,pageSize);
        }

        // 3. 把 Page<Todo> 里的每一个Todo，转换成TodoResponse
        // 4. 用 PageResponse.of(...) 组装，再用 Response.success(...) 包装返回
        List<TodoResponse> responseList=todoPage.getContent()
                .stream()//把List转换成一个"数据流"，可以对流式地做一系列操作
                .map(TodoResponse::from)//对流里的每一个元素，都调用 TodoResponse.from(...)
                .collect(Collectors.toList());//处理完的流，重新收集成一个 List

        PageResponse<TodoResponse> pageResponse = PageResponse.of(responseList, todoPage.getTotalElements(), page, pageSize);
        return Response.success(pageResponse);
    }

    @PatchMapping("/{todoId}")
    public Response<TodoResponse> updateTodo(
            @PathVariable String todoId,
            @RequestBody TodoRequest request,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        Todo todo = todoService.updateTodo(userId, todoId, request);
        TodoResponse todoResponse = TodoResponse.from(todo);
        return Response.success(todoResponse);
    }

    @DeleteMapping("/{todoId}")
    public Response<DeleteResponse> deleteTodo(
            @PathVariable String todoId,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        todoService.deleteTodo(userId, todoId);
        DeleteResponse deleteResponse=new DeleteResponse();
        deleteResponse.setTodoId(todoId);
        return Response.success(deleteResponse);
    }
}