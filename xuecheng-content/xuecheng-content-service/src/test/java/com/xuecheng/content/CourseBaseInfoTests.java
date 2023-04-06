package com.xuecheng.content;

import com.xuecheng.base.model.PagePrams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 苏航
 * @description
 * @date 2023/4/4 20:32
 **/
@SpringBootTest
public class CourseBaseInfoTests {
     @Autowired
     private CourseBaseInfoService service;
    @Test
    void testCourseBaseInfo(){
        QueryCourseParamsDto dto = new QueryCourseParamsDto();
        dto.setCourseName("S");
        PagePrams pagePrams=new PagePrams(1L,5L);
        PageResult<CourseBase> result = service.queryCourseBaseList(pagePrams, dto);
    }
}
