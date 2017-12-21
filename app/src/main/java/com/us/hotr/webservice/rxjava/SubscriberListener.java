package com.us.hotr.webservice.rxjava;

/**
 * Created by Mloong on 2017/11/14.
 */

public interface SubscriberListener<T> {
    void onNext(T t);
}
