package com.harry.order.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageResult<T> implements Serializable {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;

//    @Override
//    public String toString() {
//        return "PageResult(pageNumber=" + pageNumber +
//                ", pageSize=" + pageSize +
//                ", totalElements=" + totalElements +
//                ", contentSize=" + (content == null ? 0 : content.size()) + ")";
//    }
}
