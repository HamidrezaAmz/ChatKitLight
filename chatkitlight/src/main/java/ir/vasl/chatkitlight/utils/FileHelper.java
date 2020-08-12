package ir.vasl.chatkitlight.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;


import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHelper {

    public static String getFileName(String uri) {
        String[] split = uri.split("/");
        if (split.length > 0)
            return split[split.length - 1];
        else return "";
    }

    public static boolean checkFileExistence(Context context, String fileName){
        return new File(context.getExternalFilesDir(null).toString() + "/chatkit/", fileName).exists();
    }

    public static DownloadRequest downloadFile(Context context, String url, DownloadStatusListenerV1 downloadListener){
        String fileName = FileHelper.getFileName(url);
        String dir = context.getExternalFilesDir(null).toString() + "/chatkit/" + fileName ;
        return new DownloadRequest(Uri.parse(url))
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(Uri.parse(dir))
                .setPriority(DownloadRequest.Priority.HIGH)
                .setStatusListener(downloadListener);
    }

    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public static void openFile(Context context, String fileAddress) {
        String fileName = getFileName(fileAddress);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setDataAndType(
                FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider" ,
                        new File(context.getExternalFilesDir(null).toString() + "/chatkit/", fileName)),
                getMimeType(context, Uri.parse(fileAddress)));
        viewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent chooserIntent = Intent.createChooser(viewIntent, "انتخاب کنید");
        context.startActivity(chooserIntent);
    }

    public static byte[] getFileBytes(Context context, String fileAddress){
        byte[] result = new byte[Integer.MAX_VALUE];
        String fileName = getFileName(fileAddress);
        File file = new File(context.getExternalFilesDir(null).toString() + "/chatkit/", fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            byte buffer[] = new byte[4096];
            int read = 0;
            int all = 0;

            while((read = fis.read(buffer)) != -1) {
                all += read;
                System.arraycopy(buffer, 0, result, all, buffer.length);
            }
        } catch (Exception e) {
            System.out.println("File not found: " + e.toString());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ignored) {
            }
        }
        return result;
    }
}
