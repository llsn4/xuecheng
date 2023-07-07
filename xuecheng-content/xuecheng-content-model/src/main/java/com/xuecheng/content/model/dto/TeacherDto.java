package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author 苏航
 * @description
 * @date 2023/4/7 14:29
 **/
@Data
@ToString
public class TeacherDto {
    private Long courseId;
    private String teacherName;
    private String position;
    private String introduction;
    private Long id;
}
