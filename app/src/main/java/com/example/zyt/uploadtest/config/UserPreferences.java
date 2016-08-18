package com.example.zyt.uploadtest.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存登录用户
 *
 * @author zyt
 */
public class UserPreferences {
    private SharedPreferences mSharedP;
    private static UserPreferences userPreferences;

    private final String USER_NAME_KEY = "user_name";//用户名

    private UserPreferences(Context context) {
        mSharedP = context.getSharedPreferences(
                "user_pref", Context.MODE_PRIVATE);
    }

    public static UserPreferences getInstance(Context context) {
        if (userPreferences == null)
            userPreferences = new UserPreferences(context);
        return userPreferences;
    }

    //设置用户名
    public void setUserName(String value) {
        addOrReplace(USER_NAME_KEY, value);
    }

    //获取用户名
    public String getUserName() {
        return mSharedP.getString(USER_NAME_KEY, "");
    }

    /**
     * 添加或替换一对键值
     *
     * @param key
     * @param value
     */
    public void addOrReplace(String key, String value) {
        mSharedP.edit().putString(key, value).commit();
    }

    /**
     * 获取对应key的valus
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return mSharedP.getString(key, "");
    }

    /**
     * 删除对应key数据
     *
     * @param key
     */
    public void delete(String key) {
        mSharedP.edit().remove(key).commit();
    }

    //清除所有数据
    public void clear() {
        mSharedP.edit().clear().commit();
    }
}
