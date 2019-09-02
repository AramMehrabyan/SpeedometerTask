package com.example.speedometertask.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public abstract class PermissionUtils {

    public static boolean checkSelfPermissionIsGranted(Context context, String[] permissions) {
        boolean isPermissionsGranted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                isPermissionsGranted = false;
                break;
            }
        }
        return isPermissionsGranted;
    }

    public static void requestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
