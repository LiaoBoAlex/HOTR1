package com.us.hotr.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.SelectCityActivity;
import com.us.hotr.ui.activity.search.SearchHintActivity;
import com.us.hotr.ui.fragment.beauty.BeautyFragment;
import com.us.hotr.ui.fragment.massage.MassageFragment;
import com.us.hotr.ui.fragment.party.PartyFragment;
import com.us.hotr.ui.fragment.shop.ShopFragment;
import com.us.hotr.util.PermissionUtil;
import com.us.hotr.util.Tools;

import java.util.ArrayList;
import java.util.Properties;

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
    private int cityType = 1;

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
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        tvCity = (TextView) toolbar.findViewById(R.id.tv_subjects);
        imgSearch = (ImageView) toolbar.findViewById(R.id.img_search);

        if (PermissionUtil.hasLocationPermission(getActivity())) {
            initLocationClient();
        } else {
            PermissionUtil.requestLocationPermission(this);
        }

        titleList = new ArrayList<String>() {{
            add(getString(R.string.massage_title));
            add(getString(R.string.beauty_title));
            add(getString(R.string.party_title));
//            add(getString(R.string.shop_title));
        }};

        fragmentList = new ArrayList<Fragment>() {{
            add(MassageFragment.newInstance());
            add(BeautyFragment.newInstance());
            add(PartyFragment.newInstance());
//            add(ShopFragment.newInstance());
        }};

        if(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedMassageCityID()<=0
                && HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedProductCityID()<=0) {
            if(!HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentCityName().isEmpty())
                tvCity.setText(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentCityName());
        }
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
                Bundle b = new Bundle();
                b.putInt(Constants.PARAM_TYPE, cityType);
                i.putExtras(b);
                startActivityForResult(i, SELECT_CITY_REQUEST);
            }
        });
        adapter = new PagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0 || position == 1) {
                    if (position == 0)
                        cityType = 1;
                    if (position == 1)
                        cityType = 0;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initLocationClient(){
        mLocationClient = new LocationClient(getContext());
        mLocationClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(true);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        startLocationService();
    }


    @Override
    public void onStart() {
        super.onStart();
        if(mLocationClient!=null)
            startLocationService();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mLocationClient!=null)
            mLocationClient.stop();
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if (mLocationClient != null)
                startLocationService();
        }
        else {
            if (mLocationClient != null)
                mLocationClient.stop();
        }
    }

    private void startLocationService(){
        if(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedCityName().isEmpty())
            mLocationClient.start();
        else {
            if(!HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedCityName().isEmpty())
                tvCity.setText(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedCityName());
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

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
                GlobalBus.getBus().post(new Events.Refresh());
            }
        }
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
                    if(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentCityID().isEmpty()){
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeCurrrentCityName(getString(R.string.filter_city));
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeCurrentCityID(Constants.ALL_CITY_ID+"");
                        tvCity.setText(R.string.filter_city);
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
            if(!location.getCity().equals(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getMTACurrentCityName())){
                Properties prop = new Properties();
                prop.setProperty("city", location.getCity());
                StatService.trackCustomKVEvent(getActivity(), Constants.MTA_ID_LOCATION, prop);
                HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeMTACurrrentCityName(location.getCity());
            }
            if(Tools.isUserLogin(getActivity().getApplicationContext())
                    && !location.getCity().equals(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getMTAUserCurrentCityName())){
                Properties prop = new Properties();
                prop.setProperty("city", location.getCity());
                StatService.trackCustomKVEvent(getActivity(), Constants.MTA_ID_USER_LOCATION, prop);
                HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeMTAUserCurrrentCityName(location.getCity());
            }
            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeCurrentProvinceName(location.getProvince());
            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeCurrentCityID(location.getCityCode());
            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeCurrrentCityName(location.getCity());
            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeLatitude(location.getLatitude());
            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeLongitude(location.getLongitude());
            if(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedProductCityID()<=0
                    &&HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedMassageCityID()<=0) {
                if (location.getCity() != null && !location.getCity().isEmpty())
                    tvCity.setText(location.getCity());
            }
            else {
                tvCity.setText(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedCityName());
            }
        }
    }
}

