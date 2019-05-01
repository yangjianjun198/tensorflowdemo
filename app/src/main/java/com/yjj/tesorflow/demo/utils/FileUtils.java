package com.yjj.tesorflow.demo.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.yjj.tesorflow.demo.Constants;

import java.io.File;

/**
 * created by yangjianjun on 2019/5/1
 * 文件工具
 */
public class FileUtils {

    /**
     * 获取缓存目录
     */
    public static File getFileDirectory(Context context) {
        File cacheDir = getFileDirectory(context, true);
        return new File(cacheDir, Constants.INDIVIDUAL_DIR_NAME);
    }

    public static File getModelPath(Context context, String url) {
        File path = getFileDirectory(context);
        return new File(path, Md5Utils.getMD5(url));
    }

    public static File getDownloadFileRoot(Context context) {
        File cacheDir = getFileDirectory(context, true);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        File dir = new File(cacheDir, Constants.DOWNLOAD_DIR_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 删除某个目录及目录下的所有子目录和文件
     *
     * @param dir File path
     * @return boolean
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String aChildren : children) {
                    boolean isDelete = deleteDir(new File(dir, aChildren));
                    if (!isDelete) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    public static boolean deleteFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                return deleteFileSafely(file);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static boolean deleteFileSafely(File file) {
        return file != null && file.exists() && file.delete();
    }

    public static File getDownloadFile(Context context, String url) {
        File root = getDownloadFileRoot(context);
        File path = new File(root, Md5Utils.getMD5(url) + ".zip");
        return path;
    }

    /**
     * 获取缓存目录
     */
    public static File getFileDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;
        if (preferExternal && isExternalStorageMounted()) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getFilesDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/file/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "file");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
        }
        return appCacheDir;
    }

    public static boolean isExternalStorageMounted() {
        return Environment.MEDIA_MOUNTED.equalsIgnoreCase(getExternalStorageState());
    }

    /**
     * 目前已发现 API 22 跟少量 API 21，基本都是 Oppo 和 Vivo 的手机，会抛出 NPE
     *
     * @return Environment 的几个常量，或者捕获空指针异常时返回空字符串
     */
    public static String getExternalStorageState() {
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { // (sh)it happens
            externalStorageState = "";
        }
        return externalStorageState;
    }
}
