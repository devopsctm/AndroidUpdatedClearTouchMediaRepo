package com.hem.cleartouchmedia;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

public class GooglePlayStoreAppVersionNameLoader extends AsyncTask<String, Void, String> {

    String newVersion;

    @Override
    protected String doInBackground(String... urls) {
        try {
            return Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.hem.cleartouchmedia" + "&hl=en")
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String string) {
        if (string != null && !string.isEmpty()) {
            newVersion = string;
            Log.d("TAG", "<<<New Version: " + newVersion);
        } else {
            Log.e("TAG", "<<<New Version: Failed to fetch the latest version");
        }
    }
}