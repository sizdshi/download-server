package com.example.downloadserver;

import com.example.downloadserver.controller.TransferController;
import com.example.model.BaseResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class test {
    @Resource
    private TransferController transferController;

    @Test
    void contextLoads(){

    }

    @Test
    void testSubmit(){
        List<String> ids = new ArrayList<>();
        ids.add("1729773974278430721");
        BaseResponse<Object> result = transferController.suspend(ids, null);

    }
}
