package com.hem.cleartouchmedia.download;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageDownloadWorker extends Worker {

    public ImageDownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        String videoUrl = getInputData().getString("imageUrl");
        String fileName = getInputData().getString("fileName");
        String folderName = getInputData().getString("folderName");

        if (videoUrl == null || fileName == null || folderName == null) return Result.failure();

        File directory = new File(getApplicationContext().getExternalFilesDir(null), folderName);
        if (!directory.exists() && !directory.mkdirs()) return Result.failure();

        File outputFile = new File(directory, fileName);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.MINUTES)  // long read timeout for big files
                .writeTimeout(5, TimeUnit.MINUTES) // long write timeout
                .retryOnConnectionFailure(true)
                .build();

        Request request = new Request.Builder().url(videoUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return Result.failure();

            // Stream response directly to file
            try (InputStream inputStream = response.body().byteStream();
                 OutputStream outputStream = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[8192]; // 8KB buffer
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }

            return outputFile.exists() ? Result.success() : Result.failure();

        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}
