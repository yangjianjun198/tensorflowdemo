package com.yjj.tesorflow.demo.utils;

import android.app.Application;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yjj.tesorflow.demo.Constants;

import java.io.File;

/**
 * created by yangjianjun on 2019/5/1
 * model加载器
 */
public class TensorModelLoader {

    /**
     * 下载model
     */
    public void load(Application context) {
        File file = FileUtils.getModelPath(context, Constants.MODEL_URL);
        if (new File(file, Constants.INDEX_NAME).exists()) {
            return;
        }
        ExecutorUtils.execute(() -> {
            String downloadPath = FileUtils.getDownloadFile(context, Constants.MODEL_URL).getAbsolutePath();
            BaseDownloadTask singleTask = FileDownloader.getImpl()
                .create(Constants.MODEL_URL)
                .setPath(downloadPath)
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        File file = new File(downloadPath);
                        String path = file.getAbsolutePath();
                        ModelUnzipUtils.unZip(path,
                            FileUtils.getModelPath(context, Constants.MODEL_URL).getAbsolutePath());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

            singleTask.start();
        });
    }
}