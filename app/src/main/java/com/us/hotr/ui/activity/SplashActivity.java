package com.us.hotr.ui.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Adv;
import com.us.hotr.storage.bean.Subject;
import com.us.hotr.ui.fragment.beauty.SelectSubjectFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/11/2.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
        new CountDownTimer(1500, 1500) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
//                next();

            }
        }.start();
    }

    private void next(){
        SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<List<Adv>>() {
            @Override
            public void onNext(List<Adv> result) {
//                if(result!=null && result.size()>0){
//                    Intent i = new Intent(SplashActivity.this, ViewpagerActivity.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable(Constants.PARAM_DATA, new ArrayList<>(result));
//                    i.putExtras(b);
//                    startActivity(i);
//
//                }else
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        };
        ServiceClient.getInstance().getAdvList(new SilentSubscriber(mListener, this, null),
                Tools.getScreenWidth(this),
                Tools.getWindowHeight(this),
                1);

    }
}
