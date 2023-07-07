package com.xuecheng.media.service;

import com.xuecheng.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 苏航
 * @description
 * @date 2023/4/11 11:49
 **/
public interface MediaProcessService {

    List<MediaProcess> selectListByShardIndex(int shardTotal,  int shardIndex,  int count);

    public boolean updateTask(long id);
}
