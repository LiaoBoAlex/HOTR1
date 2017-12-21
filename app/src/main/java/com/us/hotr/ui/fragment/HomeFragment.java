package com.us.hotr.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.SelectCityActivity;
import com.us.hotr.ui.activity.search.SearchHintActivity;
import com.us.hotr.ui.fragment.beauty.BeautyFragment;
import com.us.hotr.ui.fragment.massage.MassageFragment;
import com.us.hotr.ui.fragment.party.PartyFragment;

import java.util.ArrayList;

/**
 * Created by macb00k on 2017/8/25.
 */

public class HomeFragment extends Fragment {

    static final int SELECT_CITY_REQUEST = 1;

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvCity;
    private PagerAdapter adapter;
    private ImageView imgSearch;

    private LocationClient mLocationClient = null;
    public BDAbstractLocationListener myListener = new MyLocationListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLocationClient();

        titleList = new ArrayList<String>() {{
            add(getString(R.string.beauty_title));
            add(getString(R.string.massage_title));
            add(getString(R.string.party_title));
        }};

        fragmentList = new ArrayList<Fragment>() {{
            add(BeautyFragment.newInstance());
            add(MassageFragment.newInstance());
            add(PartyFragment.newInstance());
        }};

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        tvCity = (TextView) toolbar.findViewById(R.id.tv_area);
        imgSearch = (ImageView) toolbar.findViewById(R.id.img_search);

        if(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedCityID()<=0)
            tvCity.setText(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentCityName());
        else
            tvCity.setText(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedCityName());

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchHintActivity.class));
            }
        });
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SelectCityActivity.class);
                startActivityForResult(i, SELECT_CITY_REQUEST);
            }
        });
        adapter = new PagerAdapter(getChildFragmentManager(), titleList, fragmentList);
//        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    private void initLocationClient(){
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(false);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stop();
    }


    public class PagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> titleList;
        private ArrayList<Fragment> fragmentList;

        public PagerAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_CITY_REQUEST){
            if(resultCode == Activity.RESULT_OK && data != null) {
                String value = data.getExtras().getString(Constants.PARAM_NAME);
                tvCity.setText(value);
            }
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            HOTRSharePreference.storeCurrentCityID(location.getCityCode());
            HOTRSharePreference.storeCurrrentCityName(location.getCity());
            if(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedCityID()<=0)
                tvCity.setText(location.getCity());
            else
                tvCity.setText(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedCityName());

            //获取定位结果
//            location.getTime();    //获取定位时间
//            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
//            location.getLocType();    //获取定位类型
//            location.getLatitude();    //获取纬度信息
//            location.getLongitude();    //获取经度信息
//            location.getRadius();    //获取定位精准度
//            location.getAddrStr();    //获取地址信息
//            location.getCountry();    //获取国家信息
//            location.getCountryCode();    //获取国家码
//            location.getCity();    //获取城市信息
//            location.getCityCode();    //获取城市码
//            location.getDistrict();    //获取区县信息
//            location.getStreet();    //获取街道信息
//            location.getStreetNumber();    //获取街道码
//            location.getLocationDescribe();    //获取当前位置描述信息
//            location.getPoiList();    //获取当前位置周边POI信息
//
//            location.getBuildingID();    //室内精准定位下，获取楼宇ID
//            location.getBuildingName();    //室内精准定位下，获取楼宇名称
//            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息

            if (location.getLocType() == BDLocation.TypeGpsLocation){

                //当前为GPS定位结果，可获取以下信息
//                location.getSpeed();    //获取当前速度，单位：公里每小时
//                location.getSatelliteNumber();    //获取当前卫星数
//                location.getAltitude();    //获取海拔高度信息，单位米
//                location.getDirection();    //获取方向信息，单位度
//                location.

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

                //当前为网络定位结果，可获取以下信息
//                location.getOperators();    //获取运营商信息

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                //当前为网络定位结果

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                //当前网络不通

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码

            }
        }

        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {

            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {

                //建议打开GPS

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {

                //建议打开wifi，不必连接，这样有助于提高网络定位精度！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {

                //定位权限受限，建议提示用户授予APP定位权限！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {

                //网络异常造成定位失败，建议用户确认网络状态是否异常！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {

                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {

                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {

                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {

                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {

                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！

            }
        }

    }

}

