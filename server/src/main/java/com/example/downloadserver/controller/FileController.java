package com.example.downloadserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
@Controller
@RequestMapping("/file")
public class FileController {
    @GetMapping("/list")
    @PostMapping("/list")
    public String fileList(@RequestParam(defaultValue = "/d/downloads") String directory,
                           @RequestParam(defaultValue = "name") String sortBy,
                           @RequestParam(defaultValue = "asc") String sortOrder,
                           Model model) {

        File dir = new File(directory);
        File[] files = dir.listFiles();

        // 根据排序方式对文件进行排序
        boolean ascending = true;
        if (sortOrder.equalsIgnoreCase("desc")) {
            ascending = false;
        }

        Comparator<File> comparator = null;
        if (sortBy.equals("size")) {
            comparator = Comparator.comparing(File::length);
        } else if (sortBy.equals("name")) {
            comparator = Comparator.comparing(File::getName);
        } else if (sortBy.equals("ctime")) {
            comparator = Comparator.comparingLong(File::lastModified);
        }
        if (comparator != null) {
            if (!ascending) {
                comparator = comparator.reversed();
            }
            Arrays.sort(files, comparator);
        }

        model.addAttribute("files", files);
        return "fileList";
    }
}
