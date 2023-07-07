package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @author 苏航
 * @description 课程计划管理接口
 * @date 2023/4/7 11:12
 **/
public interface TeachplanService {
    public List<TeachplanDto> findTeachplanTree(Long  courseId);

    public void save(SaveTeachplanDto dto);
    public void delete(Long id);
    public void moveDown(Long id);
    public void moveUp(Long id);

}
