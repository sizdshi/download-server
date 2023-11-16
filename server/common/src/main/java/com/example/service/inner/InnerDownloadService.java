package com.example.service.inner;


import java.util.Map;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
public interface InnerDownloadService  {

    Map<String, String> searchAllTaskStatus();

    String say(String text);

    String hello();
}
