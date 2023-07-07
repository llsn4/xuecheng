package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import lombok.experimental.Wither;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author 苏航
 */

public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
    //查询分类
   //@Select(" with recursive t1 as(select * from course_category p where id=#{id} union all select t.* from course_category t inner join t1 on t1.id=t.parentid select * from t1 order by t1.id,t1.orderby")
    public List<CourseCategoryTreeDto> selectTreeNodes(String id);

}
