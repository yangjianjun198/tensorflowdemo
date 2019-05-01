package com.yjj.tesorflow.demo;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.yjj.tesorflow.demo.utils.TensorModelLoader;

/**
 * created by yangjianjun on 2019/5/1
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.init(this);
        new TensorModelLoader().load(this);
    }
}
