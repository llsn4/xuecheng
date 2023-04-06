package com.xuecheng.base.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author 苏航
 * @description 分页查询调用的查询参数
 * @date 2023/4/4 16:00
 **/
@Data
@ToString
public class PagePrams {
    /**
     * 页码
     */
    private Long pageNo=1L;

    public PagePrams(Long pageNo, Long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     * 每页的记录数
     */
    private Long pageSize=10L;
    public PagePrams(){}

}
