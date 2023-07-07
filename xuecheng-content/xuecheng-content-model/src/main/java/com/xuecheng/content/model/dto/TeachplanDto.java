package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

/**
 * @author 苏航
 * @description
 * @date 2023/4/6 21:51
 **/
@Data
public class TeachplanDto extends Teachplan {
    //与媒资关联的信息
    private TeachplanMedia teachplanMedia;
    //小章节的list
    private List<TeachplanDto> teachPlanTreeNodes;
}
