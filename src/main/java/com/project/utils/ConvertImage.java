package com.project.utils;

import com.project.utils.common.exception.ServiceException;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description: 图片与base64互转工具类
 * @author: zhao
 */
@Log4j2
public class ConvertImage {

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @param imgFilePath
     * @return
     */
    public static String GetImageStr(String imgFilePath) {
        byte[] data = null;

        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr
     * @param imgFilePath
     * @return
     */
    public static String GenerateImage(String imgStr, String imgFilePath) {
        if (Tools.isEmpty(imgStr)) {
            return "";
        }
        String[] files = imgStr.split("-");
        List<String> fileList = new ArrayList<String>();

        for (String file : files) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileId = uuid.concat(".").concat("png");

            // 去掉base64前缀(data:image/png;base64,) 替换多余空格，避免影响生成图片无法打开
            String[] newImg = file.split(",");
            String newImgStr = newImg[1].replace(" ", "");
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                // Base64解码
                byte[] bytes = decoder.decodeBuffer(newImgStr);
                for (int i = 0; i < bytes.length; ++i) {
                    if (bytes[i] < 0) { // 调整异常数据
                        bytes[i] += 256;
                    }
                }
                String filePath = imgFilePath + File.separator + fileId;
                File uploadFile = new File(filePath);
                if (!uploadFile.exists()) {
                    FileUtil.createFile(imgFilePath);
                }
                // 生成图片
                OutputStream out = new FileOutputStream(filePath);
                out.write(bytes);
                out.flush();
                out.close();

                fileList.add(fileId);
            } catch (Exception e) {
                log.error("文件上传失败，错误消息：--->" + e.getMessage());
                throw new ServiceException(e.getMessage());
            }
        }
        JSONArray jsonArray = new JSONArray(fileList);
        return jsonArray.toString();
    }

}
