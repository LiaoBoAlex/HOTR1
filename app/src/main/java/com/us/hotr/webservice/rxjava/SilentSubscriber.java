package com.us.hotr.webservice.rxjava;

import android.content.Context;
import android.content.Intent;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.util.Tools;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import cn.jpush.im.android.api.JMessageClient;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by liaobo on 2017/12/4.
 */

public class SilentSubscriber<T> extends DisposableObserver<T>{

    private RefreshLayout refreshLayout;
    private SubscriberListener mSubscriberListener;
    private SubscriberWithFinishListener mSubscriberOnNextListener;
    private SubscriberWithReloadListener mSubscriberWithReloadListener;

    private Context context;

    public SilentSubscriber(SubscriberListener mSubscriberListener, Context context, RefreshLayout refreshLayout) {
        this.mSubscriberListener = mSubscriberListener;
        this.context = context;
        this.refreshLayout = refreshLayout;
    }

    public SilentSubscriber(SubscriberWithFinishListener mSubscriberOnNextListener, Context context, RefreshLayout refreshLayout) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.refreshLayout = refreshLayout;
    }

    public SilentSubscriber(SubscriberWithReloadListener mSubscriberWithReloadListener, Context context, RefreshLayout refreshLayout) {
        this.mSubscriberWithReloadListener = mSubscriberWithReloadListener;
        this.context = context;
        this.refreshLayout = refreshLayout;
    }

    @Override
    public void onComplete() {
        if(refreshLayout!=null) {
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh();
        }
        if(mSubscriberOnNextListener!=null)
            mSubscriberOnNextListener.onComplete();
    }

    @Override
    public void onError(Throwable e) {
        if(refreshLayout!=null) {
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh();
        }
        if (e instanceof SocketTimeoutException) {
            Tools.Toast(context, context.getString(R.string.network_error));
        } else if (e instanceof ConnectException) {
            Tools.Toast(context, context.getString(R.string.network_error));
        }else if (e instanceof ApiException){
            if(((ApiException) e).getErrorCode() == Constants.SUCCESS)
                return;
            Tools.Toast(context, ((ApiException) e).getErrorMsg());
            if(((ApiException) e).getErrorCode() == Constants.ERROR_INVALID_SESSIONID){
                Tools.logout(context.getApplicationContext());
                LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        if(mSubscriberWithReloadListener!=null)
                            mSubscriberWithReloadListener.reload();
                    }

                    @Override
                    public void onLoginCancel() {

                    }
                });
                Intent i = new Intent(context, LoginActivity.class);
                context.startActivity(i);
            }
        } else {
            Tools.Toast(context, context.getString(R.string.network_error));
        }
        if(mSubscriberOnNextListener!=null)
            mSubscriberOnNextListener.onError(e);
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberListener != null) {
            mSubscriberListener.onNext(t);
        }
        if(mSubscriberOnNextListener!=null)
            mSubscriberOnNextListener.onNext(t);
        if (mSubscriberWithReloadListener != null) {
            mSubscriberWithReloadListener.onNext(t);
        }
    }
}
