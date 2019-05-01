package com.yjj.tesorflow.demo.utils;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * created by yangjianjun on 2019/5/1
 * 模型加载完毕通知管理器
 */
public class ModelLoadListenerManager {
    private static volatile ModelLoadListenerManager instance;
    private List<WeakReference<ModelLoadFinishListener>> listenerRefList;
    private ReferenceQueue watchQueue;

    public static interface ModelLoadFinishListener {
        void onFinish();
    }

    private ModelLoadListenerManager() {
    }

    public static ModelLoadListenerManager getInstance() {
        if (instance == null) {
            synchronized (ModelLoadListenerManager.class) {
                if (instance == null) {
                    instance = new ModelLoadListenerManager();
                }
            }
        }
        return instance;
    }

    public void add(ModelLoadFinishListener loadFinishListener) {
        if (listenerRefList == null) {
            listenerRefList = new ArrayList<>(2);
        }
        if (watchQueue == null) {
            watchQueue = new ReferenceQueue();
        }
        if (contain(listenerRefList, loadFinishListener) >= 0) {
            return;
        }
        listenerRefList.add(new WeakReference<>(loadFinishListener, watchQueue));
    }

    public void notifyFinished() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (listenerRefList == null) {
                return;
            }
            innerCleanReferenceQueue(watchQueue, listenerRefList);
            for (WeakReference<ModelLoadFinishListener> itemRef : listenerRefList) {
                if (itemRef.get() == null) {
                    continue;
                }
                itemRef.get().onFinish();
            }
        });
    }

    private void innerCleanReferenceQueue(ReferenceQueue referenceQueue, List list) {
        if (list == null || referenceQueue == null) {
            return;
        }
        Reference reference;
        while ((reference = referenceQueue.poll()) != null) {
            if (list.contains(reference)) {
                list.remove(reference);
            }
        }
    }

    private <T> int contain(List<WeakReference<T>> list, T obj) {
        int index = -1;
        for (WeakReference<T> item : list) {
            index++;
            if (item.get() != null && item.get() == obj) {
                return index;
            }
        }
        return -1;
    }
}
