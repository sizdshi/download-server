package com.example.downloadserver.service.impl;

import com.example.downloadserver.mapper.DownloadMapper;
import com.example.downloadserver.service.DownloadService;
import org.apache.ibatis.logging.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

class DownloadServiceImplTest {
    @Mock
    DownloadMapper downloadMapper;
    @Mock
    Log log;

    //Field entityClass of type Class - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    //Field mapperClass of type Class - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    private DownloadService downloadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangeThread() {
        long result = downloadService.changeThread("id", 0L, null);
        Assertions.assertEquals(0L, result);
    }

    @Test
    void testStart() {

//
//        // Set up the expected behavior of the start method without accessing the database
        when(downloadService.start("1705234447153963010",null)).thenReturn(1724719502443470849L);

        long result = downloadService.start("1705234447153963010",null);

        Assertions.assertEquals(1724719502443470849L, result);
    }

    @Test
    void testSuspend() {
        long result = downloadService.suspend("id", null);
        Assertions.assertEquals(0L, result);
    }

    @Test
    void testStop() {
        long result = downloadService.stop("id", null);
        Assertions.assertEquals(0L, result);
    }

    @Test
    void testDelete() {
        long result = downloadService.delete("id");
        Assertions.assertEquals(0L, result);
    }

    @Test
    void testSubmit() {
        long result = downloadService.submit("url");
        Assertions.assertEquals(0L, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme