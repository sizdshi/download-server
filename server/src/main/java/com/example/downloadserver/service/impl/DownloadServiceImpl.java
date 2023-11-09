package com.example.downloadserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.downloadserver.model.entity.Download;
import com.example.downloadserver.service.DownloadService;
import com.example.downloadserver.mapper.DownloadMapper;
import org.springframework.stereotype.Service;

/**
* @author sizd-shi
* @description 针对表【download(上传下载表)】的数据库操作Service实现
* @createDate 2023-11-09 14:40:30
*/
@Service
public class DownloadServiceImpl extends ServiceImpl<DownloadMapper, Download>
    implements DownloadService {

}




