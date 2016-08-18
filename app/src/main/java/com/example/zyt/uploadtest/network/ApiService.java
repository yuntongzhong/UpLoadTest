package com.example.zyt.uploadtest.network;

import com.example.zyt.uploadtest.entity.BaseModel;
import com.example.zyt.uploadtest.entity.Result;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/7.
 */
public interface ApiService {
    @POST("WebServer/Login")
    Observable<BaseModel<String>> login(@Query("username") String userName, @Query("password") String password);

    @POST("WebServer/Register")
    Observable<BaseModel<String>> register(@Query("username") String userName, @Query("password") String password, @Query("email") String email, @Query("phone") String tel, @Query("sex") String sex);

    @GET("WebServer/image")
    Observable<BaseModel<String>> getImgInfo(@Query("user") String user);

    /**
     * 通过 MultipartBody和@body作为参数来上传
     *
     * @param multipartBody MultipartBody包含多个Part
     * @return 状态信息
     */
//    @POST("WebServer/upload")
//    Observable<Result> uploadFileWithRequestBody(@Body MultipartBody multipartBody, @Query("user") String user);
    @POST("WebServer/upload")
    Observable<BaseModel<String>> uploadFileWithRequestBody(@Body MultipartBody multipartBody, @Query("user") String user);
}
