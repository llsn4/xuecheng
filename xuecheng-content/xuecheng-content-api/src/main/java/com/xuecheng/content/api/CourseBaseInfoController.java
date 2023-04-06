package com.xuecheng.content.api;

import com.xuecheng.base.model.PagePrams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 苏航
 * @description
 * @date 2023/4/4 16:20
 **/
@RestController
@RequestMapping("/course")
public class CourseBaseInfoController {
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;


    @PostMapping("/list")
    public PageResult<CourseBase> list(PagePrams pagePrams, @RequestBody QueryCourseParamsDto dto){
        return courseBaseInfoService.queryCourseBaseList(pagePrams, dto);

      

    }
}
