package com.us.hotr.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

/**
 * Created by Mloong on 2017/10/16.
 */

public class HOTRSharePreference {

    private static final String PREFS_NAME = "HOTR";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_SELECTED_CITY_ID = "KEY_SELECTED_CITY_ID";
    private static final String KEY_SELECTED_CITY_NAME = "KEY_SELECTED_CITY_NAME";
    private static final String KEY_CURRENT_CITY_NAME = "KEY_CURRENT_CITY_NAME";
    private static final String KEY_CURRENT_CITY_ID = "KEY_CURRENT_CITY_ID";

    private static SharedPreferences.Editor editor;
    private static SharedPreferences settings;
    private static Context mContext;
    private static HOTRSharePreference instanse;

    private static SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<Boolean>() {
        @Override
        public void onNext(Boolean value) {
        }

        @Override
        public void onComplete() {

        }
        @Override
        public void onError(Throwable e) {

        }
    };


    public HOTRSharePreference(Context context) {
        this.mContext = context;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public static HOTRSharePreference getInstance(Context context) {
        if (null == instanse) {
            instanse = new HOTRSharePreference(context);
        }
        return instanse;
    }

    public static void storeUserID(final String id){
        editor.putString(KEY_USER_ID, id);
        editor.apply();
//        Observable.create(new ObservableOnSubscribe<Boolean>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<Boolean> subscriber) throws Exception {
//                editor.putString(KEY_USER_ID, id);
//                editor.apply();
//                subscriber.onNext(true);
//                subscriber.onComplete();
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ProgressSubscriber(mListener, mContext));

    }

    public static String getUserID(){
        return settings.getString(KEY_USER_ID, "");
    }

    public static boolean isUserLogin(){
        if(settings.getString(KEY_USER_ID, null) == null
                ||settings.getString(KEY_USER_ID, null).isEmpty())
            return false;
        else
            return true;
    }

    public static void storeSelectedCityID(final int id){
        editor.putInt(KEY_SELECTED_CITY_ID, id);
        editor.apply();
    }

    public static int getSelectedCityID(){
        return settings.getInt(KEY_SELECTED_CITY_ID, -1);
    }

    public static void storeSelectedCityName(final String name){
        editor.putString(KEY_SELECTED_CITY_NAME, name);
        editor.apply();
    }

    public static String getSelectedCityName(){
        return settings.getString(KEY_SELECTED_CITY_NAME, "");
    }

    public static void storeCurrrentCityName(final String id){
        editor.putString(KEY_CURRENT_CITY_NAME, id);
        editor.apply();
    }

    public static String getCurrentCityName(){
        return settings.getString(KEY_CURRENT_CITY_NAME, "");
    }

    public static void storeCurrentCityID(final String id){
        editor.putString(KEY_CURRENT_CITY_ID, id);
        editor.apply();
    }

    public static String getCurrentCityID(){
        return settings.getString(KEY_CURRENT_CITY_ID, "");
    }
}
