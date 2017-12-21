package com.us.hotr.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Mloong on 2017/9/5.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }
}
