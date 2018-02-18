package com.vinsofts.adlibraries;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macOS on 2/12/18.
 */

public class RetrofitClient {

    private static ApiService retrofit;

    public static ApiService getRetrofit(String baseUrl) {
        if (retrofit == null) {
            retrofit = createRetrofit(baseUrl).create(ApiService.class);
        }
        return retrofit;
    }

    private static Retrofit createRetrofit(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());

        return builder.client(httpClientBuilder.build()).build();
    }

}
