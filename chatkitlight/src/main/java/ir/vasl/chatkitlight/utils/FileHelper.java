package ir.vasl.chatkitlight.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    public static String getExistsFilePath(Context context, String fileName) {
        if (context.getExternalFilesDir(null) == null || fileName == null)
            return "";
        return new File(Objects.requireNonNull(context.getExternalFilesDir(null)).toString() + "/chatkit/", fileName).getPath();
    }

    public static boolean checkFileExistence(Context context, String fileName) {
        if (context.getExternalFilesDir(null) == null || fileName == null)
            return false;
        boolean res = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).toString() + "/chatkit/", fileName).exists();
        Log.e("TAG", "downloading: " + res );
        return res;
    }

    public static DownloadRequest downloadFile(Context context, String url, String fileName, DownloadStatusListenerV1 downloadListener) {
        if (context.getExternalFilesDir(null) == null)
            return null;
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
        if (!checkFileExistence(context, fileName))
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

    public static void openUrl(Context context, String url) {
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
//        context.startActivity(i);
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "image");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static byte[] getFileBytes(Context context, String fileName) {
        byte[] difa = new byte[]{0, 5, 10, 15, 20, 30, 40, 50, 100, 0, 5, 10, 15, 20, 30, 40, 50, 100, 0, 5, 10, 15, 20, 30, 40, 50, 100};
        if (context.getExternalFilesDir(null) == null || fileName == null || fileName.length() == 0)
            return difa;
        File file = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).toString() + "/chatkit/", fileName);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
            return difa;
        }
        return bytes;
    }
}
