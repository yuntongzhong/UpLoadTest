package com.example.zyt.uploadtest.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/6/3.
 */
public class RetrofitBuilder {
    private static Retrofit retrofit;
    private static OkHttpClient client;
    private static ApiService imageService;

    public synchronized static ApiService getApiService() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

            retrofit = new Retrofit.Builder().client(getHttpClient())
                    .baseUrl("http://yuntongweb.com/")
                    .addConverterFactory(gsonConverterFactory)
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            imageService=retrofit.create(ApiService.class);
        }
        return imageService;
    }

    public static OkHttpClient getHttpClient(){
        if(client==null){
            client = new OkHttpClient.Builder()
                    .build();
        }
        return client;
    }
}
