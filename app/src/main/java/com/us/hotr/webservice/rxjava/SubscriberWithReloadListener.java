package com.us.hotr.webservice.rxjava;

/**
 * Created by liaobo on 2017/12/18.
 */

public interface SubscriberWithReloadListener<T> {
    void onNext(T t);
    void reload();
}
