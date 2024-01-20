package com.example.controller;

import com.example.model.FileInfo;
import com.example.mapper.SettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private SettingMapper settingMapper;

    @GetMapping("/getStorePath")
    @ResponseBody
    public String getStorePath() {

        String path = settingMapper.get().getStorePath();
        path = new File(path).getAbsolutePath();
        return path;
    }

    private static final Set<String> video = new HashSet<>();
    private static final Set<String> image = new HashSet<>();

    private static final Set<String> archive = new HashSet<>();

    private static final Set<String> document = new HashSet<>();

    static {
        video.add("mp4");
        video.add("mov");

        image.add("png");
        image.add("jpg");
        image.add("gif");

        archive.addAll(Arrays.asList("zip", "rar", "tar"));

        document.addAll(Arrays.asList("pptx", "docx", "xlsx"));
    }

    @GetMapping("/list")
    @ResponseBody
    public List<FileInfo> fileList(@RequestParam(defaultValue = "D:\\Downloads") String directory,
                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                   @RequestParam(defaultValue = "desc") String sortOrder,
                                   @RequestParam(defaultValue = "all") String filter,
                                   HttpServletResponse response) throws IOException {


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

            String[] split = f.getName().split("\\.");
            String ext = split[split.length - 1];
            if(!judgeExt(ext, filter)){
                continue;
            }

            long createTime = f.lastModified();
            Date createDate = new Date(createTime);

            fileInfo.setName(f.getName());

            if(f.length()==0){
                fileInfo.setSize("");
            }else if(f.length()>0 && f.length()<1024){
                double size = f.length();
                fileInfo.setSize(formatSize(size)+"B");
            }else if(f.length()>1024 && f.length()<1024*1024){
                double size = f.length()/1024.0;
                fileInfo.setSize(formatSize(size)+"KB");
            }else if(f.length()>1024*1024 && f.length()<1024*1024*1024){
                double size = f.length()/1024.0/1024.0;
                fileInfo.setSize(formatSize(size)+"MB");
            }else if(f.length()>1024*1024*1024){
                double size = f.length()/1024.0/1024.0/1024.0;
                fileInfo.setSize(formatSize(size)+"GB");
            }
            fileInfo.setCreatedAt(sdf.format(createDate));
            fileInfo.setDirectory(f.isDirectory());
            fileInfo.setPath("\\" + f.getName());
            filenames.add(fileInfo);
        }

        return filenames;
    }


    private static boolean judgeExt(String ext, String filter){
        ext = ext.toLowerCase();
        switch (filter){
            case "all":
                return true;
            case "video":
                return video.contains(ext);
            case "image":
                return image.contains(ext);
            case "archive":
                return archive.contains(ext);
            case "document":
                return document.contains(ext);
            default:
                return false;
        }
    }

    private static String formatSize(double size) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(size);
    }
}
