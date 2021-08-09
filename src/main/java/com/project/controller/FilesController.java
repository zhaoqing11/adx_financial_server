package com.project.controller;

import com.project.utils.FileUtil;
import com.project.utils.ReturnUtil;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.web.RangeFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @description: 项目控制器
 * @author: zhao
 */
@Api(value = "文件上传API")
@RestController
@RequestMapping(value = "/file")
public class FilesController {

    private static Logger logger = LoggerFactory.getLogger(FilesController.class);

    @Value("${project.rootFolder}")
    private String rootFolder;

    @Value("${project.imagesPath}")
    private String imagesPath;

    @ApiOperation(value = "上传图片")
    @PostMapping(value = "/upload")
    public ReturnEntity uploadFile(MultipartFile[] fileList) throws IOException {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (MultipartFile file : fileList) {
            Map<String, Object> data = new HashMap<>();

            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = file.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase().trim();
            String fileId = uuid.concat(".").concat(fileType);
            FileUtil.uploadFile(file, rootFolder + imagesPath, fileId);

            data.put("fileId", fileId);
            data.put("fileType", fileType);
            data.put("fileName", fileName);
            data.put("fileSize", file.getSize());

            mapList.add(data);
        }
        return ReturnUtil.success(mapList);
    }

    @ApiOperation(value = "获取文件流", httpMethod = "GET", notes = "获取文件")
    @GetMapping(value = "get/{fileId:.+}")
    @ResponseBody
    public void getFile(@PathVariable("fileId") String fileId,
                        @RequestParam(value = "disposition", required = false, defaultValue = "inline") String disposition) {
        String filePath = rootFolder + imagesPath + fileId;
        File file = new File(filePath);
        if (!file.exists()) {
            getHttpServletResponse().setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        try {
            RangeFile rangeFile = new RangeFile(file, disposition);
            rangeFile.render(getHttpServletRequest(), getHttpServletResponse(), true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获取 Request
     *
     * @return
     */
    protected HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        } else {
            return requestAttributes.getRequest();
        }
    }

    /**
     * 获取 Response
     *
     * @return
     */
    protected HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        } else {
            return requestAttributes.getResponse();
        }
    }
}
