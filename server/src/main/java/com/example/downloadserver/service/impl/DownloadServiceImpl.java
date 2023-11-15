package com.example.downloadserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.downloadserver.common.ErrorCode;
import com.example.downloadserver.exception.BusinessException;
import com.example.downloadserver.model.entity.Download;
import com.example.downloadserver.model.enums.DownloadStatus;
import com.example.downloadserver.service.DownloadService;
import com.example.downloadserver.mapper.DownloadMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.regex.Pattern;

/**
* @author sizd-shi
* @description 针对表【download(上传下载表)】的数据库操作Service实现
* @createDate 2023-11-09 14:40:30
*/
@Service
public class DownloadServiceImpl extends ServiceImpl<DownloadMapper, Download>
    implements DownloadService {
    @Resource
    private DownloadMapper downloadMapper;

    @Override
    public long changeThread(String id, long num, HttpServletRequest request) {
        if(!StringUtils.isNotEmpty(id)||!StringUtils.isNotEmpty(String.valueOf(num))){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.eq(Download::getId,id);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件不存在");
        }
        Download download = new Download();
        download.setCount(num);

        boolean saveResult = this.save(download);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 更新任务数失败 数据库异常");
        }
        return download.getId();
    }

    @Override
    public long start(String id,HttpServletRequest request) {
        if(!StringUtils.isNotEmpty(id)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.eq(Download::getId,id);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件不存在");
        }
        Download download = new Download();
        download.setStatus(DownloadStatus.STATUS_DOWNLOADING.getValue());

        boolean saveResult = this.save(download);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 开始任务数失败 数据库异常");
        }
        return download.getId();
    }

    @Override
    public long suspend(String id, HttpServletRequest request) {
        if(!StringUtils.isNotEmpty(id)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.eq(Download::getId,id);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件不存在");
        }
        Download download = new Download();
        download.setStatus(DownloadStatus.STATUS_WAITING.getValue());

        boolean saveResult = this.save(download);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 暂停任务数失败 数据库异常");
        }
        return download.getId();
    }

    @Override
    public long stop(String id, HttpServletRequest request) {
        if(!StringUtils.isNotEmpty(id)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.eq(Download::getId,id);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件不存在");
        }
        Download download = new Download();
        download.setStatus(DownloadStatus.STATUS_PAUSED.getValue());

        boolean saveResult = this.save(download);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 停止任务数失败 数据库异常");
        }
        return download.getId();
    }

    @Override
    public long delete(String id) {
        if(!StringUtils.isNotEmpty(id)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.eq(Download::getId,id);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件不存在");
        }
        Download download = new Download();
        download.setStatus(DownloadStatus.STATUS_PAUSED.getValue());
        download.setIs_delete(1);
        boolean saveResult = this.save(download);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 删除任务失败 数据库异常");
        }
        return download.getId();
    }

    @Override
    public long submit(String url) {
        if(!StringUtils.isNotEmpty(url)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }


//        String urlPattern = "^(http|https):\\/\\/(?:www\\.)?([a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*|\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})(:\\d+)?(\\/\\S*)?$\n";
//        if(!Pattern.matches(urlPattern,url)){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不合法的URL");
//        }

        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.eq(Download::getUrl,url);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件已存在");
        }

        Download download = new Download();

        download.setUrl(url);
        download.setUpdate_time(new Date());
        boolean saveResult = this.save(download);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 提交任务失败 数据库异常");
        }
        return download.getId();
    }
}




