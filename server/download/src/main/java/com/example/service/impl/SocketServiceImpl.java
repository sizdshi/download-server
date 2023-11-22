package com.example.service.impl;

import com.example.service.SocketService;

import java.io.*;
import java.net.*;
import java.util.*;

public class SocketServiceImpl implements SocketService {

    @Override
    public void downloadFile(String url) throws IOException {
        URL website = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) website.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
    }
}
