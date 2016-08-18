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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zyt.uploadtest.R;
import com.example.zyt.uploadtest.config.UserPreferences;
import com.example.zyt.uploadtest.network.MultipartBuilder;
import com.example.zyt.uploadtest.network.RetrofitBuilder;
import com.example.zyt.uploadtest.network.RxHelper;
import com.example.zyt.uploadtest.network.RxSubscribe;
import com.example.zyt.uploadtest.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import okhttp3.MultipartBody;

public class UploadActivity extends AppCompatActivity {
    public static final int SELECTOR_FILE = 0X1235;

    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.btn_clearlist)
    Button btnClearlist;
    @Bind(R.id.btn_addpic)
    Button btnAddpic;
    @Bind(R.id.btn_addfile)
    Button btnAddfile;
    @Bind(R.id.btn_upload)
    Button btnUpload;


    List<String> paths = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paths);
        listview.setAdapter(arrayAdapter);
    }

    @OnItemClick(R.id.listview)
    public void onItemClick(int position) {
        String path = paths.get(position);
        ToastUtils.showToast(this, path);
    }

    @OnItemLongClick(R.id.listview)
    public boolean onItemLongClick(int position) {
        paths.remove(position);
        arrayAdapter.notifyDataSetChanged();
        tvCount.setText("已选择" + paths.size() + "个文件");
        return false;
    }

    @OnClick({R.id.btn_clearlist, R.id.btn_addpic, R.id.btn_addfile, R.id.btn_upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clearlist:
                clearList();
                break;
            case R.id.btn_addpic:
                addPic();
                break;
            case R.id.btn_addfile:
                addFile();
                break;
            case R.id.btn_upload:
                uploadAll();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECTOR_FILE) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String path = getRealFilePath(this, uri);
            paths.add(path);
            refresh();
        }
    }

    private void refresh() {
        arrayAdapter.notifyDataSetChanged();
        tvCount.setText("已选择" + paths.size() + "个文件");
    }

    private void clearList() {
        paths.clear();
        refresh();
    }

    private void addPic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, SELECTOR_FILE);
    }

    private void addFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, SELECTOR_FILE);
    }

    private void uploadAll() {
        if (paths.size() < 1) {
            ToastUtils.showToast(this, "没有选择文件");
            return;
        }
        List<File> files = new ArrayList<>();
        for (String path : paths) {
            File file = new File(path);
            if (file.exists())
                files.add(file);
        }
        MultipartBody body = MultipartBuilder.filesToMultipartBody(files);
        String username = UserPreferences.getInstance(this).getUserName();
        RetrofitBuilder.getApiService()
                .uploadFileWithRequestBody(body, username)
                .compose(RxHelper.<String>handleResult())
                .subscribe(new RxSubscribe<String>(this, "请稍等...") {
                    @Override
                    protected void _onNext(String result) {
                        ToastUtils.showToast(UploadActivity.this, result);
                    }
                });
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
}
