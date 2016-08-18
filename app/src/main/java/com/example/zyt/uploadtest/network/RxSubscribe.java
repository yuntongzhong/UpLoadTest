package com.example.zyt.uploadtest.network;

import android.content.Context;
import android.util.Log;

import com.example.zyt.uploadtest.utils.ToastUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

/**
 * Created by Jam on 16-7-21
 * Description: 自定义Subscribe
 */
public abstract class RxSubscribe<T> extends Subscriber<T> {
    private Context mContext;
    private SweetAlertDialog dialog;
    private String msg;

    protected boolean showDialog() {
        return true;
    }

    /**
     * @param context context
     * @param msg     dialog message
     */
    public RxSubscribe(Context context, String msg) {
        this.mContext = context;
        this.msg = msg;
    }

    /**
     * @param context context
     */
    public RxSubscribe(Context context) {
        this(context, "请稍后...");
    }

    @Override
    public void onCompleted() {
        unSubscribe();
        if (showDialog())
            dialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (showDialog()) {
            dialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(msg);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            //点击取消的时候取消订阅
            dialog.setOnCancelListener(dialog1 -> unSubscribe());
            dialog.show();
        }
    }

    public void unSubscribe() {
        if (!isUnsubscribed()) {
            Log.d("RxSubscribe", "unsubscribe");
            unsubscribe();
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.e("RxSubscribe",e.getMessage());
        if (e instanceof ServerException) {
           // _onError(e.getMessage());
            ToastUtils.showToast(mContext,e.getMessage());
        } else {
           // _onError("请求失败，请稍后再试...");
            ToastUtils.showToast(mContext,"请求失败，请稍后再试...");
        }
        unSubscribe();
        if (showDialog())
            dialog.dismiss();
    }

    protected abstract void _onNext(T t);

   // protected abstract void _onError(String message);

}
