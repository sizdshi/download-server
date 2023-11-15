package com.example.downloadserver.controller;

import com.example.downloadserver.model.BaseResponse;
import com.example.downloadserver.service.DownloadService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

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
        when(downloadService.changeThread(anyString(), anyLong(), any())).thenReturn(1705234447153963010L);

        BaseResponse<Object> result = transferController.changeThread("1705234447153963010", 6, null);
        Assertions.assertEquals(new BaseResponse<Object>(0, 1705234447153963010L, "ok"), result);
    }

    @Test
    void testStart() {
        when(downloadService.start(anyString(),any())).thenReturn(1724719502443470849L);

        BaseResponse<Object> result = transferController.start("1705234447153963010",null);
//        System.out.println(result);
        //        System.out.println(result);
        Assertions.assertEquals(new BaseResponse<Object>(0,1724719502443470849L, "ok"), result);
    }

    @Test
    void testStop() {
        when(downloadService.stop(anyString(), any())).thenReturn(1724719502443470849L);

        BaseResponse<Object> result = transferController.stop("1705234447153963010", null);
        Assertions.assertEquals(new BaseResponse<Object>(0, 1724719502443470849L, "ok"), result);
    }

    @Test
    void testDelete() {
        when(downloadService.delete(anyString())).thenReturn(1724719502443470849L);

        BaseResponse<Object> result = transferController.delete("1705234447153963010");
        Assertions.assertEquals(new BaseResponse<Object>(0, 1724719502443470849L, "ok"), result);
    }

    @Test
    void testSuspend() {
        when(downloadService.suspend(anyString(), any())).thenReturn(1724719502443470849L);

        BaseResponse<Object> result = transferController.suspend("1705234447153963010", null);
        Assertions.assertEquals(new BaseResponse<Object>(0, 1724719502443470849L, "ok"), result);
    }

    @Test
    void testSubmit() {
        when(downloadService.submit(anyString())).thenReturn(1722564247500468225L);

        BaseResponse<Object> result = transferController.submit("https://dldir.qq.com");
        Assertions.assertEquals(new BaseResponse<Object>(0, 1722564247500468225L, "ok"), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme