package com.project.utils.common;

import com.project.utils.common.base.enums.FileSeparator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * io及文件上传解压相关工具类
 *
 * @author zhao
 */
public class FileUtil {

    private static final Logger logger = LogManager.getLogger(FileUtil.class);

    /**
     * 创建文件夹
     *
     * @param path
     */
    public static boolean createFile(String path) {

        // 判断是否以文件分隔符结尾
        if (!path.endsWith(FileSeparator.FILESEPARATOR)) {

            // 添加文件分隔符
            path = path + FileSeparator.FILESEPARATOR;

        }

        File dir = new File(path);

        // 判断目录是否存在
        if (dir.exists()) {

            return false;
        }

        // 创建文件夹
        if (dir.mkdirs()) {

            return true;
        }

        return false;
    }

    /**
     * 删除目录 此处采用迭代删除
     *
     * @param path
     */
    public static void deleteDir(String path) {
        File fileDel = new File(path);
        if (fileDel.exists() && fileDel != null && fileDel.isDirectory()) {
            File[] files = fileDel.listFiles();
            for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
                if (files[i].isDirectory()) {
                    deleteDir(path + File.separator + files[i].getName());
                }
                files[i].delete();// 把每个文件用这个方法进行迭代
            }
        } else if (fileDel.isFile()) {
            fileDel.delete();
        }
    }

    /**
     * <p>
     * Title: getFileNameNoEx
     * </p>
     *
     * <p>
     * Description: 获取文件名
     * </p>
     *
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * <p>
     * Title: getExtensionName
     * </p>
     *
     * <p>
     * Description: 获取文件扩展名
     * </p>
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 解压zip文件
     *
     * @param zipFile
     * @param unPath  解压的路径
     */
    public static void unZip(File zipFile, String unPath) throws IOException {

        // 判断乱码
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));

        File pathFile = new File(unPath);

        // 若路径不存在
        if (!pathFile.exists()) {

            // 生成路径
            pathFile.mkdirs();
        }
        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {

            ZipEntry entry = entries.nextElement();

            // 获取压缩包名称
            String zipEntryName = entry.getName();

            InputStream inputStream = zip.getInputStream(entry);

            String outPath = (unPath + FileSeparator.FILESEPARATOR + zipEntryName).replaceAll("\\*",
                    FileSeparator.FILESEPARATOR);

            File file = new File(outPath.substring(0, outPath.lastIndexOf(FileSeparator.FILESEPARATOR)));

            if (!file.exists()) {

                file.mkdirs();
            }
            if (new File(outPath).isDirectory()) {

                continue;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(outPath);

            byte[] buf = new byte[1024];

            int length;

            while ((length = inputStream.read(buf)) > 0) {

                // 流输出
                fileOutputStream.write(buf, 0, length);
            }

            // 关闭输入流
            inputStream.close();

            // 关闭输出流
            fileOutputStream.close();

        }

        zip.close();
        // 删除压缩包
        zipFile.delete();

    }

    /**
     * 文件上传
     *
     * @param file
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean uploadFile(MultipartFile file, String path, String fileName) throws IOException {

        File uploadFile = null;
        if (!file.isEmpty()) {

            uploadFile = new File(path + File.separator + fileName);
            if (!uploadFile.exists()) {
                createFile(path);
            }
            FileOutputStream fileOutputStream = new FileOutputStream(uploadFile);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            bufferedOutputStream.write(file.getBytes());

            bufferedOutputStream.flush();

            fileOutputStream.close();

            bufferedOutputStream.close();

            return true;
        } else {
            return false;
        }

    }

    /**
     * 文件及文件夹重命名
     *
     * @param nativePath
     * @param newPath
     * @return
     */
    public static boolean renameDir(String nativePath, String newPath) {

        boolean result = new File(nativePath).renameTo(new File(newPath));

        return result;

    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            logger.warn("删除文件失败:{}不存在!", fileName);
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.info("删除单个文件:{}成功!", fileName);
                return true;
            } else {
                logger.warn("删除单个文件:{}失败!", fileName);
                return false;
            }
        } else {
            logger.warn("删除单个文件失败:{}不存在!", fileName);
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件(包含目录)
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            logger.warn("删除目录失败:{}不存在!", dir);
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            logger.warn("删除目录失败!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            logger.warn("删除目录:{}成功!", dir);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 递归方式 计算文件的大小
     *
     * @param file 文件
     * @return
     */
    public static long getTotalSizeOfFilesInDir(final File file) {
        if (file.exists()) {
            if (file.isFile()) {
                return file.length();
            }
            final File[] children = file.listFiles();
            long total = 0;
            if (children != null) {
                for (final File child : children) {
                    total += getTotalSizeOfFilesInDir(child);
                }
            }
            return total;
        }
        return 0L;
    }

    /**
     * 压缩成ZIP 方法
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException {

        try {
            ZipOutputStream zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            if (zos != null) {
                zos.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("zip error from FileUtil toZip", e);
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
            throws Exception {
        byte[] buf = new byte[2 * 1024];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }
}
