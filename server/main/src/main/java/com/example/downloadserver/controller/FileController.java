package com.example.downloadserver.controller;

import com.example.downloadserver.model.FileInfo;
import com.example.downloadserver.mapper.SettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private SettingMapper settingMapper;

    @GetMapping("/list")
    @ResponseBody
    public List<FileInfo> fileList(@RequestParam(defaultValue = "/d/downloads") String directory,
                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                   @RequestParam(defaultValue = "desc") String sortOrder,
                                   HttpServletResponse response) throws IOException {

        directory = settingMapper.get().getStorePath();
        // Check if directory is empty or null
        if (directory == null || directory.isEmpty()) {
            return null;
        }
        //get absolute path
        directory = new File(directory).getAbsolutePath();
        // Check if the directory exists
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("The specified directory does not exist.");
            return null;
        }

        File[] files = new File(directory).listFiles();


        boolean ascending = true;
        if (sortOrder.equalsIgnoreCase("desc")) {
            ascending = false;
        }

        Comparator<File> comparator = null;
        if (sortBy.equals("size")) {
            comparator = Comparator.comparing(File::length);
        } else if (sortBy.equals("name")) {
            comparator = Comparator.comparing(File::getName);
        } else if (sortBy.equals("createdAt")) {
            comparator = Comparator.comparingLong(File::lastModified);
        }
        if (comparator != null) {
            if (!ascending) {
                comparator = comparator.reversed();
            }
            Arrays.sort(files, comparator);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        List<FileInfo> filenames = new ArrayList<>();
        for (File f : files) {
            FileInfo fileInfo = new FileInfo();

            long createTime = f.lastModified();
            Date createDate = new Date(createTime);

            fileInfo.setFilename(f.getName());
            fileInfo.setSize(String.valueOf(f.length()));

            fileInfo.setCreatedAt(sdf.format(createDate));
            filenames.add(fileInfo);
        }


        return filenames;
    }
}
