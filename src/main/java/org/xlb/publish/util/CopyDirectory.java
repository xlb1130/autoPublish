package org.xlb.publish.util;

/**
 * @Auther: Jack Xie
 * @Date: 2018/10/29/029 17:49
 * @Description:
 */
import java.io.*;

/**
 * 复制文件夹或文件夹
 */
public class CopyDirectory {
    // 源文件夹
    String url1 = "E:\\workspace\\DeployeTest";
    // 目标文件夹
    String url2 = "E:\\java\\project\\bin";

    public static void main(String args[]) throws IOException {
        CopyDirectory copyDirectory = new CopyDirectory();
        // 创建目标文件夹
        (new File(copyDirectory.url2)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(copyDirectory.url1)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 复制文件
                copyDirectory.copyFile(file[i], new File(copyDirectory.url2 + file[i].getName()));
            }
            if (file[i].isDirectory()) {
                // 复制目录
                String sourceDir = copyDirectory.url1 + File.separator + file[i].getName();
                String targetDir = copyDirectory.url2 + File.separator + file[i].getName();
                copyDirectory.copyDirectiory(sourceDir, targetDir);
            }
        }
    }

    /**
     * 复制文件
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @throws IOException
     */
    public void copyFile(File sourceFile, File targetFile)
            throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    /**
     * 复制文件夹
     *
     * @param sourceDir 源文件夹路径
     * @param targetDir 目标文件夹路径
     * @throws IOException
     */
    public void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir)
                        .getAbsolutePath()
                        + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }
}
