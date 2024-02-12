package ir.vasl.chatkitlight.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import androidx.core.content.ContextCompat;

public class PermissionHelper {

    public static boolean checkStoragePermission(Context context) {
        Log.e("tag",
                "checkStoragePermission: ");
        if (Build.VERSION.SDK_INT >= 33) {
            return ContextCompat.checkSelfPermission(context,
                    "android.permission.READ_MEDIA_IMAGES") == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context,
                            "android.permission.READ_MEDIA_VIDEO") == PackageManager.PERMISSION_GRANTED;
        }
        else{
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }
}
