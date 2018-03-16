package com.us.hotr.wxapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.ui.HOTRApplication;
import com.us.hotr.util.Tools;

/**
 * Created by liaobo on 2018/2/26.
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        HOTRApplication.getIwxApi().handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    //app发送消息给微信，微信返回的消息回调函数,根据不同的返回码来判断操作是否成功
    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Tools.Toast(this, getString(R.string.wechat_pay_success2));
                GlobalBus.getBus().post(new Events.PaymentSuccess());
            } else {
                Tools.Toast(this, getString(R.string.wechat_pay_fail));
            }
            setResult(RESULT_OK);
            finish();
        }
    }
}
