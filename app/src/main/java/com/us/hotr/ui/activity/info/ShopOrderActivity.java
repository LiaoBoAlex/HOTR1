package com.us.hotr.ui.activity.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.YouzanTokenByUserIdResponse;
import com.us.hotr.webservice.response.YouzanTokenResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;
import com.youzan.androidsdk.YouzanToken;
import com.youzan.androidsdk.basic.YouzanBrowser;
import com.youzan.androidsdk.event.AbsAuthEvent;

public class ShopOrderActivity extends BaseLoadingActivity {

    YouzanBrowser youzanBrowser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(getIntent().getExtras().getString(Constants.PARAM_TITLE));
        mToolBar.setVisibility(View.VISIBLE);
        enableLoadMore(false);

        youzanBrowser = (YouzanBrowser) findViewById(R.id.youzan_browser);
        youzanBrowser.loadUrl(getIntent().getExtras().getString(Constants.PARAM_DATA));
        youzanBrowser.subscribe(new AbsAuthEvent() {
            @Override
            public void call(Context context, boolean needLogin) {
                final User user = HOTRSharePreference.getInstance(ShopOrderActivity.this.getApplicationContext()).getUserInfo();
                if(user!=null) {
                    SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<YouzanTokenByUserIdResponse>() {
                        @Override
                        public void onNext(final YouzanTokenByUserIdResponse result) {
                            user.setYouzan_cookie_value(result.getCookie_value());
                            user.setYouzan_access_token(result.getAccess_token());
                            user.setYouzan_cookie_key(result.getCookie_key());
                            HOTRSharePreference.getInstance(getApplicationContext()).storeUserInfo(user);
                            YouzanToken token = new YouzanToken();
                            token.setAccessToken(result.getAccess_token());
                            token.setCookieValue(result.getCookie_value());
                            token.setCookieKey(result.getCookie_key());
                            youzanBrowser.sync(token);

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if(user.getYouzan_access_token()!=null && !user.getYouzan_access_token().isEmpty()) {
                                YouzanToken token = new YouzanToken();
                                token.setAccessToken(user.getYouzan_access_token());
                                token.setCookieKey(user.getYouzan_cookie_key());
                                token.setCookieValue(user.getYouzan_cookie_value());
                                youzanBrowser.sync(token);
                            }
                        }
                    };
                    ServiceClient.getInstance().getYouzanTokenByUserId(new ProgressSubscriber(mListener, ShopOrderActivity.this), user.getUserId());
                } else if (needLogin){
                    LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            User user = HOTRSharePreference.getInstance(ShopOrderActivity.this.getApplicationContext()).getUserInfo();
                            if(user.getYouzan_access_token()!=null && !user.getYouzan_access_token().isEmpty()) {
                                YouzanToken token = new YouzanToken();
                                token.setAccessToken(user.getYouzan_access_token());
                                token.setCookieKey(user.getYouzan_cookie_key());
                                token.setCookieValue(user.getYouzan_cookie_value());
                                youzanBrowser.sync(token);
                            }else{
                                SubscriberListener mListener = new SubscriberListener<YouzanTokenResponse>() {
                                    @Override
                                    public void onNext(final YouzanTokenResponse result) {
                                        YouzanToken token = new YouzanToken();
                                        token.setAccessToken(result.getAccessToken());
                                        youzanBrowser.sync(token);

                                    }
                                };
                                ServiceClient.getInstance().initYouzan(new ProgressSubscriber(mListener, ShopOrderActivity.this));
                            }
                        }

                        @Override
                        public void onLoginCancel() {
                            if(youzanBrowser.pageCanGoBack())
                                youzanBrowser.pageGoBack();
                            else
                                finish();
                        }
                    });
                    Intent intent = new Intent(ShopOrderActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    SubscriberListener mListener = new SubscriberListener<YouzanTokenResponse>() {
                        @Override
                        public void onNext(final YouzanTokenResponse result) {
                            YouzanToken token = new YouzanToken();
                            token.setAccessToken(result.getAccessToken());
                            youzanBrowser.sync(token);

                        }
                    };
                    ServiceClient.getInstance().initYouzan(new ProgressSubscriber(mListener, ShopOrderActivity.this));
                }
            }
        });
    }

    @Override
    protected void loadData(int type) {
        youzanBrowser.reload();
        refreshLayout.finishRefresh(true);
    }

    @Override
    public void onBackPressed() {
        if(youzanBrowser.pageCanGoBack())
            youzanBrowser.pageGoBack();
        else
            super.onBackPressed();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_shop;
    }

}
