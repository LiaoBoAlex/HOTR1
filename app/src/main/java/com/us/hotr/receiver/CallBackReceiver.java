package com.us.hotr.receiver;

import android.content.Context;

import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by liaobo on 2018/3/16.
 */

public class CallBackReceiver extends JPushMessageReceiver {
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
        if(jPushMessage.getSequence() >= 0)
            GlobalBus.getBus().post(new Events.JPushSetTag(jPushMessage.getErrorCode() == 0?true:false));
    }
}
