package com.xuecheng.content.api;

import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PagePrams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     *
     * @param companyId
     * @param dto
     * @return
     */
    @PostMapping()
    public CourseBaseInfoDto save(Long companyId, @Validated(ValidationGroups.Insert.class) @RequestBody AddCourseDto dto){
        companyId=1L;

        //获取用户所属机构的id
        return courseBaseInfoService.save(companyId,dto);

    }
    @GetMapping("/{id}")
    public CourseBaseInfoDto findById(@PathVariable("id") Long id){
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(id);
        return courseBaseInfo;

    }
    @PutMapping()
    public CourseBaseInfoDto update( @RequestBody @Validated(ValidationGroups.Update.class) EditCourseDto dto){
       return courseBaseInfoService.update(1L,dto);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        courseBaseInfoService.delete(id);

    }
}
