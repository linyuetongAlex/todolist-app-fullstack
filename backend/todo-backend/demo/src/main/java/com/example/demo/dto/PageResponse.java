package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> list;
    private long total;
    private int page;
    private int pageSize;

    public static <T> PageResponse<T> of(List<T> list, long total, int page, int pageSize) {
        PageResponse<T> response = new PageResponse<>();
        response.setList(list);
        response.setTotal(total);
        response.setPage(page);
        response.setPageSize(pageSize);
        return response;
    }
}