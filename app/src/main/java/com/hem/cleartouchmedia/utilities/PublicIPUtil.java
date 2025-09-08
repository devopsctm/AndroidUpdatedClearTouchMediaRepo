package com.hem.cleartouchmedia.utilities;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PublicIPUtil {
    public interface PublicIpCallback {
        void onIpReceived(String ip);
        void onError(String error);
    }

    public static void getPublicIp(final PublicIpCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String ip = null;
                try {
                    URL url = new URL("https://api.ipify.org");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    ip = in.readLine();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return ip;
            }
            @Override
            protected void onPostExecute(String ip) {
                if (ip != null) {
                    callback.onIpReceived(ip);
                } else {
                    callback.onError("Failed to fetch IP address");
                }
            }
        }.execute();
    }
}
