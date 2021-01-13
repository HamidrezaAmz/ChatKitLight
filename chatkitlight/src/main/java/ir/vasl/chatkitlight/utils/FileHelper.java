package ir.vasl.chatkitlight.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class FileHelper {

//    public static String getFileName(String uri) {
//        String res = uri;
//        if (uri == null)
//            return "";
//        String[] split = uri.split("/");
//        if (split.length > 0)
//            res = split[split.length - 1];
//        return res.length() < 17 ? res : res.substring(res.length() - 16);
//    }

    public static boolean checkFileExistence(Context context, String fileName) {
        if (context.getExternalFilesDir(null) == null || fileName == null)
            return false;
        return new File(Objects.requireNonNull(context.getExternalFilesDir(null)).toString() + "/chatkit/", fileName).exists();
    }

    public static DownloadRequest downloadFile(Context context, String url, String fileName, DownloadStatusListenerV1 downloadListener) {
        if (context.getExternalFilesDir(null) == null)
            return null;
//        String fileName = FileHelper.getFileName(url);
        String dir = Objects.requireNonNull(context.getExternalFilesDir(null)).toString() + "/chatkit/" + fileName;
        return new DownloadRequest(Uri.parse(url))
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(Uri.parse(dir))
                .setPriority(DownloadRequest.Priority.HIGH)
                .setStatusListener(downloadListener);
    }

    public static Uri getFileUri(Context context, String fileName) {
        if (context.getExternalFilesDir(null) == null || fileName == null)
            return null;
        return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",
                new File(Objects.requireNonNull(context.getExternalFilesDir(null)).toString() + "/chatkit/", fileName));
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

    public static void openFile(Context context, String fileAddress, String fileName) {
        if (context.getExternalFilesDir(null) == null)
            return;
        if(!checkFileExistence(context, fileName))
            return;
//        String fileName = getFileName(fileAddress);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setDataAndType(
                FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",
                        new File(Objects.requireNonNull(context.getExternalFilesDir(null)).toString() + "/chatkit/", fileName)),
                getMimeType(context, Uri.parse(fileAddress)));
        viewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent chooserIntent = Intent.createChooser(viewIntent, Constants.CHOOSE);
        context.startActivity(chooserIntent);
    }

    public static void openUrl(Context context, String url){
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
//        context.startActivity(i);
    }

    public static byte[] getFileBytes(Context context, String fileName) {
        if (context.getExternalFilesDir(null) == null || fileName == null || fileName.length() == 0)
            return new byte[]{};
        byte[] result = new byte[((int) Runtime.getRuntime().freeMemory())];
//        String fileName = getFileName(fileAddress);
        File file = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).toString() + "/chatkit/", fileName);
        FileInputStream fis = null;
        int all = 0;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = fis.read(buffer)) != -1) {
                all += read;
                System.arraycopy(buffer, 0, result, all, buffer.length);
            }
        } catch (Exception e) {
            Log.e("tag", "getFileBytes: " + e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ignored) {
            }
        }
        return Arrays.copyOfRange(result, 0, all - 1);
    }
}
