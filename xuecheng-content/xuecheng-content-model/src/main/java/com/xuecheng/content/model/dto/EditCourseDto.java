package com.xuecheng.content.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author 苏航
 * @description
 * @date 2023/4/6 21:18
 **/
@Data
public class EditCourseDto extends AddCourseDto{
    @NotEmpty(message = "id不能为空")
    private Long id;
}
