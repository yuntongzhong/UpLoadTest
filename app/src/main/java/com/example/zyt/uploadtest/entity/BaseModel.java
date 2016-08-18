package com.example.zyt.uploadtest.entity;

import java.io.Serializable;

/**
 * Created by Jam on 16-8-12
 * Description:
 */
public class BaseModel<T> implements Serializable {
    public String code;
    public T result;

    public boolean success() {
        return code.equals("0");
    }
}
