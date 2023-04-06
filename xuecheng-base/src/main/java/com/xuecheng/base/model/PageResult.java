package com.xuecheng.base.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author 苏航
 * @description 分页查询结果模型类
 * @date 2023/4/4 16:14
 **/
@Data
@ToString
public class PageResult<T> implements Serializable {
    /**
     *   数据列表
     */

    private List<T> items;
    /**
     *   总记录数
     */
    private Long counts;
    private Long page;
    private Long pageSize;

    public PageResult(List<T> items, Long counts, Long page, Long pageSize) {
        this.items = items;
        this.counts = counts;
        this.page = page;
        this.pageSize = pageSize;
    }


}
