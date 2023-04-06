package com.xuecheng.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PagePrams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 苏航
 * @description
 * @date 2023/4/4 19:31
 **/
@SpringBootTest
public class CourseBaseMapperTests {
    @Autowired
    private CourseBaseMapper baseMapper;
    @Test
    void testCourseBaseMapper(){
       // CourseBase courseBase = baseMapper.selectById(1);
        //System.out.println(courseBase);
        QueryCourseParamsDto dto = new QueryCourseParamsDto();
        dto.setCourseName("S");
        //拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper=new LambdaQueryWrapper<>();
        //根据名称模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(dto.getCourseName()),
                CourseBase::getName,dto.getCourseName());
        //根据审核状态精确查询
        queryWrapper.eq(StringUtils.isNotEmpty(dto.
                getAuditStatus()),CourseBase::getAuditStatus,dto.getAuditStatus());
        //根据发布状态精确查询
        queryWrapper.eq(StringUtils.isNotEmpty(dto.
                getPublishStatus()),CourseBase::getStatus,dto.getPublishStatus());
        //创建page分页参数对象
        PagePrams pagePrams=new PagePrams(1L,5L);
        Page<CourseBase> page = new Page<>(pagePrams.getPageNo(), pagePrams.getPageSize());
        Page<CourseBase> selectPage = baseMapper.selectPage(page, queryWrapper);
        List<CourseBase> items = selectPage.getRecords();
//        for (CourseBase courseBase:records){
//            System.out.println(courseBase);
//        }
        //List<T> items, Long counts, Long page, Long pageSize
        long counts = selectPage.getTotal();
        long pageSize = selectPage.getSize();
        long pageN = selectPage.getCurrent();
        PageResult<CourseBase> result = new PageResult<>(items,counts,pageN,pageSize);
        System.out.println(selectPage);


    }
}
