package com.example.zyt.uploadtest.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.zyt.uploadtest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/7.
 */
public class MainMenuActivity extends AppCompatActivity {

    @Bind(R.id.upload)
    Button upload;
    @Bind(R.id.show)
    Button show;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian_menu_activity);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.upload, R.id.show})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload:
                startActivity(new Intent(this, UploadActivity.class));
                break;
            case R.id.show:
                startActivity(new Intent(this, ShowImgActivity.class));
                break;
        }
    }




}
