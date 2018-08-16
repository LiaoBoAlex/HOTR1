package com.us.hotr.webservice.rxjava;

import android.content.Context;
import android.content.Intent;

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
 * Created by Mloong on 2017/11/13.
 */

public class ProgressSubscriber<T> extends DisposableObserver<T> implements ProgressCancelListener{

    private SubscriberWithFinishListener mSubscriberOnNextListener;
    private SubscriberListener mSubscriberListener;
    private SubscriberWithReloadListener mSubscriberWithReloadListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;

    public ProgressSubscriber(SubscriberWithFinishListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    public ProgressSubscriber(SubscriberListener mSubscriberListener, Context context) {
        this.mSubscriberListener = mSubscriberListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    public ProgressSubscriber(SubscriberWithReloadListener mSubscriberWithReloadListener, Context context) {
        this.mSubscriberWithReloadListener = mSubscriberWithReloadListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        if(mSubscriberOnNextListener!=null)
            mSubscriberOnNextListener.onComplete();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Tools.Toast(context, context.getString(R.string.network_error));
        } else if (e instanceof ConnectException) {
            Tools.Toast(context, context.getString(R.string.network_error));
        }else if (e instanceof ApiException){
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
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
        if (mSubscriberListener != null) {
            mSubscriberListener.onNext(t);
        }
        if (mSubscriberWithReloadListener != null) {
            mSubscriberWithReloadListener.onNext(t);
        }
    }

    @Override
    public void onCancelProgress() {
        if (!this.isDisposed()) {
            this.dispose();
        }
        if(mSubscriberOnNextListener!=null)
            mSubscriberOnNextListener.onComplete();
    }
}
