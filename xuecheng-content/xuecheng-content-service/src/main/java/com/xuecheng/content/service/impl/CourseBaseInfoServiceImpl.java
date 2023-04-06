package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PagePrams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 苏航
 * @description
 * @date 2023/4/4 20:29
 **/
@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PagePrams pagePrams, QueryCourseParamsDto dto) {
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
        Page<CourseBase> page = new Page<>(pagePrams.getPageNo(), pagePrams.getPageSize());
        Page<CourseBase> selectPage = courseBaseMapper.selectPage(page, queryWrapper);
        List<CourseBase> items = selectPage.getRecords();
        long counts = selectPage.getTotal();
        long pageSize = selectPage.getSize();
        long pageN = selectPage.getCurrent();
        PageResult<CourseBase> result = new PageResult<>(items,counts,pageN,pageSize);
        return result;

    }
}
