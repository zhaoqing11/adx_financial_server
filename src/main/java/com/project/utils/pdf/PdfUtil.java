package com.project.utils.pdf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.project.entity.ImageVO;
import com.project.entity.Project;
import com.project.utils.FileUtil;
import com.project.utils.ImageUtil;
import com.project.utils.Tools;
import com.project.utils.common.exception.ServiceException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PdfUtil {

    static Logger logger = LogManager.getLogger(PdfUtil.class);

    static String rootFolder = "/SCResource";

    static String imagesPath = "/Projects/images/";

    private static String fontFolder = "/YbimCurtainResource/font/simsun.ttc,0";  // "C:\\Windows\\Fonts\\SIMSUN.TTC,1"

    private static String tempFilePath = "D:\\tempFilePath";

    private static final float[] TABLE_WIDTHS = {88, 88, 88, 88, 88, 88};

    /**
     * 创建document实例
     *
     * @param response
     * @param map      显示数据
     */
    public static void createPdfDocument(HttpServletResponse response, Project project,
                                         Map<String, List<Map<String, Object>>> map,
                                         List<ProjectFieldValues> newProjectFields) {
        String filePath = "";
        String path = "";
        try {
            for (Map.Entry<String, List<Map<String, Object>>> item : map.entrySet()) {

                String fileName = item.getKey();
                List<Map<String, Object>> listMap = item.getValue();

                filePath = tempFilePath + File.separator;
                path = filePath + fileName + ".pdf";
                File file = new File(path);
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }

                // 创建文本对象
                Rectangle pageSize = new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                Document document = new Document(pageSize);
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

                writer.getPageNumber();

                //文档属性
                document.addTitle(fileName);
                document.open();

                //表格处理   创建表格时必须指定表格的列数
                PdfPTable table = new PdfPTable(TABLE_WIDTHS);
                table.setLockedWidth(true);
                table.setTotalWidth(530);
                table.setHorizontalAlignment(Element.ALIGN_CENTER);

                pdfCover(table, project.getIdProject(), project.getProjectName(), project.getCheckDate());
                pdfTitle(table);
                pdfContent(table, listMap, newProjectFields);
                document.add(table);

                fileName = fileName + ".pdf";
                document.close();

                String outPath = setPageNumber(path, fileName);
                downloadPdf(response, fileName, outPath);

                // 删除临时文件
                FileUtil.deleteFile(path);
                FileUtil.deleteFile(outPath);
            }
        } catch (IOException io) {
            logger.error(io);
            throw new ServiceException("创建文件异常：" + io.getMessage());
        } catch (DocumentException e) {
            logger.error(e);
            throw new ServiceException("输出pdf文件流失败：" + e.getMessage());
        }
    }

    /**
     * 追加页码
     * @param pdfPath 文件路径
     */
    public static String setPageNumber(String pdfPath, String fileName) {
        PdfReader reader = null;
        PdfStamper stamper = null;
        String outFilePath = "";
        try {
            outFilePath = "D:\\tempFilePath\\Files" + File.separator;
            outFilePath = outFilePath + fileName;
            File file = new File(outFilePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }

            // 创建一个pdf读入流
            reader = new PdfReader(pdfPath);
            // 根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
            stamper = new PdfStamper(reader, new FileOutputStream(outFilePath));
            // 这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
            BaseFont bf = BaseFont.createFont(fontFolder, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            // baseFont不支持字体样式设定.但是font字体要求操作系统支持此字体会带来移植问题.
            Font font = new Font(bf, 10);
            font.setStyle(Font.BOLD);
            font.getBaseFont();
            // 获得宽
            Rectangle pageSize = reader.getPageSize(1);
            float width = pageSize.getWidth();
            // 获取页码
            int num = reader.getNumberOfPages();
            for (int i = 1; i <= num; i++) {
                PdfContentByte over = stamper.getOverContent(i);

                over.beginText();
                over.setFontAndSize(font.getBaseFont(), 13);
                over.setColorFill(BaseColor.BLACK);
                over.setTextMatrix((int) width / 2, 15); // 设置页码在页面中的坐标
                over.showText(i + "/" + num);
                over.endText();
                over.stroke();
            }
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return outFilePath;
    }

    private static String formatProjectNo(int num, Integer projectNo) {
        String newStr = "";
        int len = projectNo.toString().length();
        for (int i = len; i < num; i++) {
            newStr += "0";
        }
        return newStr + projectNo;
    }

    /**
     * 设置封面模板
     * @param table
     * @param projectNo 编号
     * @param projectName 项目名称
     * @param checkDate 检查日期
     */
    private static void pdfCover(PdfPTable table, Integer projectNo,
                                 String projectName, String checkDate) {
        String newCode = "GZ-" + Tools.date2Str(new Date(), "yyyy")
                + "-" + formatProjectNo(3, projectNo);
        Date newDate = Tools.str2DateFormat(checkDate.trim(), "yyyy-MM-dd");

        // 表格的单元格
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Paragraph("编号：" + newCode, setChineseFont(16)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(40);
        cell.setBorder(0);
        cell.setColspan(6);
        table.addCell(cell);

         cell = new PdfPCell();
        cell.setPhrase(new Paragraph("既有玻璃幕墙安全检查评估表", setChineseFont(26)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setMinimumHeight(200);
        cell.setBorder(0);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell();

        Chunk dateUnderline = new Chunk(projectName);
        dateUnderline.setUnderline(0.1f, -2f);

        Paragraph para = new Paragraph("", setChineseFont(22));
        para.add(dateUnderline);

        cell.setPhrase(para);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(50);
        cell.setBorder(0);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPhrase(new Paragraph("检查评估小组", setChineseFont(22)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setMinimumHeight(220);
        cell.setBorder(0);
        cell.setColspan(6);
        table.addCell(cell);

        cell = new PdfPCell();

        dateUnderline = new Chunk( Tools.date2Str(newDate, "yyyy年MM月dd日"));
        dateUnderline.setUnderline(0.1f, -2f);

        para = new Paragraph("", setChineseFont(22));
        para.add(dateUnderline);

        cell.setPhrase(para);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setMinimumHeight(250);
        cell.setPaddingTop(12);
        cell.setBorder(0);
        cell.setColspan(6);
        table.addCell(cell);

//        cell = new PdfPCell();
//        cell.setPhrase(new Paragraph("广州市既有幕墙安全监管平台系统 https://www.ai-jymq.com/curtain", setChineseFont(14)));
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        cell.setMinimumHeight(50);
//        cell.setBorder(0);
//        cell.setColspan(6);
//        table.addCell(cell);
    }

    /**
     * 下载 pdf
     *
     * @param response
     * @param fileName
     * @param path
     */
    public static void downloadPdf(HttpServletResponse response, String fileName, String path) {
        OutputStream out = null;
        FileInputStream in = null;
        try {
            response.setContentType("multipart/form-data"); // 1.自动判断下载文件类型（multipart/form-data）  2.application/pdf
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String((fileName).getBytes("UTF-8"), "ISO-8859-1"));
            out = response.getOutputStream();
//            ba.writeTo(out);//将字节数组输出流中的数据写入指定的OutputStream输出流中.

            in = new FileInputStream(path);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = in.read(buffer)) != -1) {
                out.write(buffer, 0, i);
            }

        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException("读写文件流异常： " + e.getMessage());
        } finally {
            try {
                out.flush();
                out.close();
                in.close();
                out = null;
            } catch (IOException e) {
                logger.error(e);
                throw new ServiceException("关闭文件流异常");
            }
        }
    }

    /**
     * 设置标题
     *
     * @param table
     * @throws Exception
     */
    private static void pdfTitle(PdfPTable table) {
        // 表格的单元格
        PdfPCell cell = new PdfPCell();
        // 向单元格中插入数据
        // new Paragraph()是段落的处理，可以设置段落的对齐方式，缩进和间距。
        cell.setPhrase(new Paragraph("既有玻璃幕墙安全检查评估表", setChineseFont(18)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(40);
        cell.setBorder(0);
        cell.setColspan(6);
        table.addCell(cell);
    }

    /**
     * 设置项目编号
     *
     * @param table
     * @param projectNo
     * @throws Exception
     */
    private static void pdfProjectNo(PdfPTable table, String projectNo) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Paragraph(projectNo, setChineseFont(8)));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setMinimumHeight(30);
        cell.setBorder(0);
        cell.setColspan(6);
        table.addCell(cell);
    }

    /**
     * 设置pdf内容
     *
     * @param table
     * @param dataList
     */
    private static void pdfContent(PdfPTable table, List<Map<String, Object>> dataList,
                                   List<ProjectFieldValues> newProjectFields) {
        String url = "";
        String projectName = "";
        for (Map<String, Object> map : dataList) {
            for (String key : map.keySet()) {

                int n = 0;
                // 表格的单元格
                PdfPCell cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setMinimumHeight(30);

                cell.setPhrase(new Paragraph(map.get(key).toString(), setChineseFont(10)));

                // 指定 维护责任人/联系人 两项为一排显示四个单元值
                if (key.equals("personMaintenancePhone-value")) {
                    cell.setColspan(5);
                }
                // 现场检查照片
                if (key.equals("checkPhoto-key")) {
                    // 处理动态新增字段并显示在结果前面
                    if (newProjectFields.size() > 0) {
                        for (ProjectFieldValues item: newProjectFields) {
                            // key
                            PdfPCell cell_add = new PdfPCell();
                            cell_add.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell_add.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            cell_add.setMinimumHeight(30);
                            cell_add.setPhrase(new Paragraph(item.getFieldName(), setChineseFont(10)));
                            cell_add.setColspan(6);
                            table.addCell(cell_add);

                            // values
                            setImage(item.getValue(), cell, table, projectName, url);
                        }
                    }

                    PdfPCell cell_r = new PdfPCell();
                    cell_r.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell_r.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell_r.setMinimumHeight(30);
                    cell_r.setPhrase(new Paragraph("四、现场检查照片", setChineseFont(12)));
                    cell_r.setColspan(6);
                    table.addCell(cell_r);
                    continue;
                }
                if (key.equals("checkPhoto-value")) {
                    setImage(map.get(key).toString(), cell, table, projectName, url);
                    continue;
                }

                // 单选题 独占一行
                String[] tmpStr = key.split("-");
                String index = tmpStr[0].length() == 3 ? tmpStr[0].substring(1, 3) : tmpStr[0].substring(1, 2);
                if (key.equals("r" + index + "-key")) {
                    if (index.equals("1") || index.equals("14")) {
                        PdfPCell cell_r = new PdfPCell();
                        cell_r.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell_r.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell_r.setMinimumHeight(30);
                        if (index.equals("1")) {
                            cell_r.setPhrase(new Paragraph("一、玻璃幕墙内业检查", setChineseFont(12)));
                        } else {
                            cell_r.setPhrase(new Paragraph("二、玻璃幕墙现场局部实体检查", setChineseFont(12)));
                        }
                        cell_r.setColspan(6);
                        table.addCell(cell_r);

                        if (index.equals("1")) {
                            PdfPCell cell_t = new PdfPCell();
                            cell_t.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell_t.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            cell_t.setMinimumHeight(30);

                            cell_t.setPhrase(new Paragraph("检查内容", setChineseFont(12)));
                            cell_t.setColspan(4);
                            table.addCell(cell_t);

                            cell_t.setPhrase(new Paragraph("检查情况", setChineseFont(12)));
                            cell_t.setColspan(1);
                            table.addCell(cell_t);

                            cell_t.setPhrase(new Paragraph("备注", setChineseFont(12)));
                            cell_t.setColspan(1);
                            table.addCell(cell_t);
                        }
                    }
                    if (index.equals("17") || index.equals("19")) {
                        cell.setBorderWidthBottom(0);
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setColspan(4);
                } else if (key.equals("r" + index + "-value")) {
//                    if (index.equals("17") || index.equals("19")) { // 等于 r17 / r19 追加输出备注

                        Map<String, Object> mp = JSONObject.parseObject(map.get(key).toString());
                        for (Map.Entry<String, Object> item : mp.entrySet()) {
                            cell.setPhrase(new Paragraph(item.getKey(), setChineseFont(10)));
                            cell.setColspan(1);
                            table.addCell(cell);

                            if (Tools.isEmpty(item.getValue().toString())) {
                                // Todo: 输出空cell
                                PdfPCell cell_v = new PdfPCell();
                                cell_v.setHorizontalAlignment(Element.ALIGN_CENTER);
                                cell_v.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                cell_v.setPhrase(new Paragraph(""));
                                cell_v.setColspan(1);
                                table.addCell(cell_v);
                                continue;
                            }

                            List<ImageVO> images = JSONObject.parseArray(item.getValue().toString(), ImageVO.class);
                            String temp = "";
                            for (ImageVO tmp: images) {
                                temp += tmp.getRemark();
                            }
                            PdfPCell cell_k = new PdfPCell();
                            cell_k.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell_k.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            cell_k.setPhrase(new Paragraph(temp, setChineseFont(10)));
                            cell_k.setColspan(1);
                            table.addCell(cell_k);
                        }

                        if (index.equals("28")) {
                            PdfPCell cell_k = new PdfPCell();
                            cell_k.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell_k.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            cell_k.setPhrase(new Paragraph("注：本项检查不替代维护责任主体法定责任；实体检查为局部检查，不代表该幕墙整体情况。", setChineseFont(10)));
                            cell_k.setColspan(6);
                            cell_k.setMinimumHeight(30);
                            table.addCell(cell_k);
                        }
//                    } else {
//                      table.addCell(cell);
//                    }
                    n = -1;
                } else if (key.equals("t" + index + "-key")) { // 处理签名字段
                    if (index.equals("1")) {
                        PdfPCell cell_title = new PdfPCell();
                        cell_title.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cell_title.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell_title.setMinimumHeight(30);
                        cell_title.setPhrase(new Paragraph("六、签名栏", setChineseFont(12)));
                        cell_title.setColspan(6);
                        table.addCell(cell_title);
                    }
                    // Todo: 暂时不输出 t1,t2 key
                    if (index.equals("1") || index.equals("2")) {
                        continue;
                    }
                    cell.setColspan(1);
                } else if (key.equals("t" + index + "-value")) { // 处理签名内容
                    try {
                        // Todo: 暂时不输出 t1,t2 value
                        if (index.equals("1") || index.equals("2")) {
                            continue;
                        }
                        Object[] images = null;
                        if (!Tools.isEmpty(map.get(key).toString())) {
                            JSONArray json = JSON.parseArray(map.get(key).toString());
                            images = json.toArray();
                        }

                        for (int i = 0; i < 5; i++) {
                            if (images != null &&
                                    i <= images.length - 1) {
                                Object obj = images[i];
                                cell = convertImage(cell, String.valueOf(obj), false);
                                cell.setFixedHeight((float) 35);
                                cell.setColspan(1);
                                table.addCell(cell);
                            } else {
                                cell.setPhrase(new Paragraph(""));
                                cell.setFixedHeight((float) 35);
                                cell.setColspan(1);
                                table.addCell(cell);
                            }
                        }
                        n = -1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // 多选题
                    String[] tmpStr_c = key.split("-");
                    String index_c = tmpStr_c[0].length() == 3 ? tmpStr_c[0].substring(1, 3) : tmpStr_c[0].substring(1, 2);
                    if (key.equals("c" + index_c + "-key")) {
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        if (index_c.equals("14")) {
                            PdfPCell cell_title = new PdfPCell();
                            cell_title.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell_title.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            cell_title.setMinimumHeight(30);
                            cell_title.setPhrase(new Paragraph("五、检查结论", setChineseFont(12)));
                            cell_title.setColspan(6);
                            cell_title.setBorderWidthBottom(0);
                            table.addCell(cell_title);

                            cell.setColspan(6);
                            cell.setBorderWidthTop(0);
                            cell.setBorderWidthBottom(0);
                        } else {
                            if (index_c.equals("1")) {
                                PdfPCell cell_r = new PdfPCell();
                                cell_r.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell_r.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                cell_r.setMinimumHeight(30);
                                cell_r.setPhrase(new Paragraph("三、存在问题，整改要求与建议", setChineseFont(12)));
                                cell_r.setColspan(6);
                                table.addCell(cell_r);
                            }
                            cell.setColspan(2);
                        }
                    }
                    if (key.equals("c" + index_c + "-value")) {
                        if (map.get(key).toString() == "") {
                            cell.setPhrase(new Paragraph("", setChineseFont(10)));
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell.setColspan(1);
                            table.addCell(cell);

                            PdfPCell cell_img = new PdfPCell();
                            cell_img.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell_img.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            cell_img.setMinimumHeight(30);
                            cell_img.setPhrase(new Paragraph("", setChineseFont(10)));
                            cell_img.setColspan(6);
                            table.addCell(cell_img);
                        } else {
                            // 过滤检查结果
                            if (index_c.equals("14")) {
                                String[] newArrays = map.get(key).toString().split(";");

                                cell.setColspan(6);
                                cell.setBorderWidthTop(0);
                                cell.setMinimumHeight(10);
                                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell.setBorderWidthBottom(0);
                                cell.setPhrase(new Paragraph(newArrays[0], setChineseFont(11)));
                                cell.setPaddingLeft(10);
                                table.addCell(cell);

                                String advise = "";
                                if (newArrays.length < 2) {
                                    advise = "";
                                } else {
                                    advise = newArrays[1];
                                }

                                PdfPCell cell_r = new PdfPCell();
                                cell_r.setHorizontalAlignment(Element.ALIGN_LEFT);
                                cell_r.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                cell_r.setMinimumHeight(30);
                                cell_r.setPhrase(new Paragraph("要求及建议：" + advise, setChineseFont(10)));
                                cell_r.setBorderWidthTop(0);
                                cell_r.setPaddingLeft(10);
                                cell_r.setColspan(6);
                                table.addCell(cell_r);
                            } else {
                                Map<String, Object> mp = JSONObject.parseObject(map.get(key).toString());
                                for (Map.Entry<String, Object> item : mp.entrySet()) {
                                    cell.setPhrase(new Paragraph(item.getKey(), setChineseFont(10)));
                                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                    cell.setColspan(4);
                                    table.addCell(cell);

                                    // Todo: c1只输出备注，不用输出图片
                                    if (index_c.equals("1")) {
                                        if (Tools.isEmpty(item.getValue().toString())) {
                                            continue;
                                        }
                                        List<ImageVO> images = JSONObject.parseArray(item.getValue().toString(), ImageVO.class);
                                        for (int i = 0; i < 3; i++) { // 默认设置三个单元格存放备注
                                            cell = new PdfPCell();
                                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                            cell.setMinimumHeight(30);//设置表格行高

                                            if (i < images.size()) {
                                                ImageVO file = images.get(i);
                                                if (file != null) {
                                                    cell.setPhrase(new Paragraph(file.getRemark(), setChineseFont(10)));
                                                    cell.setColspan(2);
                                                }
                                            } else { // 填补空数据
                                                cell.setPhrase(new Paragraph("", setChineseFont(10)));
                                                cell.setColspan(2);
                                            }
                                            table.addCell(cell);
                                        }
                                    } else {
                                        setImage(item.getValue().toString(), cell, table, projectName, url);
                                    }
                                }
                            }
                        }
                        n = -1;
                    }
                }
                if (n == 0) {
                    table.addCell(cell);
                }
            }
        }
    }

    /**
     * 图片处理
     * @param image
     * @param cell
     * @param table
     * @param projectName
     * @param url
     */
    private static void setImage(String image, PdfPCell cell, PdfPTable table,
                                 String projectName, String url){
        if (!Tools.isEmpty(image)) {
            PdfPCell cell_img = new PdfPCell();
            cell_img.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_img.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell_img.setMinimumHeight(30);
            List<ImageVO> images = JSONObject.parseArray(image, ImageVO.class);
            File fe = new File(tempFilePath);
            if (!fe.exists()) {
                fe.mkdirs();
            }

            try {
                // 默认设置三个单元格存放描述图片
                for (int i = 0; i < 3; i++) {
                    cell = new PdfPCell();
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setMinimumHeight(30);//设置表格行高

                    if (i < images.size()) {
                        ImageVO file = images.get(i);
                        if (file != null) {
                            cell = convertImage(cell, file.getUrl(), true);
                            cell.setColspan(2);
                        }
                    } else { // 填补空数据
                        cell.setPhrase(new Paragraph("", setChineseFont(8)));
                        cell.setColspan(2);
                        cell.setFixedHeight((float) 150.0);
                    }
                    cell.setBorderWidthBottom(0);
                    table.addCell(cell);
                }

                // 输出图片对应备注
                for (int i = 0; i < 3; i++) {
                    cell = new PdfPCell();
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setMinimumHeight(30);//设置表格行高

                    if (i < images.size()) {
                        ImageVO file = images.get(i);
                        if (file != null) {
                            cell.setPhrase(new Paragraph(file.getRemark(), setChineseFont(8)));
                            cell.setColspan(2);
                        }
                    } else { // 填补空数据
                        cell.setPhrase(new Paragraph("", setChineseFont(8)));
                        cell.setColspan(2);
                    }
                    cell.setBorderWidthTop(0);
                    table.addCell(cell);
                }
            } catch (IOException io) {
                logger.error(io);
                if (io.getMessage().equals("Can't get input stream from URL!")) {
                    throw new ServiceException("抱歉，系统未找到项目（" + projectName + "）目标文件" + url);
                }
                throw new ServiceException("导出失败，文件流读写异常，请联系平台管理员。");
            }
        } else { // 若图片不存在，则创建空单元格
            for (int i = 0; i < 3; i++) { // 默认设置三个单元格存放描述图片
                cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setMinimumHeight(30);//设置表格行高
                cell.setPhrase(new Paragraph("", setChineseFont(8)));
                cell.setColspan(2);
                table.addCell(cell);
            }
        }
    }

    /**
     * 设置中文字体
     *
     * @return
     */
    private static Font setChineseFont(int fontSize) {
        // 使用iTextAsian.jar中的字体
        BaseFont bfChinese = null;
        try {
            bfChinese = BaseFont.createFont(fontFolder, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            logger.error(e);
            throw new ServiceException("抱歉，系统查找指定字体不存在。" + e.getMessage());
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("读取文件流异常：" + e.getMessage());
        }
        Font fontChinese = new Font(bfChinese, fontSize, Font.NORMAL);
        return fontChinese;
    }

    /**
     * 图片处理
     *
     * @param cell
     * @param fileId
     * @return
     * @throws IOException
     */
    public static PdfPCell convertImage(PdfPCell cell, String fileId, boolean flag) throws IOException {
        try {
            int strIndex = fileId.lastIndexOf(".");
            String fileType = fileId.substring(strIndex + 1, fileId.length());

            String filePath = rootFolder + imagesPath + fileId;
            File destFile = new File(filePath); // 输出目录
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageUtil.changeSize(destFile, os, fileType, 800, 800);
            ImageUtil.writeFile(os, destFile);

            Image image = Image.getInstance(filePath);
            cell = new PdfPCell(image, true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(3);
            if (flag) {
                cell.setPadding(10);
            } else {
//                cell.setPaddingTop(10);
//                cell.setPaddingLeft(20);
//                cell.setPaddingRight(20);
            }
            cell.setFixedHeight((float) 150.0);
        } catch (BadElementException e) {
            logger.error(e);
            throw new ServiceException("导出失败，请联系平台管理员。");
        }
        return cell;
    }
}
