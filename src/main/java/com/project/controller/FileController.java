package com.project.controller;

import com.project.utils.ReturnUtil;
import com.project.utils.common.FileUtil;
import com.project.utils.common.base.ReturnEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 项目控制器
 *
 * @author lisheng
 */
@Api(value = "文件上传API")
@RestController
@Validated
@RequestMapping(value = "/file")
public class FileController {

    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${project.uploadPath}")
    private String uploadPath;

    @ApiOperation(value = "上传图片")
    @PostMapping(value = "/upload")
    public ReturnEntity uploadFile(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase().trim();
        String fileId = uuid.concat(".").concat(fileType);
        FileUtil.uploadFile(file, uploadPath, fileId);
        Map<String, Object> data = new HashMap<>();
        data.put("fileId", fileId);
        data.put("fileType", fileType);
        data.put("fileName", fileName);
        data.put("fileSize", file.getSize());
        return ReturnUtil.success(data);
    }

}
