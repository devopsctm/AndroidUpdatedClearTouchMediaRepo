package com.hem.cleartouchmedia.utilities;

import android.content.Context;

import java.io.File;

public class ImageUtil {
    public static boolean isImageDownloaded(Context context, String folderName, String fileName) {
        File file = new File(context.getExternalFilesDir(folderName), fileName);
        return file.exists();
    }
}
