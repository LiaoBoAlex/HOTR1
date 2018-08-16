package com.us.hotr.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.us.hotr.storage.bean.Address;
import com.us.hotr.storage.bean.Contact;
import com.us.hotr.storage.bean.User;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by Mloong on 2017/10/16.
 */

public class HOTRSharePreference {

    private static final String PREFS_NAME = "HOTR";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_SELECTED_PRODUCT_CITY_ID = "KEY_SELECTED_PRODUCT_CITY_ID";
    private static final String KEY_SELECTED_MASSAGE_CITY_ID = "KEY_SELECTED_MASSAGE_CITY_ID";
    private static final String KEY_SELECTED_CITY_NAME = "KEY_SELECTED_CITY_NAME";
    private static final String KEY_CURRENT_CITY_NAME = "KEY_CURRENT_CITY_NAME";
    private static final String KEY_MTA_USER_CURRENT_CITY_NAME = "KEY_MTA_USER_CURRENT_CITY_NAME";
    private static final String KEY_MTA_CURRENT_CITY_NAME = "KEY_MTA_CURRENT_CITY_NAME";
    private static final String KEY_CURRENT_PROVINCE_NAME = "KEY_CURRENT_PROVINCE_NAME";
    private static final String KEY_CURRENT_CITY_ID = "KEY_CURRENT_CITY_ID";
    private static final String KEY_USER_INFO = "KEY_USER_INFO";
    private static final String KEY_CONTACT_INFO = "KEY_CONTACT_INFO";
    private static final String KEY_DEFAULT_ADDRESS = "KEY_DEFAULT_ADDRESS";
    private static final String KEY_LATITUDE = "KEY_LATITUDE";
    private static final String KEY_LONGITUDE = "KEY_LONGITUDE";
    private static final String KEY_TOTAL_NOTICE_COUNT = "KEY_TOTAL_NOTICE_COUNT";
    private static final String KEY_TOTAL_MESSAGE_COUNT = "KEY_TOTAL_MESSAGE_COUNT";
    private static final String KEY_FIRST_INVITE_FRIEND = "KEY_TOTAL_MESSAGE_COUNT";
    private static final String KEY_YOUZAN_TOKEN_EXPIRED_TIME = "KEY_YOUZAN_TOKEN_EXPIRED_TIME";

    private static SharedPreferences.Editor editor;
    private static SharedPreferences settings;
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
    }

    public static String getUserID(){
        return settings.getString(KEY_USER_ID, "");
    }

    public static void storeFirstInviteFriend(boolean value){
        editor.putBoolean(KEY_FIRST_INVITE_FRIEND, value);
        editor.apply();
    }

    public static boolean isFirstinviteFriend(){
        return settings.getBoolean(KEY_FIRST_INVITE_FRIEND, true);
    }

    public static void storeYouzanTokenExpiredTime(long value){
        editor.putLong(KEY_YOUZAN_TOKEN_EXPIRED_TIME, value);
        editor.apply();
    }

    public static long getYouzanTokenExpiredTime(){
        return settings.getLong(KEY_YOUZAN_TOKEN_EXPIRED_TIME, 0);
    }


    public static boolean isUserLogin(){
        if(settings.getString(KEY_USER_ID, null) == null
                ||settings.getString(KEY_USER_ID, null).isEmpty())
            return false;
        else
            return true;
    }

    public static void storeSelectedProductCityID(final long id){
        editor.putLong(KEY_SELECTED_PRODUCT_CITY_ID, id);
        editor.apply();
    }

    public static void storeLatitude(final double latitude){
        editor.putLong(KEY_LATITUDE, Double.doubleToRawLongBits(latitude));
        editor.apply();
    }

    public static Double getLatitude(){
        if(settings.getLong(KEY_LATITUDE, -1)>=0)
            return Double.longBitsToDouble(settings.getLong(KEY_LATITUDE, -1));
        else
            return null;
    }

    public static void storeLongitude(final double longitude){
        editor.putLong(KEY_LONGITUDE, Double.doubleToRawLongBits(longitude));
        editor.apply();
    }

    public static Double getLongitude(){
        if(settings.getLong(KEY_LONGITUDE, -1)>=0)
            return Double.longBitsToDouble(settings.getLong(KEY_LONGITUDE, -1));
        else
            return null;
    }

    public static void storeSelectedMassageCityID(final long id){
        editor.putLong(KEY_SELECTED_MASSAGE_CITY_ID, id);
        editor.apply();
    }

    public static long getSelectedProductCityID(){
        return settings.getLong(KEY_SELECTED_PRODUCT_CITY_ID, -1);
    }

    public static long getSelectedMassageCityID(){
        return settings.getLong(KEY_SELECTED_MASSAGE_CITY_ID, -1);
    }

    public static void storeSelectedCityName(final String name){
        editor.putString(KEY_SELECTED_CITY_NAME, name);
        editor.apply();
    }

    public static String getSelectedCityName(){
        return settings.getString(KEY_SELECTED_CITY_NAME, "");
    }

    public static void storeCurrentProvinceName(final String name){
        editor.putString(KEY_CURRENT_PROVINCE_NAME, name);
        editor.apply();
    }

    public static String getCurrentProvinceName(){
        return settings.getString(KEY_CURRENT_PROVINCE_NAME, "");
    }

    public static void storeCurrrentCityName(final String id){
        editor.putString(KEY_CURRENT_CITY_NAME, id);
        editor.apply();
    }

    public static String getCurrentCityName(){
        return settings.getString(KEY_CURRENT_CITY_NAME, "");
    }

    public static void storeMTACurrrentCityName(final String id){
        editor.putString(KEY_MTA_CURRENT_CITY_NAME, id);
        editor.apply();
    }

    public static String getMTAUserCurrentCityName(){
        return settings.getString(KEY_MTA_USER_CURRENT_CITY_NAME, "");
    }

    public static void storeMTAUserCurrrentCityName(final String id){
        editor.putString(KEY_MTA_USER_CURRENT_CITY_NAME, id);
        editor.apply();
    }

    public static String getMTACurrentCityName(){
        return settings.getString(KEY_MTA_CURRENT_CITY_NAME, "");
    }

    public static void storeCurrentCityID(final String id){
        editor.putString(KEY_CURRENT_CITY_ID, id);
        editor.apply();
    }

    public static String getCurrentCityID(){
        return settings.getString(KEY_CURRENT_CITY_ID, "");
    }

    public static void storeNoticeCount(final int count){
        editor.putInt(KEY_TOTAL_NOTICE_COUNT, count);
        editor.apply();
    }

    public static int getNoticeCount(){
        return  settings.getInt(KEY_TOTAL_NOTICE_COUNT, 0);
    }

    public static int getMessageCount(){
        return  settings.getInt(KEY_TOTAL_MESSAGE_COUNT, 0);
    }

    public static void storeMessageCount(final int count){
        editor.putInt(KEY_TOTAL_MESSAGE_COUNT, count);
        editor.apply();
    }

    public static int getTotalNotice(){
        return  settings.getInt(KEY_TOTAL_MESSAGE_COUNT, 0) + settings.getInt(KEY_TOTAL_NOTICE_COUNT, 0);
    }

    public static void storeUserInfo(User user){
        saveObject(KEY_USER_INFO, user);
    }

    public static User getUserInfo(){
        return (User) getObject(KEY_USER_INFO);
    }

    public static void storeContactInfo(Contact contact){
        saveObject(KEY_CONTACT_INFO, contact);
    }

    public static Contact getContactInfo(){
        return (Contact) getObject(KEY_CONTACT_INFO);
    }

    public static void storeDefaultAddress(Address address){
        saveObject(KEY_DEFAULT_ADDRESS, address);
    }

    public static Address getDefaultAddress(){
        return (Address) getObject(KEY_DEFAULT_ADDRESS);
    }

    private static void saveObject(String mKey, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String server = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        editor.putString(mKey, server);
        editor.commit();
    }

    private static Object getObject(String mKey) {
        String objectInfo = settings.getString(mKey, "");
        Object object = null;
        if (objectInfo.equals("")) {
            return null;
        }
        byte[] base64Bytes = Base64.decode(objectInfo.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(bais);
            object = ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
