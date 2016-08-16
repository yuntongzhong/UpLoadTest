package com.example.zyt.uploadtest.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/6/3.
 */
public class RetrofitBuilder {
    private static Retrofit retrofit;
    private static OkHttpClient client;

    public synchronized static Retrofit buildRetrofit() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

            retrofit = new Retrofit.Builder().client(getHttpClient())
//                    .baseUrl("http://192.168.1.85:8080/")
                    .baseUrl("http://yuntongweb.com/")
                    .addConverterFactory(gsonConverterFactory)
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public static OkHttpClient getHttpClient(){
        if(client==null){
            client = new OkHttpClient.Builder()
                    .build();
        }
        return client;
    }
}
