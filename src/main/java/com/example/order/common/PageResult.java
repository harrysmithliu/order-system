package com.example.order.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// common/PageResult.java - 分页结果
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private List<T> records;    // 数据列表
    private Long total;         // 总记录数
    private Long current;       // 当前页
    private Long size;          // 每页大小
    private Long pages;         // 总页数

    public PageResult(List<T> records, Long total, Long current, Long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size;
    }
}