package com.us.hotr.ui;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.request.GetAppVersionRequest;
import com.us.hotr.webservice.response.GetAppVersionResponse;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Mloong on 2017/8/30.
 */

public class HOTRApplication extends MultiDexApplication {

    private static IWXAPI iwxApi;
    private Activity mCurrentActivity;
    public static boolean isMaintaince = false;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, R.color.text_black);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler();
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);

        try{
            StatService.startStatService(getApplicationContext(), null, com.tencent.stat.common.StatConstants.VERSION);
            Log.e("MTA","MTA初始化成功");
        } catch (MtaSDkException e) {
            Log.e("MTA","MTA初始化失败"+e);
        }
//
        iwxApi = WXAPIFactory.createWXAPI(getApplicationContext(), Constants.WECHAT_APP_ID, true);
        iwxApi.registerApp(Constants.WECHAT_APP_ID);

        SDKInitializer.initialize(getApplicationContext());

        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND | JMessageClient.FLAG_NOTIFY_WITH_LED | JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
        new NotificationClickEventReceiver(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
        builder.statusBarDrawable = R.drawable.logo_hotr_round;
        JPushInterface.setPushNotificationBuilder(0, builder);

    }

    public static IWXAPI getIwxApi(){return iwxApi;}

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public class ApplicationLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

        private  boolean isInBackground = true;

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            mCurrentActivity = activity;
            if(isInBackground){
//                checkAppVersion();
                isInBackground = false;
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onConfigurationChanged(Configuration configuration) {
        }


        @Override
        public void onLowMemory() {
        }

        @Override
        public void onTrimMemory(int i) {
            if(i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
                isInBackground = true;
            }
        }
    }

    private void checkAppVersion(){
        SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<GetAppVersionResponse>() {
            @Override
            public void onNext(GetAppVersionResponse result) {
                if(result.isUpdate_force()){
                    isMaintaince = true;
                    TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(mCurrentActivity);
                    alertDialogBuilder.setMessage(R.string.force_version_update);
                    alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Tools.openApplicationMarket(getApplicationContext());
                                    System.exit(0);
                                }
                            });
                    alertDialogBuilder.setNegativeButton(getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    System.exit(0);
                                }
                            });
                    alertDialogBuilder.create().show();
                }
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
            }
        };

        GetAppVersionRequest request = new GetAppVersionRequest();
        request.setDevice_type(0);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            request.setVersion_code(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            request.setVersion_code("0");
        }
        ServiceClient.getInstance().getAppVersion(mListener, request);
    }
}
