package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author 苏航
 * @description 新增大章节、小章节、修改章节
 * @date 2023/4/7 12:42
 **/
@Data
@ToString
public class SaveTeachplanDto {
    private Long id;
    private String pname;

    /**
     * 课程计划父级Id
     */
    private Long parentid;

    /**
     * 层级，分为1、2、3级
     */
    private Integer grade;

    /**
     * 课程类型:1视频、2文档
     */
    private String mediaType;


    /**
     * 课程标识
     */
    private Long courseId;

    /**
     * 课程发布标识
     */
    private Long coursePubId;


    /**
     * 是否支持试学或预览（试看）
     */
    private String isPreview;

}
