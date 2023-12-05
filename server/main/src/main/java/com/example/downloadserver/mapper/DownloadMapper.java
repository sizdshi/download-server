package com.example.downloadserver.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.downloadserver.model.entity.Download;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* @author sizd-shi
* @description 针对表【download(上传下载表)】的数据库操作Mapper
* @createDate 2023-11-09 14:40:30
* @Entity com.example.downloadserver.model.entity.Download
*/
public interface DownloadMapper extends BaseMapper<Download> {


    int queryByIds(@Param("ew") LambdaUpdateWrapper<Download> wrapper);
}




