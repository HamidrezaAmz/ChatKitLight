package ir.vasl.chatkitlight.utils;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;


import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;

import java.io.File;

public class FileHelper {

    public static String getFileName(String uri) {
        String[] split = uri.split("/");
        if (split.length > 0)
            return split[split.length - 1];
        else return "";
    }

    public static boolean checkFileExistence(Context context, String fileName){
        return new File(context.getExternalFilesDir(null).toString() + "/" + fileName).exists();
    }

    public static DownloadRequest downloadFile(Context context, String url, DownloadStatusListenerV1 downloadListener){
        String fileName = FileHelper.getFileName(url);
        String dir = context.getExternalFilesDir(null).toString() + "/" + fileName ;
        return new DownloadRequest(Uri.parse(url))
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(Uri.parse(dir))
                .setPriority(DownloadRequest.Priority.HIGH)

                .setStatusListener(downloadListener);
    }
}
