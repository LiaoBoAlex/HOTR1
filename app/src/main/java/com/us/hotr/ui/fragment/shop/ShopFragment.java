package com.us.hotr.ui.fragment.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.activity.info.ShopOrderActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.YouzanTokenByUserIdResponse;
import com.us.hotr.webservice.response.YouzanTokenResponse;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;
import com.youzan.androidsdk.YouzanToken;
import com.youzan.androidsdk.basic.YouzanBrowser;
import com.youzan.androidsdk.event.AbsAuthEvent;

public class ShopFragment extends BaseLoadingFragment{

    YouzanBrowser youzanBrowser;

    public static ShopFragment newInstance() {
        ShopFragment shopFragment = new ShopFragment();
        return shopFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableLoadMore(false);

        youzanBrowser = (YouzanBrowser) view.findViewById(R.id.youzan_browser);
        youzanBrowser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!url.contains("homepage")){
                    Intent i = new Intent(getActivity(), ShopOrderActivity.class);
                    Bundle b = new Bundle();
                    b.putString(Constants.PARAM_TITLE, getString(R.string.shop_title));
                    b.putString(Constants.PARAM_DATA, url);
                    i.putExtras(b);
                    startActivity(i);
                    return true;
                }
                return false;
            }

        });
        youzanBrowser.subscribe(new AbsAuthEvent() {
            @Override
            public void call(Context context, boolean needLogin) {
                final User user = HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserInfo();
                if(user!=null) {
                    SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<YouzanTokenByUserIdResponse>() {
                        @Override
                        public void onNext(final YouzanTokenByUserIdResponse result) {
                            user.setYouzan_cookie_value(result.getCookie_value());
                            user.setYouzan_access_token(result.getAccess_token());
                            user.setYouzan_cookie_key(result.getCookie_key());
                            HOTRSharePreference.getInstance(getActivity()).storeUserInfo(user);
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
                    ServiceClient.getInstance().getYouzanTokenByUserId(new ProgressSubscriber(mListener, getActivity()), user.getUserId());
                } else if (needLogin){
                    LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            User user = HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserInfo();
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
                                ServiceClient.getInstance().initYouzan(new SilentSubscriber(mListener, getActivity(), refreshLayout));
                            }
                        }

                        @Override
                        public void onLoginCancel() {
                            youzanBrowser.pageGoBack();
                        }
                    });
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
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
                    ServiceClient.getInstance().initYouzan(new ProgressSubscriber(mListener, getActivity()));
                }
            }
        });

        youzanBrowser.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    if(youzanBrowser.pageCanGoBack())
                    {
                        youzanBrowser.pageGoBack();
                        return true;
                    }
                }
                return false;
            }
        });

        youzanBrowser.loadUrl(Constants.YOUZAN_CLIENT_URL);
    }

    @Override
    protected void loadData(int type) {
        youzanBrowser.reload();
        refreshLayout.finishRefresh(true);
    }
}
