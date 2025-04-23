package com.chen.picture.controller;

import com.chen.picture.annotation.AuthCheck;
import com.chen.picture.common.BaseResponse;
import com.chen.picture.common.ResultUtils;
import com.chen.picture.contant.UserContant;
import com.chen.picture.exception.BusinessException;
import com.chen.picture.exception.ErrorCode;
import com.chen.picture.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileContriller {

    @Resource
    private CosManager cosManager;
    /**
     * 测试文件上传
     *
     * @param multipartFile
     * @return
     */
    @AuthCheck(mustRole = UserContant.ADMIN_ROLE)
    @PostMapping("/test/upload")            // @RequestParam 用于提取单个参数，@RequestPart 用于处理文件或复杂对象
    public BaseResponse<String> testUploadFile(@RequestPart("file") MultipartFile multipartFile) {
        // 文件目录
        String filename = multipartFile.getOriginalFilename();
        String filepath = String.format("/test/%s", filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null); //创建一个临时文件
            multipartFile.transferTo(file); //将文件存储到临时文件中
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success(filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

}
