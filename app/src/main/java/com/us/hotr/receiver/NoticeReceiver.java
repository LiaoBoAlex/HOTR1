package com.us.hotr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.WebViewActivity;
import com.us.hotr.ui.activity.beauty.CaseActivity;
import com.us.hotr.ui.activity.beauty.DoctorActivity;
import com.us.hotr.ui.activity.beauty.HospitalActivity;
import com.us.hotr.ui.activity.beauty.ListActivity;
import com.us.hotr.ui.activity.beauty.ListWithSearchActivity;
import com.us.hotr.ui.activity.beauty.ProductActivity;
import com.us.hotr.ui.activity.beauty.SelectSubjectActivity;
import com.us.hotr.ui.activity.beauty.SubjectActivity;
import com.us.hotr.ui.activity.found.GroupDetailActivity;
import com.us.hotr.ui.activity.found.NearbyActivity;
import com.us.hotr.ui.activity.info.InviteFriendActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.activity.massage.MassageActivity;
import com.us.hotr.ui.activity.massage.MasseurActivity;
import com.us.hotr.ui.activity.massage.SpaActivity;
import com.us.hotr.ui.activity.party.PartyActivity;
import com.us.hotr.util.Tools;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by liaobo on 2018/3/15.
 */

public class NoticeReceiver extends BroadcastReceiver{
    private final String TAG = "JPushMessage";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
                Notice notice = new Gson().fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA), Notice.class);
                handleClickEvent(context, notice);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e){

        }
    }

    private void handleClickEvent(Context mContext, Notice notice){
        switch (Integer.valueOf(notice.getType())){
            case 1:
                Intent i = new Intent(mContext, WebViewActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.PARAM_TYPE, WebViewActivity.TYPE_URL);
                b.putString(Constants.PARAM_DATA, notice.getThemeUrl());
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 2:
                try {
                    Set<String> set = new HashSet<String>();
                    set.add(notice.getThemeUrl());
                    JPushInterface.deleteTags(mContext.getApplicationContext(), -1, set);
                    i = new Intent(mContext, PartyActivity.class);
                    b = new Bundle();
                    b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                    i.putExtras(b);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                } catch (NumberFormatException e) {
                }
                break;
            case 3:
                i = new Intent(mContext, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 5:
                i = new Intent(mContext, ProductActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 6:
                i = new Intent(mContext, DoctorActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 7:
                i = new Intent(mContext, HospitalActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 11:
                i = new Intent(mContext, MasseurActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 12:
                i = new Intent(mContext, SpaActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 13:
                i = new Intent(mContext, MassageActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 16:
                i = new Intent(mContext, CaseActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 18:
                i = new Intent(mContext, CaseActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
            case 23:
                i = new Intent(mContext, PartyActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                i.putExtras(b);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
                break;
        }
    }
}
