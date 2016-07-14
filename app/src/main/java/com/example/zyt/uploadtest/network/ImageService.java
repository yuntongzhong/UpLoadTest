package com.example.zyt.uploadtest.network;

import com.example.zyt.uploadtest.entity.ImageInfo;
import com.example.zyt.uploadtest.entity.Str;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/7.
 */
public interface ImageService {
    @GET("WebServer/image")
    Observable<List<ImageInfo>> getImgInfo(@Query("user")String user);

    /**
     * 通过 MultipartBody和@body作为参数来上传
     * @param multipartBody MultipartBody包含多个Part
     * @return 状态信息
     */
    @POST("WebServer/upload")
    Observable<Str> uploadFileWithRequestBody(@Body MultipartBody multipartBody, @Query("user") String user);
}
