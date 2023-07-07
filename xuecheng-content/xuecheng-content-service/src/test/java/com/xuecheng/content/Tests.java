package com.xuecheng.content;

import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 苏航
 * @description
 * @date 2023/4/6 22:18
 **/
@SpringBootTest
public class Tests {
    @Autowired
    private TeachplanMapper mapper;
    @Test
    void test(){
        List<TeachplanDto> selected = mapper.selectTreeNodes(117L);


    }
}
