package com.example.downloadserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
@Controller
@RequestMapping("/file")
public class FileController {

    @GetMapping("/list")
    public String fileList(@RequestParam(defaultValue = "/d/download") String directory,
                           @RequestParam(defaultValue = "name") String sortBy, Model model) {

        File dir = new File(directory);
        File[] files = dir.listFiles();


        // 根据排序方式对文件进行排序
        if (sortBy.equals("size")) {
            Arrays.sort(files, Comparator.comparing(File::length));
        } else if (sortBy.equals("name")) {
            Arrays.sort(files, Comparator.comparing(File::getName));
        } else if (sortBy.equals("ctime")) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        }


        model.addAttribute("files", files);
        return "fileList";
    }
}
