package com.project.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

@SuppressWarnings("all")
public class ImageUtil {

    /**
     * 按固定长宽进行缩放
     * @param is      输入流
     * @param os      输出流
     * @param width   指定长度
     * @param height  指定宽度
     * @throws Exception
     */
    public static void zoomImage(String url, OutputStream os, int width, int height) throws Exception {
        //读取图片
        BufferedImage bufImg = ImageIO.read(new URL(url));
        //获取缩放比例
        double wRatio = width * 1.0/ bufImg.getWidth();
        double hRatio = height * 1.0 / bufImg.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wRatio, hRatio), null);
        BufferedImage bufferedImage = ato.filter(bufImg, null);
        //写入缩减后的图片
        ImageIO.write(bufferedImage, "jpg", os);
    }

    /**
     * 按固定文件大小进行缩放
     * @param is     输入流
     * @param os     输出流
     * @param size   文件大小指定
     * @throws Exception
     */
    public static void zoomImage(InputStream is, OutputStream os, Integer size) throws Exception {
        /*FileInputStream的available()方法返回的是int类型，当数据大于1.99G(2147483647字节)后将无法计量，
            故求取流文件大小最好的方式是使用FileChannel的size()方法，其求取结果与File的length()方法的结果一致
            参考：http://blog.csdn.net/chaijunkun/article/details/22387305*/
        int fileSize = is.available();
        //文件大于size时，才进行缩放。注意：size以K为单位
        if(fileSize < size * 1024){
            return;
        }
        // 获取长*宽(面积)缩放比例
        double sizeRate = (size * 1024 * 0.5) / fileSize;
        // 获取长和宽分别的缩放比例，即面积缩放比例的2次方根
        double sideRate = Math.sqrt(sizeRate);
        BufferedImage bufImg = ImageIO.read(is);

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(sideRate, sideRate), null);
        BufferedImage bufferedImage = ato.filter(bufImg, null);
        ImageIO.write(bufferedImage, "jpg", os);
    }

    /**
     * 等比例缩放，以宽或高较大者达到指定长度为准
     * @param src      输入文件路径
     * @param dest     输出文件路径
     * @param width    指定宽
     * @param height   指定高
     */
    public static void zoomTo400(String src, String dest, Integer width, Integer height){
        try {
            File srcFile = new File(src);
            File destFile = new File(dest);
            BufferedImage bufImg = ImageIO.read(srcFile);
            int w0 = bufImg.getWidth();
            int h0 = bufImg.getHeight();
            // 获取较大的一个缩放比率作为整体缩放比率
            double wRatio = 1.0 * width / w0;
            double hRatio = 1.0 * height / h0;
            double ratio = Math.min(wRatio, hRatio);
            // 缩放
            AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            BufferedImage bufferedImage = ato.filter(bufImg, null);
            // 输出
            ImageIO.write(bufferedImage, dest.substring(dest.lastIndexOf(".")+1), destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 等比例图片压缩，以宽或高较大者达到指定长度为准
     * @param is     输入流
     * @param os     输出流
     * @param width  宽
     * @param height 高
     * @throws IOException
     */
    public static void changeSize(File file, OutputStream os, String type, int width, int height) throws IOException {
        BufferedImage bis = ImageIO.read(file);
//        BufferedImage bis = ImageIO.read(new URL(url));

        int srcWidth = bis.getWidth(null);   // 得到源图宽
        int srcHeight = bis.getHeight(null); // 得到源图高

        if (width <= 0 || width > srcWidth) {
            width = bis.getWidth();
        }
        if (height <= 0 || height > srcHeight) {
            height = bis.getHeight();
        }
        // 若宽高小于指定最大值，不需重新绘制
        if (srcWidth <= width && srcHeight <= height) {
            ImageIO.write(bis, type, os);
            os.close();
        } else {
            double scale =
                    ((double) width / srcWidth) > ((double) height / srcHeight) ?
                            ((double) height / srcHeight)
                            : ((double) width / srcWidth);
            width = (int) (srcWidth * scale);
            height = (int) (srcHeight * scale);

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(bis, 0, 0, width, height, Color.WHITE, null); // 绘制缩小后的图
            ImageIO.write(bufferedImage, type, os);
            os.close();
        }
    }

    /**
     * 将file文件转为字节数组
     * @param file
     * @return
     */
    public static byte[] getByte(File file){
        byte[] bytes = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将字节流写到指定文件
     * @param os
     * @param file
     */
    public static void writeFile(ByteArrayOutputStream os, File file){
        FileOutputStream fos = null;
        try {
            byte[] bytes = os.toByteArray();
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
