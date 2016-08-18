package com.example.zyt.uploadtest.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zyt.uploadtest.R;
import com.example.zyt.uploadtest.config.UserPreferences;
import com.example.zyt.uploadtest.network.RetrofitBuilder;
import com.example.zyt.uploadtest.network.RxHelper;
import com.example.zyt.uploadtest.network.RxSubscribe;
import com.example.zyt.uploadtest.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/16.
 * Created by zyt on 2016/8/16.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Bind(R.id.edt_userName)
    TextInputLayout edtUserName;
    @Bind(R.id.edt_passward)
    TextInputLayout edtPassward;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.tv_register_number)
    Button tvRegisterNumber;
    @Bind(R.id.version)
    TextView version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        String username = UserPreferences.getInstance(LoginActivity.this).getUserName();
        edtUserName.getEditText().setText(username);
    }

    @OnClick({R.id.btn_login, R.id.tv_register_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_register_number:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    private void login() {
        String userName = edtUserName.getEditText().getText().toString();
        String passWord = edtPassward.getEditText().getText().toString();
        if (userName.length() < 1) {
            ToastUtils.showToast(this, "用户名不能为空");
            return;
        }
        if (passWord.length() < 1) {
            ToastUtils.showToast(this, "密码不能为空");
            return;
        }
        RetrofitBuilder.getApiService()
                .login(userName, passWord)
                .compose(RxHelper.<String>handleResult())
                .subscribe(new RxSubscribe<String>(this, "登录中...") {
                    @Override
                    protected void _onNext(String message) {
                        Log.i(TAG, message);
                        String userName = edtUserName.getEditText().getText().toString();
                        UserPreferences.getInstance(LoginActivity.this).setUserName(userName);
                        startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(LoginActivity.this, message);
                    }
                });
    }
}
