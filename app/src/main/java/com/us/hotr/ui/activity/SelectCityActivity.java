package com.us.hotr.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.fragment.SelectCityFragment;
import com.us.hotr.util.PermissionUtil;
import com.us.hotr.util.Tools;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Mloong on 2017/8/28.
 */

public class SelectCityActivity extends BaseActivity {

    private TextView tvCurrentCity;
    private HOTRSharePreference p;

    private LocationClient mLocationClient = null;
    public BDAbstractLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.city_list);
        p = HOTRSharePreference.getInstance(getApplicationContext());
        initStaticView();

        if (PermissionUtil.hasLocationPermission(this)) {
            initLocationClient();
        } else {
            PermissionUtil.requestLocationPermission(this);
        }
    }

    private void initLocationClient(){
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(true);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtil.PERMISSIONS_REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocationClient();
                }
                else{
                    if(HOTRSharePreference.getInstance(getApplicationContext()).getCurrentCityID().isEmpty()){
                        HOTRSharePreference.getInstance(getApplicationContext()).storeCurrrentCityName(getString(R.string.filter_city));
                        HOTRSharePreference.getInstance(getApplicationContext()).storeCurrentCityID(Constants.ALL_CITY_ID+"");
                        tvCurrentCity.setText(R.string.filter_city);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            p.storeCurrentProvinceName(location.getProvince());
            p.storeCurrentCityID(location.getCityCode());
            p.storeCurrrentCityName(location.getCity());
            p.storeLatitude(location.getLatitude());
            p.storeLongitude(location.getLongitude());
            tvCurrentCity.setText(location.getCity());
            mLocationClient.stop();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_city;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mLocationClient!=null)
            mLocationClient.start();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mLocationClient!=null)
            mLocationClient.stop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getMessage(Events.CitySelected citySelectedEvent) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.PARAM_NAME, citySelectedEvent.getSelectedCity());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void initStaticView(){

        tvCurrentCity = (TextView) findViewById(R.id.tv_current_city);
        tvCurrentCity.setText(p.getCurrentCityName());
        tvCurrentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.storeSelectedCityName(p.getCurrentCityName());
                p.storeSelectedMassageCityID(Tools.getCityCodeFromBaidu(p.getCurrentCityName()).getMassageCityCode());
                p.storeSelectedProductCityID(Tools.getCityCodeFromBaidu(p.getCurrentCityName()).getProductCityCode());
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constants.PARAM_NAME, tvCurrentCity.getText().toString().trim());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        Fragment fragment = new SelectCityFragment().newInstance(getIntent().getExtras().getInt(Constants.PARAM_TYPE), true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment).commit();
    }
}
