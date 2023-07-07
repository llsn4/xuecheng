package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 苏航
 * @description
 * @date 2023/4/7 11:13
 **/
@Service
@Slf4j
public class TeachplanServiceImpl implements TeachplanService{
    @Autowired
    private TeachplanMapper mapper;
    @Autowired
    private TeachplanMediaMapper mediaMapper;
    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
      return   mapper.selectTreeNodes(courseId);
    }

    @Override
    public void save(SaveTeachplanDto dto) {
        Long teachplanId = dto.getId();
        if(teachplanId==null){
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(dto,teachplan);
            //确定排序字段--找到同级结点的个数，排序字段就是个数加一
            Long id = teachplan.getCourseId();
            Long parentid = teachplan.getParentid();
            LambdaQueryWrapper<Teachplan> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getCourseId,id).eq(Teachplan::getParentid,parentid);
            int count = mapper.selectCount(queryWrapper);
            teachplan.setOrderby(count+1);
            mapper.insert(teachplan);
            //新增
        }
        else {
            //修改
            Teachplan teachplan = mapper.selectById(teachplanId);
            BeanUtils.copyProperties(dto,teachplan);
            mapper.updateById(teachplan);
        }



    }

    @Override
    public void delete(Long id) {
        Teachplan teachplan = mapper.selectById(id);
        if(teachplan==null){
            throw new XueChengException("不存在该章节");
        }
        if(teachplan.getGrade()==1){
            //大章节
            LambdaQueryWrapper<Teachplan> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid,teachplan.getId());
            int count = mapper.selectCount(queryWrapper);
            if(count==0){
                mapper.deleteById(id);
                log.info("成功删除大章节");
            }
            else {
                throw new RuntimeException("课程计划信息还有子级信息，无法操作");
            }
        }
        if(teachplan.getGrade()==2){
            mapper.deleteById(id);
            LambdaQueryWrapper<TeachplanMedia>queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(TeachplanMedia::getTeachplanId,id);
            mediaMapper.delete(queryWrapper);
            log.info("成功删除子章节");
        }


    }

    @Override
    public void moveDown(Long id) {
        log.info("进行上移");
        Teachplan teachplanPre = mapper.selectById(id);
        int orderby = teachplanPre.getOrderby();
        LambdaQueryWrapper<Teachplan> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,teachplanPre.getCourseId())
                .eq(Teachplan::getParentid,teachplanPre.getParentid()).orderByDesc(Teachplan::getOrderby);
        changePosition(teachplanPre, orderby, queryWrapper);


    }

    @Override
    public void moveUp(Long id) {
        log.info("进行下移");
        Teachplan teachplanPre = mapper.selectById(id);
        int orderby = teachplanPre.getOrderby();
        LambdaQueryWrapper<Teachplan> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,teachplanPre.getCourseId())
                .eq(Teachplan::getParentid,teachplanPre.getParentid()).orderByAsc(Teachplan::getOrderby);
        changePosition(teachplanPre, orderby, queryWrapper);
    }

    private void changePosition(Teachplan teachplanPre, int orderby, LambdaQueryWrapper<Teachplan> queryWrapper) {
        List<Teachplan> teachplans = mapper.selectList(queryWrapper);
        Long xuId= 0L;
        boolean flag=false;
        for(Teachplan teachplan:teachplans){
            if(flag){
                xuId=teachplan.getId();
                flag=false;
            }
            if(teachplan.equals(teachplanPre)){
                //现在的下一个是需要替换的
                flag=true;
            }
        }
        Teachplan teachplan = mapper.selectById(xuId);
        if(teachplan!=null){
            Integer orderby1 = teachplan.getOrderby();
            teachplanPre.setOrderby(orderby1);
            teachplan.setOrderby(orderby); mapper.updateById(teachplan);
            mapper.updateById(teachplanPre);

        }
    }
}
