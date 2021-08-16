package ir.vasl.chatkitlight.utils;

import android.net.Uri;

public class ExtensionHelper {

    public static String getUriExtension(Uri uri) {
        String stringBaseUri = uri.toString();
        return stringBaseUri.substring(stringBaseUri.lastIndexOf("."));
    }

}