package com.example.zyt.uploadtest.ui.activitys;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyt.uploadtest.R;
import com.example.zyt.uploadtest.entity.Str;
import com.example.zyt.uploadtest.network.BaseResponse;
import com.example.zyt.uploadtest.network.FileuploadService;
import com.example.zyt.uploadtest.network.ImageService;
import com.example.zyt.uploadtest.network.MultipartBuilder;
import com.example.zyt.uploadtest.network.RetrofitBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {

    //private String mBaseUrl = "http://10.0.2.2:8080/WebServer/";
    private String mBaseUrl = "http://192.168.1.85:8080/WebServer/";

    public static final String TAG = MainActivity.class.getSimpleName();

    Button btnAdd, btnUpload, bntClearList;
    ListView listView;
    List<String> paths = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCount = (TextView) findViewById(R.id.tv_count);
        bntClearList = (Button) findViewById(R.id.btn_clearlist);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        listView = (ListView) findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paths);

        listView.setAdapter(arrayAdapter);

        btnAdd.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        bntClearList.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String path = getRealFilePath(this, uri);
            paths.add(path);
            arrayAdapter.notifyDataSetChanged();
            tvCount.setText("已选择" + paths.size() + "个文件");
        }
    }


    //根据uri获取绝对路径
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = paths.get(position);
        Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        paths.remove(position);
        arrayAdapter.notifyDataSetChanged();
        tvCount.setText("已选择" + paths.size() + "个文件");
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_upload:
                uploadAll();
                break;
            case R.id.btn_clearlist:
                paths.clear();
                arrayAdapter.notifyDataSetChanged();
                tvCount.setText("已选择" + paths.size() + "个文件");
                break;
            default:
                break;
        }
    }

    void uploadAll() {
        List<File> files = new ArrayList<>();
        for (String path : paths) {
            File file = new File(path);
            if (file.exists())
                files.add(file);
        }

        MultipartBody body = MultipartBuilder.filesToMultipartBody(files);
        RetrofitBuilder.buildRetrofit().create(ImageService.class)
                .uploadFileWithRequestBody(body, "zhong")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Str>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Str s) {
                        Toast.makeText(MainActivity.this, s.getResult(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
