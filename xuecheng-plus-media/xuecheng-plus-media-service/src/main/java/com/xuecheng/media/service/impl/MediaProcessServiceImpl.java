package com.xuecheng.media.service.impl;

import com.xuecheng.media.mapper.MediaProcessMapper;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 苏航
 * @description
 * @date 2023/4/11 11:50
 **/
@Service
@Slf4j
public class MediaProcessServiceImpl implements MediaProcessService {
    @Autowired
    private MediaProcessMapper mediaProcessMapper;
    @Override
    public List<MediaProcess> selectListByShardIndex(int shardTotal, int shardIndex, int count) {
     return    mediaProcessMapper.selectListByShardIndex(shardTotal,shardIndex,count);
    }

    @Override
    public boolean updateTask(long id) {
        return false;
    }
}
