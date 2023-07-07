package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.dto.TeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 苏航
 * @description
 * @date 2023/4/7 14:24
 **/
@Service
@Slf4j
public class CourseTeacherServiceImpl implements CourseTeacherService {
    @Autowired
    private CourseTeacherMapper mapper;
    @Override
    public List<CourseTeacher> list(Long id) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId,id);
        log.info("查询到教师");
        return mapper.selectList(queryWrapper);
    }

    @Override
    public CourseTeacher save(Long companyId,TeacherDto dto) {


        Long id = dto.getId();
        CourseTeacher courseTeacher = mapper.selectById(id);
        if(courseTeacher!=null){
            //修改
             BeanUtils.copyProperties(dto,courseTeacher);
             mapper.updateById(courseTeacher);
        }
        else {
            courseTeacher=new CourseTeacher();
            BeanUtils.copyProperties(dto,courseTeacher);
            mapper.insert(courseTeacher);
        }


        return courseTeacher;
    }

    @Override
    public void delete(Long companyId,Long courseId, Long id) {
        mapper.deleteById(id);
    }


}
