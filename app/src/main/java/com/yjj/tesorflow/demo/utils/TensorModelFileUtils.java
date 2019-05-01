package com.yjj.tesorflow.demo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.yjj.tesorflow.demo.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * created by yangjianjun on 2019/5/1
 * tensorflow model file 工具类
 */
public class TensorModelFileUtils {
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    /** Memory-map the model file in Assets. */
    public static void loadModelFile(final Context context, final LoadCallback callback) {
        ExecutorUtils.execute(() -> {
            File dir = FileUtils.getModelPath(context, Constants.MODEL_URL);
            if (dir == null) {
                performLoadFailure(callback);
                return;
            }
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!new File(dir, Constants.INDEX_NAME).exists()) {
                performLoadFailure(callback);
                return;
            }
            String[] list = dir.list();
            if (list == null) {
                performLoadFailure(callback);
                return;
            }
            String di = null;
            for (String p : list) {
                if (p.endsWith(Constants.MODEL_FILE_NAME_POSTFIX)) {
                    di = p;
                    break;
                }
            }
            if (di == null) {
                performLoadFailure(callback);
                return;
            }
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(new File(dir, di));
            } catch (Exception e) {
                Log.d("TensorModelFileUtils", e.getMessage());
            }
            if (inputStream == null) {
                performLoadFailure(callback);
                return;
            }
            FileChannel input = inputStream.getChannel();
            if (input == null) {
                performLoadFailure(callback);
                return;
            }
            MappedByteBuffer resultMBB = null;
            try {
                resultMBB = input.map(FileChannel.MapMode.READ_ONLY, input.position(), input.size());
            } catch (Exception ignore) {

            }
            try {
                input.close();
            } catch (IOException e) {
            }
            String txt = null;
            for (String p : list) {
                if (p.endsWith(Constants.TXT_FILE_NAME_POSTFIX)) {
                    txt = p;
                    break;
                }
            }
            if (txt == null) {
                performLoadFailure(callback);
                return;
            }
            List<String> labels = loadLabel(new File(dir, txt).getAbsolutePath());
            final MappedByteBuffer finalResultBB = resultMBB;
            mainHandler.post(() -> {
                if (callback != null) {
                    callback.onLoad(finalResultBB, labels);
                }
            });
        });
    }

    private static void performLoadFailure(LoadCallback callback) {

        mainHandler.post(() -> {
            if (callback != null) {
                callback.onLoad(null, null);
            }
        });
    }

    public static interface LoadCallback {
        public void onLoad(MappedByteBuffer outBB, List<String> label);
    }

    private static List<String> loadLabel(String path) {
        BufferedReader br = null;
        List<String> labels = new ArrayList<>(16);
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            String line;
            while ((line = br.readLine()) != null) {
                labels.add(line);
            }
            br.close();
        } catch (IOException e) {
        }
        return labels;
    }
}
