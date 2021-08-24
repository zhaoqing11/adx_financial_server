package com.project.utils.excel;

import com.project.entity.PublicReport;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Excel导入导出工具类
 *
 * @author: zhao
 */
public class ExcelUtil {

    static Logger logger = LogManager.getLogger(ExcelUtil.class);

    @Value("${project.downloadUrl}")
    private static Integer downloadUrl;

    static final String[] ANALYSIS_DATA_COLUMN_FIELD = new String[]{"日期", "上日余额", "本月收入", "本月支出", "手续费"};

    /**
     * 导出月报统计
     * @param dataList
     * @return
     */
    @SuppressWarnings("deprecation")
    public static HSSFWorkbook exportAnalysisData(List<Map<String, Object>> dataList, String fileName) {
        HSSFWorkbook wb = createWorkBook();
        HSSFSheet sheet = createSheet(wb, fileName);
        HSSFCellStyle style = setCellStyle(wb, String.valueOf(FontEnum.FONT_TYPE6), Integer.valueOf(FontEnum.FONT_SIZE1),
                false,true, false);
        CellRangeAddress range = new CellRangeAddress(0, 0, 0, 4);
        for (int i = 0; i < 5; i++) {
            sheet.setColumnWidth(i, 256 * 15);
        }

        HSSFRow row1 = createRow(sheet,0,30); // rowIndex 1
        setFieldColumnName(row1, ANALYSIS_DATA_COLUMN_FIELD, style); // 设置标题列值

        HSSFCellStyle contentStyle = setCellStyle(wb, String.valueOf(FontEnum.FONT_TYPE1), Integer.valueOf(FontEnum.FONT_SIZE1),
                false, true, false);
        setRegionStyle(range, sheet, wb, contentStyle);

        // 格式化数据
        List<String[]> newData = formatDataList(dataList);

        HSSFRow row2 = null;
        HSSFCell cell2 = null;
        for (int i = 0; i < newData.size(); i++) {
            String[] array = newData.get(i);
            row2 = createRow(sheet, (int)i + 1,25);
            for (int j = 0; j < array.length; j++) {
                cell2 = row2.createCell((short) j);
                cell2.setCellStyle(contentStyle);
                cell2.setCellValue(array[j]);
            }
        }

        return wb;
    }

    /**
     * 格式化数据
     * @param dataList
     * @return
     */
    public static List<String[]> formatDataList(List<Map<String, Object>> dataList) {
        List<String[]> newData = new ArrayList<String[]>();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> data = dataList.get(i);
            String[] formatData = new String[5];

            String day = String.valueOf(data.get("day")) ;
            PublicReport report = (PublicReport) data.get("report");

//            ObjectMapper objectMapper = new ObjectMapper();
//            PublicReport report = objectMapper.convertValue(entry.getValue(), PublicReport.class);
            if (i >= dataList.size() - 1) {
                formatData[0] = "合计";
            } else {
                formatData[0] = day;
            }
            formatData[1] = report.getLastRemainingSum();
            formatData[2] = report.getCollectionAmount();
            formatData[3] = report.getPayAmount();
            formatData[4] = report.getServiceCharge();
            newData.add(formatData);
        }
        return newData;
    }

    public static void downExcel(HttpServletResponse response, HSSFWorkbook wb, String fileName) {
        OutputStream os = null;
        response.reset();
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes("UTF-8"), "ISO-8859-1"));
            //response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(excelName + ".xls", TygConstants.ALL_ENCODING));
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException("文件下载异常" + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                logger.error(e);
                throw new RuntimeException("读写文件流异常" + e.getMessage());
            }
        }
    }

    public static String downExcel2(HttpServletRequest request, HttpServletResponse response, HSSFWorkbook wb, String fileName) {
        FileOutputStream os = null;
        String filePath = null;
        try {
            filePath = downloadUrl + File.separator;
            String newfileName = fileName + ".xlsx";

            File fileDir = new File(filePath);
            if (!fileDir.exists() && !fileDir.isDirectory()) {
                fileDir.mkdirs();
            }

            os = new FileOutputStream(filePath + File.separator + newfileName);
            wb.write(os);

        } catch (Exception e) {
            throw new ServiceException("文件下载异常" + e.getMessage());
        } finally {
            try {
                os.flush();
                os.close();
                os = null;
            } catch (IOException e) {
                logger.error(e);
                throw new ServiceException("关闭文件流失败：" + e.getMessage());
            }
        }
        return filePath;
    }

    public static void zipDirectory(HttpServletResponse response, String filePath, List<String> fileNames) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            String zipName = "幕墙数据库建筑资料.zip";
            //导出压缩文件的全路径
            String zipFilePath = filePath + zipName;
            File zip = new File(zipFilePath);
            //将excel文件生成压缩文件
            File srcfile[] = new File[fileNames.size()];
            for (int j = 0, n1 = fileNames.size(); j < n1; j++) {
                srcfile[j] = new File(fileNames.get(j));
            }
            ZipFiles(srcfile, zip);
            response.setContentType("application/x-zip-compressed");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((zipName).getBytes("UTF-8"), "ISO-8859-1"));

            outputStream = response.getOutputStream();
            inputStream = new FileInputStream(zipFilePath);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }

            try {
                delAllFile(filePath); // 删除完里面所有内容
                filePath = filePath.toString();
                java.io.File myFilePath = new java.io.File(filePath);
                myFilePath.delete(); // 删除空文件夹
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException io) {
            logger.error(io);
            throw new ServiceException("读写文件流异常：" + io.getMessage());
        } catch (Exception e) {
            logger.error(e);
            throw new ServiceException("压缩文件失败：" + e.getMessage());
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                outputStream = null;
            } catch (IOException e) {
                logger.error(e);
                throw new ServiceException("关闭文件流异常");
            }
        }

    }

    /**
     *  压缩文件
     * @param srcfile
     * @param zipfile
     */
    public static void ZipFiles(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            logger.error(e);
            throw new ServiceException("读写文件流失败");
        }
    }

    /***
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static  boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 创建HSSFWorkbook
     * @return
     */
    public static HSSFWorkbook createWorkBook() {
        HSSFWorkbook wb = new HSSFWorkbook();
        return wb;
    }

    /**
     * 创建sheet工作表
     * @param wb
     * @return
     */
    public static HSSFSheet createSheet(HSSFWorkbook wb, String sheetName) {
        return wb.createSheet(sheetName);
    }

    /**
     * 创建行
     * @param rowIndex
     * @param height
     * @return
     */
    public static HSSFRow createRow(HSSFSheet sheet, Integer rowIndex, Integer height) {
        HSSFRow row = sheet.createRow(rowIndex);
        row.setHeightInPoints(height);
        return row;
    }

    /**
     * 设置字体及表格样式
     * @param wb
     * @param fontName 字体类型
     * @param fontSize 字体大小
     * @param isBold 是否加粗
     * @param isAlign 是否对齐
     * @return
     */
    public static HSSFCellStyle setCellStyle(HSSFWorkbook wb, String fontName, int fontSize,
                                             boolean isBold, boolean isAlign, boolean fontColor) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) fontSize);
        font.setFontName(fontName);

        if (fontColor) { // 设置字体颜色
            font.setColor(IndexedColors.RED.getIndex());
        }
        if (isBold) {
            font.setBold(true);//粗体显示
        }
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直对齐

        if (isAlign) {
            style.setAlignment(HorizontalAlignment.CENTER); // 水平对齐
        }
        return style;
    }

    /**
     *  设置背景颜色填充
     * @param style
     * @return
     */
    public static HSSFCellStyle setFillForegroundColor(HSSFCellStyle style, short colors) {
        style.setFillForegroundColor(colors);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 设置标题列字段值
     * @param row
     * @param fieldColumnList
     */
    public static void setFieldColumnName(HSSFRow row, String[] fieldColumnList, HSSFCellStyle style) {
        HSSFCell cell = null;
        for (int i = 0; i < fieldColumnList.length; i++) {
            cell = row.createCell((short) i);
            cell.setCellValue(fieldColumnList[i]);
            cell.setCellStyle(style);
        }
    }

    /**
     * 设置合并单元格边框样式
     * @param region
     * @param sheet
     * @param workbook
     */
    private static void setRegionStyle(CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook, HSSFCellStyle style) {
        HSSFCellStyle headerStyle = setBorderCellStyle(workbook, style);
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                HSSFCell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                }
                cell.setCellStyle(headerStyle);
            }
        }
    }

    /**
     * 常用单元格边框格式
     *
     * 虚线HSSFCellStyle.BORDER_DOTTED
     * 实线HSSFCellStyle.BORDER_THIN
     */
    private static HSSFCellStyle setBorderCellStyle(HSSFWorkbook wb, HSSFCellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);// 下边框
        style.setBorderLeft(BorderStyle.THIN);// 左边框
        style.setBorderRight(BorderStyle.THIN);// 右边框
        style.setBorderTop(BorderStyle.THIN);// 上边框
        return style;
    }

}
