package com.example.zyt.uploadtest.ui.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyt.uploadtest.R;
import com.example.zyt.uploadtest.entity.Str;
import com.example.zyt.uploadtest.network.ImageService;
import com.example.zyt.uploadtest.network.MultipartBuilder;
import com.example.zyt.uploadtest.network.RetrofitBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TestActivity extends Activity {

    @Bind(R.id.btn_test)
    Button btnTest;
    @Bind(R.id.tv_test)
    TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_test)
    public void onClick() {
        List<String> paths = new ArrayList<>();
        paths.add("/storage/sdcard0/M8/Problem/90100010_666076101835.jpg");
        uploadAll(paths);
    }

    void uploadAll(List<String> paths) {
        List<File> files = new ArrayList<>();
        for (String path : paths) {
            File file = new File(path);
            if (file.exists())
                files.add(file);
        }

        MultipartBody body = MultipartBuilder.filesToMultipartBody(files);
        RetrofitBuilder.buildRetrofit().create(ImageService.class)
                .uploadTest(body, "9543a7dbddac81b0386ba0fa6d19b392")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        tvTest.setText(s);
                    }
                });
    }



}
