package com.yjj.tesorflow.demo.utils;

import com.yjj.tesorflow.demo.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * created by yangjianjun on 2019/5/1
 * 模型下载文件解压工具
 */
public class ModelUnzipUtils {

    /**
     * 解压model到指定位置
     */
    public static void unZip(final String zipFilePath, final String targetDirPath) {
        ExecutorUtils.execute(() -> {
            ZipFile zipFile = null;
            InputStream entryIn = null;
            FileOutputStream entryOut = null;
            try {
                zipFile = new ZipFile(zipFilePath);
                Enumeration enumeration = zipFile.entries();
                File path = new File(targetDirPath);
                boolean isSuccess = true;
                if (path.exists() && path.isDirectory()) {
                    isSuccess = FileUtils.deleteDir(path);
                } else if (path.exists()) {
                    isSuccess = FileUtils.deleteFile(path.getAbsolutePath());
                }

                if (isSuccess) {
                    isSuccess = path.mkdirs();
                }
                if (!isSuccess) {
                    return;
                }
                while (enumeration.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
                    if (zipEntry.isDirectory() || zipEntry.getName().contains("../")) {
                        continue;
                    }
                    String fileName = zipEntry.getName();
                    /***
                     * lite name
                     * */
                    if (!(fileName.endsWith(Constants.MODEL_FILE_NAME_POSTFIX) || fileName.endsWith(
                        Constants.TXT_FILE_NAME_POSTFIX))) {
                        continue;
                    }
                    File f = new File(targetDirPath, fileName);
                    boolean successed = true;
                    if (f.exists()) {
                        successed = FileUtils.deleteFile(f.getAbsolutePath());
                    }
                    if (successed) {
                        successed = f.createNewFile();
                    }
                    if (!successed) {
                        continue;
                    }
                    entryIn = zipFile.getInputStream(zipEntry);
                    entryOut = new FileOutputStream(f);
                    int c;
                    byte[] by = new byte[1024];
                    while ((c = entryIn.read(by)) != -1) {
                        entryOut.write(by, 0, c);
                    }
                    entryOut.flush();
                    entryOut.close();
                    entryIn.close();
                    entryOut = null;
                    entryIn = null;
                }
                /**
                 * 打标记 已经写入完毕
                 */
                File file = new File(path, Constants.INDEX_NAME);
                if (!file.exists()) {
                    file.createNewFile();
                }
                ModelLoadListenerManager.getInstance().notifyFinished();
            } catch (Exception ignore) {
            }
        });
    }
}