package com.example.downloadserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController {
    @GetMapping("/list")
    @PostMapping("/list")
    public List<String> fileList(@RequestParam(defaultValue = "/d/downloads") String directory,
                                 @RequestParam(defaultValue = "name") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortOrder,
                                 HttpServletResponse response) throws IOException {

        //get absolute path
        directory = new File(directory).getAbsolutePath();

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
        } else if (sortBy.equals("ctime")) {
            comparator = Comparator.comparingLong(File::lastModified);
        }
        if (comparator != null) {
            if (!ascending) {
                comparator = comparator.reversed();
            }
            Arrays.sort(files, comparator);
        }

        List<String> filenames = new ArrayList<>();
        for (File f : files) {
            filenames.add(f.getName());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), filenames);

        return filenames;
    }
}
