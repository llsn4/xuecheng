package com.xuecheng.content.service;

import com.xuecheng.base.model.PagePrams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @author 苏航
 * @description 课程信息管理接口
 * @date 2023/4/4 20:26
 **/
public interface CourseBaseInfoService {

    public PageResult<CourseBase> queryCourseBaseList(PagePrams pagePrams, QueryCourseParamsDto dto);


}
