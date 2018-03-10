package com.us.hotr.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.us.hotr.Constants;
import com.us.hotr.ui.HOTRApplication;

/**
 * Created by liaobo on 2018/2/26.
 */

public class AppRegister extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        HOTRApplication.getIwxApi().registerApp(Constants.WECHAT_APP_ID);
    }
}
