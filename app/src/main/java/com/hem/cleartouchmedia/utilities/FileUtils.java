package com.hem.cleartouchmedia.utilities;

import android.util.Log;

import java.io.File;

public class FileUtils {
    public static boolean deleteDirectory(File directory) {
        Log.d("CMT", "<<<<<<<<<<<<<<<<files directory ");
        if (directory != null && directory.isDirectory()) {
            File[] files = directory.listFiles();
            Log.d("CMT", "<<<<<<<<<<<<<<<<files length: " + files.length);
            if (files != null) {
                for (File file : files) {
                    Log.e("CTM", "<<<<<<<<<<<<<<<<file name: " + file.getAbsolutePath());
                    boolean deletedsss = file.delete();
                    Log.e("CTM", "<<<<<<<<<<<<<<<<file file.getName(): " + file.getName());
                    Log.e("CTM", "<<<<<<<<<<<<<<<<file deleted: " + deletedsss);
                    if (file.isDirectory()) {
                        deleteDirectory(file); // Recursive call to delete subdirectories
                    } else {
                        boolean deleted = file.delete();
                        if (!deleted) {
                            Log.e("CTM", "Failed to delete file: " + file.getAbsolutePath());
                        }
                    }
                }
            }
            boolean deleted = directory.delete();
            if (!deleted) {
                Log.e("CTM", "Failed to delete directory: " + directory.getAbsolutePath());
            }
        } else if (directory != null && directory.isFile()) {
            boolean deleted = directory.delete();
            if (!deleted) {
                Log.e("CTM", "Failed to delete file: " + directory.getAbsolutePath());
            }
        }
        /*if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return false;
        }

        File[] files = directory.listFiles();
        Log.d("CTM", "<<<<<<<<<<<files length: "+files.length);
        Log.d("CTM", "<<<<<<<<<<<files directory: "+directory);
        if (files != null) {
            for (File file : files) {
                file.delete();
                *//*if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }*//*
            }
        }*/
        return directory.delete();
    }
}
