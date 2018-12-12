package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

/**
 * @author FallingSkies
 * @date 2018/12/12 11:48
 */
@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String file_server_url;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        //获取文件全名
        String originalFilename = file.getOriginalFilename();
        //得到扩展名
        String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        try {
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String fileId = client.uploadFile(file.getBytes(), extName);
            //图片完整地址
            String url = file_server_url + fileId;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
