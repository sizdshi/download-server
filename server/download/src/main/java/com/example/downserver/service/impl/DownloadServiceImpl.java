package com.example.downserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.downserver.constant.CommonConstant;
import com.example.download.common.ErrorCode;
import com.example.exception.BusinessException;
import com.example.downserver.mapper.DownloadMapper;
import com.example.downserver.model.dto.DownloadRequest;
import com.example.downserver.model.dto.ThreadRequest;
import com.example.downserver.model.entity.Download;
import com.example.downserver.model.enums.DownloadStatus;
import com.example.downserver.model.vo.DownloadVO;
import com.example.downserver.service.DownloadService;
import com.example.downserver.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
* @author sizd-shi
* @description 针对表【download(上传下载表)】的数据库操作Service实现
* @createDate 2023-11-09 14:40:30
*/
@Service
@Slf4j
public class DownloadServiceImpl extends ServiceImpl<DownloadMapper, Download>
    implements DownloadService {
    @Resource
    private DownloadMapper downloadMapper;

    @PostConstruct
    public void init() {
        // 在这里添加初始化日志输出或其他初始化逻辑
        log.info("DownloadService initialized!");
        System.out.println("DownloadService initialized!");
    }

    @Override
    public long suspend(List<String> ids) {
        if(!CollectionUtils.isNotEmpty(ids)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"传入数组为空");
        }
        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.in(Download::getId,ids);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求数据不存在");
        }

        invokeLambdaUpdateWrapper.set(Download::getStatus,DownloadStatus.STATUS_PAUSED.getValue());
        int updateCount = downloadMapper.update(new Download(),invokeLambdaUpdateWrapper);
        List<Download> up = downloadMapper.selectList(invokeLambdaUpdateWrapper);

        if(updateCount<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 暂停任务数失败 数据库异常");
        }


        return up.get(1).getId();
    }

    @Override
    public long changeThread(ThreadRequest threadRequest, HttpServletRequest request) {
        if (!StringUtils.isNotEmpty(threadRequest.getId()) || !StringUtils.isNotEmpty(String.valueOf(threadRequest.getCount()))) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.eq(Download::getId,Long.parseLong(threadRequest.getId()));
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件不存在");
        }

        invokeLambdaUpdateWrapper.set(Download::getCount,threadRequest.getCount());
        int changeThread = downloadMapper.update(new Download(),invokeLambdaUpdateWrapper);

        if(changeThread<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"修改任务数失败 数据库异常");
        }
        return changeThread;
    }

    @Override
    public long start(List<String> ids,HttpServletRequest request) {
        if(!CollectionUtils.isNotEmpty(ids)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"传入数组为空");
        }
        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.in(Download::getId,ids);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件不存在");
        }
        invokeLambdaUpdateWrapper.set(Download::getStatus,DownloadStatus.STATUS_DOWNLOADING.getValue());

        int resumeCount = downloadMapper.update(new Download(),invokeLambdaUpdateWrapper);
        if(resumeCount<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 开始下载任务数失败 数据库异常");
        }



        return resumeCount;
    }



    @Override
    public long suspend(List<String> ids, HttpServletRequest request) {
        if(!CollectionUtils.isNotEmpty(ids)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"传入数组为空");
        }
        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.in(Download::getId,ids);
        invokeLambdaUpdateWrapper.eq(Download::getIs_delete,0);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求数据不存在");
        }

        invokeLambdaUpdateWrapper.set(Download::getStatus,DownloadStatus.STATUS_PAUSED.getValue());
        int updateCount = downloadMapper.update(new Download(),invokeLambdaUpdateWrapper);
//        List<Download> up = downloadMapper.selectList(invokeLambdaUpdateWrapper);

        if(updateCount<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 暂停任务数失败 数据库异常");
        }

        return updateCount;
    }

    @Override
    public long restart(List<String> ids, HttpServletRequest request) {
        if(!CollectionUtils.isNotEmpty(ids)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"传入数组为空");
        }

        LambdaUpdateWrapper<Download> resumeWrapper = new LambdaUpdateWrapper<>();
        resumeWrapper.in(Download::getId,ids);
        resumeWrapper.eq(Download::getIs_delete,0);
        long count = downloadMapper.selectCount(resumeWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件不存在");
        }
        //todo 检查本地文件是否存在，存在则删除

        resumeWrapper.set(Download::getStatus,DownloadStatus.STATUS_DOWNLOADING.getValue());

        int resumeCount = downloadMapper.update(new Download(),resumeWrapper);
        if(resumeCount<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 重新下载任务数失败 数据库异常");
        }
        return resumeCount;

    }

    @Override

    public long delete(List<String> ids) {
        if(!CollectionUtils.isNotEmpty(ids)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"传入数组为空");
        }
        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.in(Download::getId,ids);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求数据不存在");
        }

        invokeLambdaUpdateWrapper.set(Download::getStatus,DownloadStatus.STATUS_DELETE.getValue());
        invokeLambdaUpdateWrapper.set(Download::getIs_delete,1);
        int deleteCount = downloadMapper.update(new Download(),invokeLambdaUpdateWrapper);
        System.out.println("删除成功");
        if(deleteCount<=0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 删除任务数失败 数据库异常");
        }
        return deleteCount;
    }

    @Override
    public String submit(String url) {
        if(!StringUtils.isNotEmpty(url)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        String urlPattern = "^(http|https):\\/\\/(www\\.)?([a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*|\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})(:\\d+)?(\\/\\S*)?$";
        if(!Pattern.matches(urlPattern,url)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不合法的URL");
        }

        //todo 检查是否逻辑删除，如果是，改为否
        LambdaUpdateWrapper<Download> invokeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        invokeLambdaUpdateWrapper.eq(Download::getUrl,url);
        long count = downloadMapper.selectCount(invokeLambdaUpdateWrapper);

        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求文件已存在");
        }
        String fileName = url.substring(url.lastIndexOf('/') + 1);


        Download download = new Download();
        download.setUrl(url);
        download.setTask_type("http");
        download.setFile_name(fileName);
        download.setUpdate_time(new Date());
        download.setCreate_time(new Date());
        download.setIs_delete(0);
        //todo 文件大小在哪里处理

        boolean saveResult = this.save(download);


        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"download 提交任务失败 数据库异常");
        }
        return Long.toString(download.getId());
    }
    @Override
    public Page<DownloadVO> listDownloadVOByPage(DownloadRequest downloadRequest, HttpServletRequest request) {
        long current = downloadRequest.getCurrent();
        long size = downloadRequest.getPageSize();

        Page<Download> downloadPage = this.page(new Page<>(current,size),this.getQueryWrapper(downloadRequest));

        return this.getDownloadVOPage(downloadPage,request);
    }


    @Override
    public Page<DownloadVO> getDownloadVOPage(Page<Download> downloadPage, HttpServletRequest request) {
        List<Download> downloadList = downloadPage.getRecords();
        Page<DownloadVO> downloadVOPage = new Page<>(downloadPage.getCurrent(),downloadPage.getSize(),downloadPage.getTotal());
        if (CollectionUtils.isEmpty(downloadList)) {
            return downloadVOPage;
        }

        List<DownloadVO> downloadVOList = downloadList.stream().map(DownloadVO::objToVo).collect(Collectors.toList());

        downloadVOPage.setRecords(downloadVOList);
        return downloadVOPage;
    }



    @Override
    public QueryWrapper<Download> getQueryWrapper(DownloadRequest downloadRequest){

        QueryWrapper<Download> downloadQueryWrapper = new QueryWrapper<>();
        if(downloadRequest == null){
            return downloadQueryWrapper;
        }

        String url = downloadRequest.getUrl();
        String status = downloadRequest.getStatus();
        String fileName = downloadRequest.getFile_name();
        Long id = downloadRequest.getId();
        String sortField = downloadRequest.getSortField();
        String sortOrder = downloadRequest.getSortOrder();


        downloadQueryWrapper.eq(ObjectUtils.isNotEmpty(url),"url",url);
        downloadQueryWrapper.eq(ObjectUtils.isNotEmpty(status),"status",status);
        downloadQueryWrapper.ne(ObjectUtils.isNotEmpty(id),"id",id);
        downloadQueryWrapper.eq(ObjectUtils.isNotEmpty(fileName),"file_name",fileName);
        if(status.equals(DownloadStatus.STATUS_DELETE.getValue())){
            downloadQueryWrapper.eq("is_delete",1);
        }else{
            downloadQueryWrapper.eq("is_delete",0);
        }
//        downloadQueryWrapper.eq("is_delete",false);
        downloadQueryWrapper.orderBy(SqlUtils.validSortField(sortField),sortOrder.equals(CommonConstant.SORT_ORDER_ASC)
                        ,sortField);

        return downloadQueryWrapper;
    }
}




