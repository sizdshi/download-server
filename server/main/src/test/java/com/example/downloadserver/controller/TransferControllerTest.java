package com.example.downloadserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.downloadserver.model.dto.DownloadRequest;
import com.example.downloadserver.model.dto.SubmitRequest;
import com.example.downloadserver.model.vo.DownloadVO;
import com.example.downloadserver.service.DownloadService;
import com.example.model.BaseResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

class TransferControllerTest {
    @Mock
    DownloadService downloadService;
    @Mock
    Logger log;
    @InjectMocks
    TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangeThread() {
//        when(downloadService.changeThread(anyString(), anyLong(), any())).thenReturn(0L);
//
//        BaseResponse<Object> result = transferController.changeThread("id", 0L, null);
//        Assertions.assertEquals(new BaseResponse<Object>(0, "data", "message"), result);
//
    }

    @Test
    void testStart() {
//        when(downloadService.start(anyString(), any())).thenReturn(0L);
//
//        BaseResponse<Object> result = transferController.start("id", null);
//        Assertions.assertEquals(new BaseResponse<Object>(0, "data", "message"), result);
    }

    @Test
    void testStop() {
       // when(downloadService.stop(anyString(), any())).thenReturn(0L);

//        BaseResponse<Object> result = transferController.restart("id", null);
//        Assertions.assertEquals(new BaseResponse<Object>(0, "data", "message"), result);
    }

    @Test
    void testDelete() {
       // when(downloadService.delete(anyString())).thenReturn(0L);

//        BaseResponse<Object> result = transferController.delete("id");
//        Assertions.assertEquals(new BaseResponse<Object>(0, "data", "message"), result);
    }

    @Test
    void testSuspend() {
//        when(downloadService.suspend(any(), any())).thenReturn(0L);
//        List<String> ids = new ArrayList<>();
//        ids.add("1729773974278430721");
//        BaseResponse<Object> result = transferController.suspend(ids, null);
//        Assertions.assertEquals(new BaseResponse<Object>(0, "data", "message"), result);
    }

    @Test
    void testSubmit() {
//        when(downloadService.submit(anyString())).thenReturn(0L);
//
//        BaseResponse<Object> result = transferController.submit(new SubmitRequest());
//        Assertions.assertEquals(new BaseResponse<Object>(0, "data", "message"), result);
    }

    @Test
    void testListPostVOByPage() {
        when(downloadService.getDownloadVOPage(any(), any())).thenReturn(null);
        when(downloadService.getQueryWrapper(any())).thenReturn(null);

        BaseResponse<Page<DownloadVO>> result = transferController.listPostVOByPage(new DownloadRequest(), null);
        Assertions.assertEquals(new BaseResponse<Page<DownloadVO>>(0, null, "message"), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme