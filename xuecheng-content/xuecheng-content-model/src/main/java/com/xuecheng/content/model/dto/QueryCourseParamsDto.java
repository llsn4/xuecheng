package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author 苏航
 * @description 课程查询条件模型类
 * @date 2023/4/4 16:06
 **/
@Data
@ToString
public class QueryCourseParamsDto {
    /**
     * 审核状态
     */
    private String auditStatus;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 发布状态
     */
    private String publishStatus;
}
