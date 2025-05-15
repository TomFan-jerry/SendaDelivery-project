package com.senda.controller.admin;

import com.senda.constant.MessageConstant;
import com.senda.result.Result;
import com.senda.utils.R2Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private R2Util r2Util;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传，文件名:{}", file);
        String url = null;
        try {
            url = r2Util.upload(file);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
        log.info("文件上传完成，文件访问的url:{}", url);
        return Result.success(url);
    }

}
