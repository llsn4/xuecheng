package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengException;
import com.xuecheng.base.model.PagePrams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private TeachplanService teachplanService;

    @Autowired
    private CourseBaseMapper courseBaseMapper;
    @Autowired
    private CourseMarketMapper marketMapper;
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired

    private CourseTeacherMapper courseTeacherMapper;
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

    /**
     * 向两张表写数据
     * @param companyId
     * @param dto
     * @return
     */
    @Transactional
    @Override
    public CourseBaseInfoDto save(Long companyId,AddCourseDto dto) {
        //参数合法性校验
        //向课程基础表写数据
        CourseBase courseBase = new CourseBase();
        //将传入的AddcourseDto放到里面
        //只要属性名一直就会拷到里面,原先在里面的数据会被覆盖
        BeanUtils.copyProperties(dto,courseBase);
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        //审核状态默认未提交,发布状态同理
        courseBase.setAuditStatus("202002");
        courseBase.setStatus("203001");
        int insert = courseBaseMapper.insert(courseBase);
        if(insert<=0){
            throw new RuntimeException("添加失败");
        }
        //向课程营销表写数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto,courseMarket);
        //插入成功，可以拿到课程的id
        Long id = courseBase.getId();
        courseMarket.setId(id);
        saveCourseMarket(courseMarket);
        return getCourseBaseInfo(id);


    }



    /**
     * 根据课程id找课程信息和课程的营销信息
     * @param id
     * @return 将两个信息拼接
     */
    @Override
    public CourseBaseInfoDto getCourseBaseInfo(Long id){
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if(courseBase==null){
            return null;
        }
        CourseMarket courseMarket = marketMapper.selectById(id);
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        if(courseMarket!=null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
       //根据 courseCategoryMapper得到分类的名称
        String stName = courseCategoryMapper.selectById(courseBaseInfoDto.getSt()).getName();
        String mtName = courseCategoryMapper.selectById(courseBaseInfoDto.getMt()).getName();
        courseBaseInfoDto.setMtName(mtName);
        courseBaseInfoDto.setStName(stName);

        return courseBaseInfoDto;


    }

    @Override
    public CourseBaseInfoDto update(Long companyId, EditCourseDto dto) {
        //合法性校验
        //本机构只能修改本机构的课程
        Long id = dto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(id);
        CourseMarket courseMarket = marketMapper.selectById(id);
        if(courseBase==null){
            throw new XueChengException("课程不存在");
        }
//        if(!courseBase.getCompanyId().equals(companyId)){
//            throw new XueChengException("本机构只能修改本机构的课程");
//        }
        //封装数据
        BeanUtils.copyProperties(dto,courseBase);
        BeanUtils.copyProperties(dto,courseMarket);
        courseBase.setChangeDate(LocalDateTime.now());
        int i = courseBaseMapper.updateById(courseBase);
        if(i<=0){
            throw new XueChengException("课程修改失败");
        }
        saveCourseMarket(courseMarket);
        log.info("修改成功！");
        return getCourseBaseInfo(id);



    }

    @Override
    @Transactional
    public void delete(Long id) {
       if(!"002003".equals(courseBaseMapper.selectById(id).getAuditStatus())){
           //删除课程相关的基本信息、营销信息、课程计划、课程教师信息。
           courseBaseMapper.deleteById(id);
           marketMapper.deleteById(id);
           LambdaQueryWrapper<Teachplan> queryWrapper=new LambdaQueryWrapper<>();
           queryWrapper.eq(Teachplan::getCourseId,id);
           List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);
           for (Teachplan teachplan:teachplans){
               teachplanService.delete(teachplan.getId());
           }
           LambdaQueryWrapper<CourseTeacher>queryWrapper1=new LambdaQueryWrapper<>();
           queryWrapper1.eq(CourseTeacher::getCourseId,id);
           List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(queryWrapper1);
           for(CourseTeacher teacher:courseTeachers){
               courseTeacherMapper.deleteById(teacher.getId());
           }
       }

    }


    /**
     * 单独写一个方法，如果存在就更新，不存在，就添加
     * @param courseMarket
     * @return
     */
    private int saveCourseMarket(CourseMarket courseMarket){
        //参数合法性校验
        String charge=courseMarket.getCharge();
        if(!StringUtils.isNotEmpty(charge)){
            throw new XueChengException("收费规则不能空");

        }
        if("201001".equals(charge)){
            if(courseMarket.getPrice()==null||courseMarket.getPrice()<=0){
                throw new XueChengException("课程价格不合法");
            }

        }
        //数据库查询
            CourseMarket courseBase = marketMapper.selectById(courseMarket.getId());
        if(courseBase==null){
            return  marketMapper.insert(courseMarket);
        }
        else {
            //先拷贝
            BeanUtils.copyProperties(courseMarket,courseBase);
            courseBase.setId(courseMarket.getId());
            return   marketMapper.updateById(courseBase);
        }



    }

}
