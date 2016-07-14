package com.example.zyt.uploadtest.network;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/6/3.
 */
public interface FileuploadService {
    /**
     * 通过 List<MultipartBody.Part> 传入多个part实现多文件上传
     * @param parts 每个part代表一个
     * @return 状态信息
     */
    @Multipart
    @POST("users/image")
    Call<BaseResponse<String>> uploadFilesWithParts(@Part() List<MultipartBody.Part> parts);



    /**
     * 通过 MultipartBody和@body作为参数来上传
     * @param multipartBody MultipartBody包含多个Part
     * @return 状态信息
     */
    @POST("WebServer/upload")
    Call<BaseResponse<String>> uploadFileWithRequestBody(@Body MultipartBody multipartBody);



    /**
     * 通过 MultipartBody和@body作为参数来上传
     * @param multipartBody MultipartBody包含多个Part
     * @return 状态信息
     */
    @POST("WebServer/upload")
    Call<BaseResponse<String>> uploadFileWithRequestBody(@Body MultipartBody multipartBody, @Query("user") String user);



}
