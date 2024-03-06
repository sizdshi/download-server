package com.example.downserver.mapper;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.downserver.model.entity.Download;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
* @author sizd-shi
* @description 针对表【download(上传下载表)】的数据库操作Mapper
* @createDate 2023-11-09 14:40:30
* @Entity com.example.entity.model.Download
*/
@Component
public interface DownloadMapper extends BaseMapper<Download> {

    int queryByIds(@Param("ew") LambdaUpdateWrapper<Download> wrapper);
}




