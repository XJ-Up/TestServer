package com.lou.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Controller
public class UploadController {

    private final static String FILE_UPLOAD_PATH = "D:\\upload\\";

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            return "上传失败，未选择文件";
        }

        StringBuilder responseMessage = new StringBuilder();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                // 生成文件名称通用方法
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Random r = new Random();
                StringBuilder tempName = new StringBuilder();
                tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
                String newFileName = tempName.toString();

                try {
                    // 保存文件
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(FILE_UPLOAD_PATH + newFileName);
                    Files.write(path, bytes);
                    responseMessage.append("上传成功，文件地址为：/files/").append(newFileName).append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    return "上传失败，服务器发生错误";
                }
            } else {
                responseMessage.append("上传失败，文件为空：").append(file.getOriginalFilename()).append("\n");
            }
        }
        return responseMessage.toString();
    }
}