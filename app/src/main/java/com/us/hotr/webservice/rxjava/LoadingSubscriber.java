package com.us.hotr.webservice.rxjava;

import android.content.Intent;
import android.widget.Toast;

import com.us.hotr.Constants;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.util.Tools;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by Mloong on 2017/11/13.
 */

public class LoadingSubscriber<T> extends DisposableObserver<T>{

    private SubscriberListener mSubscriberListener;
    private SubscriberWithReloadListener mSubscriberWithReloadListener;
    private BaseLoadingFragment mFragment;
    private BaseLoadingActivity mActivity;
    private static int count = 0;
//    private Context context;

    public LoadingSubscriber(SubscriberListener mSubscriberListener, BaseLoadingFragment mFragment) {
        this.mSubscriberListener = mSubscriberListener;
        this.mFragment = mFragment;
    }

    public LoadingSubscriber(SubscriberListener mSubscriberListener, BaseLoadingActivity mActivity) {
        this.mSubscriberListener = mSubscriberListener;
        this.mActivity = mActivity;
    }

    public LoadingSubscriber(SubscriberWithReloadListener mSubscriberWithReloadListener, BaseLoadingFragment mFragment) {
        this.mSubscriberWithReloadListener = mSubscriberWithReloadListener;
        this.mFragment = mFragment;
    }

    public LoadingSubscriber(SubscriberWithReloadListener mSubscriberWithReloadListener, BaseLoadingActivity mActivity) {
        this.mSubscriberWithReloadListener = mSubscriberWithReloadListener;
        this.mActivity = mActivity;
    }

    @Override
    public void onStart() {
        if(mFragment!=null)
            mFragment.showLoadingPage();
        if(mActivity!=null) {
            mActivity.showLoadingPage();
            count ++;
        }
    }

    @Override
    public void onComplete() {
        if(mFragment!=null)
            mFragment.showContentPage();
        if(mActivity!=null) {
            count--;
            if (count == 0)
                mActivity.showContentPage();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(mFragment!=null) {
            if (e instanceof SocketTimeoutException) {
                mFragment.showErrorPage();
            } else if (e instanceof ConnectException) {
                mFragment.showErrorPage();
            } else if (e instanceof ApiException) {
                mFragment.showErrorPage();
                if(((ApiException) e).getErrorCode() == Constants.ERROR_INVALID_SESSIONID){
                    Tools.Toast(mFragment.getActivity(), ((ApiException) e).getErrorMsg());
                    HOTRSharePreference.getInstance(mFragment.getActivity().getApplicationContext()).storeUserID("");
//                    LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
//                        @Override
//                        public void onLoginSuccess() {
//                            if(mSubscriberWithReloadListener!=null)
//                                mSubscriberWithReloadListener.reload();
//                        }
//                    });
                    Intent i = new Intent(mFragment.getActivity(), LoginActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mFragment.getActivity().startActivity(i);
                }
            } else {
                mFragment.showErrorPage();
            }
        }
        if(mActivity!=null) {
            count--;
            if (e instanceof SocketTimeoutException) {
                mActivity.showErrorPage();
            } else if (e instanceof ConnectException) {
                mActivity.showErrorPage();
            } else if (e instanceof ApiException) {
                mActivity.showErrorPage();
                if(((ApiException) e).getErrorCode() == Constants.ERROR_INVALID_SESSIONID){
                    Tools.Toast(mFragment.getActivity(), ((ApiException) e).getErrorMsg());
                    HOTRSharePreference.getInstance(mActivity.getApplicationContext()).storeUserID("");
//                    LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
//                        @Override
//                        public void onLoginSuccess() {
//                            if(mSubscriberWithReloadListener!=null)
//                                mSubscriberWithReloadListener.reload();
//                        }
//                    });
                    Intent i = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(i);
                }
            } else {
                mActivity.showErrorPage();
            }
        }

    }

    @Override
    public void onNext(T t) {
        if (mSubscriberListener != null)
            mSubscriberListener.onNext(t);
        if(mSubscriberWithReloadListener!=null)
            mSubscriberWithReloadListener.onNext(t);
    }
}
