package com.dvinfosys.smokeeffectphotomaker.Utils;
/*
    Person : Dhaval Thakor
    Email : dhaval@vakratundasystem.in
    File Name : RetrofitClient.java
    Description :
      - Initialization of Retrofit and OkHttp Client
 */

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(150, TimeUnit.SECONDS)
            .readTimeout(150, TimeUnit.SECONDS)
            .writeTimeout(150, TimeUnit.SECONDS)
            .build();

    public static Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

}
