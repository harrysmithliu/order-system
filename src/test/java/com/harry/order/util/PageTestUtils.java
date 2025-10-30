package com.harry.order.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 简单的分页工具类，用于测试
 */
public class PageTestUtils {
    /**
     * 创建一个空列表分页（默认内容为空）
     */
    public static <T> Page<T> emptyPage(int size) {
        return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, size), 0);
    }

    /**
     * 创建一个包含指定数量元素的分页
     * 用于测试 list() 或分页接口返回结构
     */
    public static Page<Integer> pageOf(int size) {
        List<Integer> list = IntStream.rangeClosed(1, size)
                .boxed()
                .collect(Collectors.toList());
        return new PageImpl<>(list, PageRequest.of(0, size), size);
    }

    /**
     * 自定义数据列表分页
     */
    public static <T> Page<T> pageOf(List<T> content, int page, int size) {
        return new PageImpl<>(content, PageRequest.of(page, size), content.size());
    }
}
