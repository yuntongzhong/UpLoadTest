package com.example.zyt.uploadtest.ui.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zyt.uploadtest.R;
import com.example.zyt.uploadtest.entity.ImageInfo;
import com.example.zyt.uploadtest.network.ImageService;
import com.example.zyt.uploadtest.network.RetrofitBuilder;
import com.example.zyt.uploadtest.ui.adapter.CommonAdapter;
import com.example.zyt.uploadtest.ui.adapter.ViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/6/7.
 */
public class ShowImgActivity extends AppCompatActivity {


    @Bind(R.id.gridview)
    GridView gridview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_img_activity);
        ButterKnife.bind(this);
        getInfo();
    }

    void getInfo() {
        RetrofitBuilder.buildRetrofit().create(ImageService.class)
                .getImgInfo("zhong")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<ImageInfo>, List<ImageInfo>>() {
                    @Override
                    public List<ImageInfo> call(List<ImageInfo> imageInfos) {
                        return filter(imageInfos);
                    }
                })
                .subscribe(new Subscriber<List<ImageInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<ImageInfo> images) {
                        Log.e("ShowImgActivity", images.size() + "");
                        for (ImageInfo imageInfo : images) {
                            System.out.println(imageInfo.toString());
                        }
                        gridview.setAdapter(new CommonAdapter<ImageInfo>(ShowImgActivity.this, images, R.layout.item_fragment_list_imgs) {
                            @Override
                            public void convert(ViewHolder holder, ImageInfo imageInfo) {
                                ImageView imageView = holder.getView(R.id.id_img);
                                ViewGroup.LayoutParams layoutParams=imageView.getLayoutParams();
                                layoutParams.height= gridview.getWidth()/3;
                                Log.e("width", gridview.getWidth()/3+"");
                                Log.e("imgurl", imageInfo.getUrl());
                                String url=imageInfo.getUrl();
                                url=url.startsWith("http://")?url:"http://"+url;
                                Glide.with(ShowImgActivity.this).load(url)
                                       .into(imageView);
                            }
                        });
                    }
                });
    }

    List<ImageInfo> filter(List<ImageInfo> imageInfos) {
        List<ImageInfo> temp = new ArrayList<>();
        for (ImageInfo imageInfo : imageInfos) {
            String filename = imageInfo.getFileNme();
            if (filename.endsWith(".jpg") || filename.endsWith(".png")
                    || filename.endsWith(".jpeg") || filename.endsWith(".gif")) {
                temp.add(imageInfo);
            }
        }
        return temp;
    }

}
