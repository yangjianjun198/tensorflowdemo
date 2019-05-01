package com.yjj.tesorflow.demo.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by yangjianjun on 2019/5/1
 * 线程工具
 */
public class ExecutorUtils {
    private static ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

    public static void execute(Runnable runnable) {
        singleExecutor.execute(runnable);
    }
}
