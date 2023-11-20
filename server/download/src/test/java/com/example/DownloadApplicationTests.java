package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
public class DownloadApplicationTests {
    @Test
    void contextLoads() {
    }

}
