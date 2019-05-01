package com.yjj.tesorflow.demo.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

/**
 * created by yangjianjun on 2019/5/1
 * 权限管理工具
 */
public class PermissionUtils {
    /**
     * 返回系统是否给了权限组的权限
     *
     * @return false 表示真的是没有权限，true 表示可能有也可能没有
     */
    public static boolean hasPermission(Context context, String[] permissions) {
        try {
            for (String permission : permissions) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                } else {
                    if (context.getPackageManager().checkPermission(permission, context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }

                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

                if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        } catch (Throwable ignored) {}
        return true;
    }
}
