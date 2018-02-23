package com.us.hotr.wxapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.HOTRApplication;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetLoginResponse;
import com.us.hotr.webservice.response.GetWechatUserInfo;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Mloong on 2017/11/2.
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 5;

    private static final String TAG = "WXEntryActivity";

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
        if(resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
            if(resp.errCode == 0){
                Tools.Toast(this, getString(R.string.wechat_pay_success2));
            }else{
                Tools.Toast(this, getString(R.string.wechat_pay_fail));
            }
            finish();
        }
        if (resp.transaction != null && resp.transaction.equals(Constants.SHARE_TO_WECHAT_FRIEND)) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    Tools.Toast(this, getString(R.string.wechat_share_fail));
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Tools.Toast(this, getString(R.string.wechat_share_cancel));
                    break;

                case BaseResp.ErrCode.ERR_OK:
                    Tools.Toast(this, getString(R.string.wechat_share_success));
                    break;
            }
            finish();
        }
        if (resp.transaction != null && resp.transaction.equals(Constants.SHARE_TO_WECHAT_TIMELINE)) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    Tools.Toast(this, getString(R.string.wechat_share_fail));
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Tools.Toast(this, getString(R.string.wechat_share_cancel));
                    break;
                case BaseResp.ErrCode.ERR_OK:
                    Tools.Toast(this, getString(R.string.wechat_share_success));
                    break;
            }
            finish();
        }
//
        if(resp instanceof  SendAuth.Resp){
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    Tools.Toast(this, getString(R.string.wechat_login_fail));
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Tools.Toast(this, getString(R.string.wechat_login_cancel));
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_OK:
                    if (((SendAuth.Resp) resp).state != null && ((SendAuth.Resp) resp).state.equals(Constants.LOGIN_TO_WECHAT)) {
                        SubscriberWithFinishListener subscriber = new SubscriberWithFinishListener<GetLoginResponse>() {
                            @Override
                            public void onNext(GetLoginResponse result) {
                                GlobalBus.getBus().post(new Events.WechatLogin(result));
                            }

                            @Override
                            public void onComplete() {
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                finish();
                            }
                        };
                        ServiceClient.getInstance().loginWithWechat(new ProgressSubscriber(subscriber, WXEntryActivity.this),
                                Constants.WECHAT_APP_ID,
                                Constants.WECHAT_APP_SECRET,
                                ((SendAuth.Resp) resp).code,
                                "authorization_code");
                    }else{
                        Tools.Toast(this, getString(R.string.wechat_login_fail));
                        finish();
                    }
                    break;
                default:
                    finish();
            }
        }
    }
}
