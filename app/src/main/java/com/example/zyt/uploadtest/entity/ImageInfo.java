package com.example.zyt.uploadtest.entity;

/**
 * Created by Administrator on 2016/6/7.
 */
public class ImageInfo {

    /**
     * id : 2
     * absolutePath : D:\WebApp\WebServer\resource\zhong\img
     * fileNme : IMG_20160524_170410_2(1).jpg
     * imgSources : android
     * user : zhong
     * time : 2016-6-3 17:33:04
     * url : 192.168.1.85:8080/WebServer/resource/\zhong\IMG_20160524_170410_2(1).jpg
     */


    private int id;
    private String absolutePath;
    private String fileNme;
    private String imgSources;
    private String user;
    private String time;
    private String url;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getFileNme() {
        return fileNme;
    }

    public void setFileNme(String fileNme) {
        this.fileNme = fileNme;
    }

    public String getImgSources() {
        return imgSources;
    }

    public void setImgSources(String imgSources) {
        this.imgSources = imgSources;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "id=" + id +
                ", absolutePath='" + absolutePath + '\'' +
                ", fileNme='" + fileNme + '\'' +
                ", imgSources='" + imgSources + '\'' +
                ", user='" + user + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
