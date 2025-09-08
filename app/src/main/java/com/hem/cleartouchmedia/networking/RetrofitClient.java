package com.hem.cleartouchmedia.networking;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hem.cleartouchmedia.utilities.ApplicationConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LENOVO on 24-08-2023.
 */

public class RetrofitClient
{
//    public static final String BASEURL = "https://demo.cleartouchmedia.com/api/";
//    public static final String BASEURL = "https://dev.cleartouchmedia.com/api/";
    public static Retrofit retrofit;
    public static final String BASEURLWEATHER = "https://api.openweathermap.org/data/2.5/";
    public static Retrofit retrofit_weather;

    public static Retrofit getClient()
    {
        if(retrofit == null)
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(false)
                    .readTimeout(1000, TimeUnit.MINUTES)
                    .connectTimeout(1000, TimeUnit.MINUTES);
            httpClient.addInterceptor(new Interceptor()
            {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException
                {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type","application/json; charset=utf-8")
                            .header("Authorization", "d4e48_55693_c19e557d192c74cab8fbeb5aea4aec6d")  //token.getTokenType() + " " + token.getAccessToken()
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    Log.d("MMS", "OKHttpClieny request: >> " + request);

                    return chain.proceed(request);
                }
            });
          //  OkHttpClient client = httpClient.build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            /**
             * Create an instance of Retrofit object
             * */
            retrofit= new Retrofit.Builder()
                    .baseUrl(ApplicationConstants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit;
        }
        else
        {
            return  retrofit;
        }
    }

    public static Retrofit getWeatherClient()
    {
        if(retrofit_weather == null)
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(false)
                    .readTimeout(1000, TimeUnit.MINUTES)
                    .connectTimeout(1000, TimeUnit.MINUTES);
            httpClient.addInterceptor(new Interceptor()
            {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException
                {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type","application/json; charset=utf-8")
                            .header("Authorization", "d4e48_55693_c19e557d192c74cab8fbeb5aea4aec6d")//token.getTokenType() + " " + token.getAccessToken()
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    Log.d("MMS", "OKHttpClieny request: >> " + request);

                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();

            /**
             * Create an instance of Retrofit object
             * */
            retrofit_weather= new Retrofit.Builder()
                    .baseUrl(BASEURLWEATHER)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit_weather;
        }
        else
        {
            return  retrofit_weather;
        }
    }
}

