package com.us.hotr.webservice.rxjava;

/**
 * Created by Mloong on 2017/11/13.
 */
public interface SubscriberWithFinishListener<T> {
    void onNext(T t);
    void onComplete();
    void onError(Throwable e);
}