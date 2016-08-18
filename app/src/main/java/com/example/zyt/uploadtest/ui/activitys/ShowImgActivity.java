package com.example.zyt.uploadtest.ui.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zyt.uploadtest.R;
import com.example.zyt.uploadtest.config.UserPreferences;
import com.example.zyt.uploadtest.entity.ImageInfo;
import com.example.zyt.uploadtest.network.RetrofitBuilder;
import com.example.zyt.uploadtest.network.RxHelper;
import com.example.zyt.uploadtest.network.RxSubscribe;
import com.example.zyt.uploadtest.ui.adapter.CommonAdapter;
import com.example.zyt.uploadtest.ui.adapter.ViewHolder;
import com.example.zyt.uploadtest.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/6/7.
 */
public class ShowImgActivity extends AppCompatActivity {
    public static final String TAG = "ShowImgActivity";

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
        String username = UserPreferences.getInstance(this).getUserName();
        RetrofitBuilder.getApiService()
                .getImgInfo(username)
                .compose(RxHelper.<String>handleResult())
                //将json字符串转换成json数组
                .map((Func1<String, List<ImageInfo>>) imageInfos -> new Gson().fromJson(imageInfos,
                        new TypeToken<List<ImageInfo>>() {
                        }.getType()))
                .map(imageInfos -> imgsFilter(imageInfos))
                .subscribe(new RxSubscribe<List<ImageInfo>>(this, "请稍等...") {
                    @Override
                    protected void _onNext(List<ImageInfo> imageInfos) {
                        gridview.setAdapter(new CommonAdapter<ImageInfo>(ShowImgActivity.this, imageInfos, R.layout.item_fragment_list_imgs) {
                            @Override
                            public void convert(ViewHolder holder, ImageInfo imageInfo) {
                                ImageView imageView = holder.getView(R.id.id_img);
                                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                                layoutParams.height = gridview.getWidth() / 3;
                                String url = imageInfo.getUrl();
                                Glide.with(ShowImgActivity.this).load(url)
                                        .into(imageView);
                            }
                        });
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(ShowImgActivity.this, message);
                    }
                });
    }

    //过滤非图片信息
    private List<ImageInfo> imgsFilter(List<ImageInfo> imageInfos) {
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
