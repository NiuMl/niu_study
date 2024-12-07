package com.example.demo.interceptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @author niumengliang
 * Date:2024/11/30
 * Time:15:56
 */
public class Tempp {

    static String sourcePath = "/Users/niumengliang/Downloads/temp/mov";
    static String targetPath = "/Users/niumengliang/Downloads/牛牛/mov";

    public static void main(String[] args) throws IOException {
        //本地已有的
        File [] files = new File(targetPath).listFiles();
        List<String> list = Arrays.stream(files).map(File::getName).toList();
        File [] files2 = new File(sourcePath).listFiles();
        for (File file : files2) {
            if(!list.contains(file.getName())){
                System.out.println(file.getName());
                Files.move(file.toPath(),new File(targetPath,file.getName()).toPath());
            }
        }

    }
}
