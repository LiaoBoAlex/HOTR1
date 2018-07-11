package com.us.hotr.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.us.hotr.Constants;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.WebViewActivity;
import com.us.hotr.ui.activity.party.PartyActivity;

import java.util.HashSet;
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
                if(notice.getType() == 1) {
                    Intent i = new Intent(context, WebViewActivity.class);
                    Bundle b = new Bundle();
                    b.putInt(Constants.PARAM_TYPE, WebViewActivity.TYPE_URL);
                    b.putString(Constants.PARAM_DATA, notice.getThemeUrl());
                    i.putExtras(b);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                if(notice.getType() == 2){

                    try {
                        Set<String> set = new HashSet<String>();
                        set.add(notice.getThemeUrl());
                        JPushInterface.deleteTags(context.getApplicationContext(), -1, set);
                        Intent i = new Intent(context, PartyActivity.class);
                        Bundle b = new Bundle();
                        b.putLong(Constants.PARAM_ID, Long.parseLong(notice.getThemeUrl()));
                        i.putExtras(b);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    } catch (NumberFormatException e) {
                    }
                }
                if(notice.getType() == 3){
                    Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }

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
}
