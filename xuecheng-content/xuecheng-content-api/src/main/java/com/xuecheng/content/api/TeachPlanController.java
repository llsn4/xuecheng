package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 苏航
 * @description 课程计划相关接口
 * @date 2023/4/6 21:53
 **/
@RestController
@Slf4j
@RequestMapping("/teachplan")
public class TeachPlanController {
    @Autowired
    private TeachplanService service;
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachplanDto> search(@PathVariable Long courseId){
     return    service.findTeachplanTree(courseId);


    }
    @PostMapping()
    public void save(@RequestBody SaveTeachplanDto dto){
        service.save(dto);

    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);

    }
    @PostMapping("/movedown/{id}")
    public void moveDown(@PathVariable Long id){
        service.moveUp(id);


    } @PostMapping("/moveup/{id}")
    public void moveUp(@PathVariable Long id){
        service.moveDown(id);


    }


}
