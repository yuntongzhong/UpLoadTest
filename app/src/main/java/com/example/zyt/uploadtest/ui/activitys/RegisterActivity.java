package com.example.zyt.uploadtest.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.zyt.uploadtest.R;
import com.example.zyt.uploadtest.config.UserPreferences;
import com.example.zyt.uploadtest.network.RetrofitBuilder;
import com.example.zyt.uploadtest.network.RxHelper;
import com.example.zyt.uploadtest.network.RxSubscribe;
import com.example.zyt.uploadtest.utils.CheckUtils;
import com.example.zyt.uploadtest.utils.ToastUtils;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户注册
 * Created by zyt on 2016/3/14.
 */
public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.edt_userName)
    TextInputLayout edtUserName;
    @Bind(R.id.edt_passward)
    TextInputLayout edtPassward;
    @Bind(R.id.edt_confirm_passward)
    TextInputLayout edtConfirmPassward;
    @Bind(R.id.edt_email)
    TextInputLayout edtEmail;
    @Bind(R.id.edt_phone)
    TextInputLayout edtPhone;
    @Bind(R.id.rbtn_man)
    AppCompatRadioButton rbtnMan;
    @Bind(R.id.rbtn_woman)
    AppCompatRadioButton rbtnWoman;
    @Bind(R.id.rg_sex)
    RadioGroup rgSex;
    @Bind(R.id.btn_register)
    Button btnRegister;

    @BindString(R.string.login_only_can_input)
    String loginInput;

    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        addListener();
        rgSex.setOnCheckedChangeListener((group, checkedId) -> {
            //checkId就是当前选中的RadioButton
            switch (checkedId) {
                case R.id.rbtn_man:
                    sex = rbtnMan.getText().toString();
                    break;
                case R.id.rbtn_woman:
                    sex = rbtnWoman.getText().toString();
                    break;
            }
        });
    }

    void addListener() {
        edtUserName.getEditText().setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                return loginInput.toCharArray();
            }
        });
        edtPassward.getEditText().setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                return loginInput.toCharArray();
            }
        });
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        readyRegister();
    }

    void readyRegister() {
        String userName = edtUserName.getEditText().getText().toString().trim();
        String passward = edtPassward.getEditText().getText().toString().trim();
        String email = edtEmail.getEditText().getText().toString().trim();
        String confrimPwd = edtConfirmPassward.getEditText().getText().toString().trim();
        String phone = edtPhone.getEditText().getText().toString().trim();
        if (check(userName, passward, confrimPwd, sex, phone, email)) {
            register(userName, passward, sex, phone, email);
        }
    }

    boolean check(String userName, String passward, String confrimPwd, String sex, String phone, String email) {


        if (userName == null || userName.length() < 1) {
            ToastUtils.showToast(this, "用户名不能为空");
            return false;
        }
        if (passward == null || passward.length() < 1) {
            ToastUtils.showToast(this, "密码不能为空");
            return false;
        }

        if (email == null || !CheckUtils.checkEmail(email)) {
            ToastUtils.showToast(this, "请输入正确的电子邮箱");
        }
        if (sex == null || sex.length() < 1) {
            ToastUtils.showToast(this, "请选择您的性别");

            return false;
        }
        if (passward.length() < 6) {
            ToastUtils.showToast(this, "密码不能低于6位");
            return false;
        }
        if (!(passward.equals(confrimPwd))) {
            ToastUtils.showToast(this, "前后密码输入不一致");
            return false;
        }
        if (phone.length() > 0 && phone.length() != 11) {
            ToastUtils.showToast(this, "请输入正确的手机号码");
            return false;
        }

        return true;
    }

    void register(final String uesrName, String userPwd, String phone, String sex, String email) {
        RetrofitBuilder.getApiService()
                .register(uesrName, userPwd, email, phone, sex)
                .compose(RxHelper.<String>handleResult())
                .subscribe(new RxSubscribe<String>(this) {
                    @Override
                    protected void _onNext(String message) {
                        UserPreferences.getInstance(RegisterActivity.this).setUserName(uesrName);
                        startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
                        RegisterActivity.this.finish();
                    }

                });
    }
}
