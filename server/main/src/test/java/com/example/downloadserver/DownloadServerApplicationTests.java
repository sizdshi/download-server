package com.example.downloadserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
class DownloadServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
