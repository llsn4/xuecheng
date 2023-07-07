package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.TeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @author 苏航
 * @description
 * @date 2023/4/7 14:24
 **/
public interface CourseTeacherService {
    public List<CourseTeacher> list(Long id);
    public CourseTeacher save(Long companyId,TeacherDto dto);
    public void delete(Long companyId,Long courseId,Long id);

}
