package com.us.hotr.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.dialog.MapDialogFragment;
import com.us.hotr.ui.view.PopupWindowView;
import com.us.hotr.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaobo on 2018/1/27.
 */

public class MapViewActivity extends BaseActivity{
    private MapView mMapView;
    private ImageView ivCenter;
    private BaiduMap mBaiduMap;

    private LatLng targetLocation, currentLocation;
    private String mName, mAddress;
    private LocationClient mLocationClient = null;
    private BDAbstractLocationListener myListener = new MyLocationListener();
    private boolean isFirstLocation = true;
    private RoutePlanSearch mSearch;
    private boolean isPopupShown;

    @Override
    protected int getLayout() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        targetLocation = getIntent().getExtras().getParcelable(Constants.PARAM_DATA);
        mName = getIntent().getExtras().getString(Constants.PARAM_TITLE);
        mAddress = getIntent().getExtras().getString(Constants.PARAM_HOSPITAL_ID);
        setMyTitle(mName);
        initView();
        initMap();
        addStartMarker();
        showPopupWindow(true);
    }

    private void initView(){
        mMapView = (MapView) findViewById(R.id.map_view);
        ivCenter = (ImageView) findViewById(R.id.iv_center);
        ivCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerMyLocation();
            }
        });
    }

    private void initMap(){

        mBaiduMap = mMapView.getMap();

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(2000);
        option.setIsNeedAddress(false);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mBaiduMap.setMyLocationEnabled(true);
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                PolylineOptions polyline = null;
                List<LatLng> pointList = new ArrayList<>();
                for(int i=0;i<walkingRouteResult.getRouteLines().get(0).getAllStep().size();i++){
                    pointList.addAll(walkingRouteResult.getRouteLines().get(0).getAllStep().get(i).getWayPoints());
                }
                if (pointList.size() >=2 && pointList.size() <= 100000) {
                    polyline = new PolylineOptions().width(6)
                            .color(getResources().getColor(R.color.blue)).points(pointList);
                }
                mBaiduMap.addOverlay(polyline);
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                PolylineOptions polyline = null;
                List<LatLng> pointList = new ArrayList<>();
                if(drivingRouteResult!=null && drivingRouteResult.getRouteLines()!=null && drivingRouteResult.getRouteLines().size()>0) {
                    for (int i = 0; i < drivingRouteResult.getRouteLines().get(0).getAllStep().size(); i++) {
                        pointList.addAll(drivingRouteResult.getRouteLines().get(0).getAllStep().get(i).getWayPoints());
                    }
                    if (pointList.size() >= 2 && pointList.size() <= 100000) {
                        polyline = new PolylineOptions().width(6)
                                .color(getResources().getColor(R.color.blue)).points(pointList);
                    }
                    mBaiduMap.addOverlay(polyline);
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
    }

    private void centerMyLocation() {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(currentLocation);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void drawRoute(){
        PlanNode start = PlanNode.withLocation(targetLocation);
        PlanNode end = PlanNode.withLocation(currentLocation);
        if(DistanceUtil. getDistance(currentLocation, targetLocation)<1000)
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(start)
                    .to(end));
        else
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(start)
                    .to(end));
    }

    private void addStartMarker(){
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_end);
        OverlayOptions option = new MarkerOptions()
                .position(targetLocation)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(targetLocation).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void addEndMarker(){
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_start);
        OverlayOptions option = new MarkerOptions()
                .position(currentLocation)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(isPopupShown)
                    showPopupWindow(false);
                else
                    showPopupWindow(true);
                return true;
            }
        });
    }

    private void showPopupWindow(boolean value){
        if(value) {
            isPopupShown = true;
            PopupWindowView popupWindowView = new PopupWindowView(this);
            popupWindowView.setData(mName, mAddress, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Tools.isPackageInstalled(Constants.AUTONAVI_MAP_PACKAGE_NAME)
                            &&!Tools.isPackageInstalled(Constants.BAIDU_MAP_PACKAGE_NAME)
                            &&!Tools.isPackageInstalled(Constants.TENCENT_MAP_PACKAGE_NAME)){
                        Tools.Toast(MapViewActivity.this, getString(R.string.no_map_software));
                    }else
                        MapDialogFragment.newInstance(targetLocation, mName).show(getSupportFragmentManager(), "dialog");
                }
            });
            InfoWindow mInfoWindow = new InfoWindow(popupWindowView, targetLocation, -100);
            mBaiduMap.showInfoWindow(mInfoWindow);
        }else{
            isPopupShown = false;
            mBaiduMap.hideInfoWindow();
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if(isFirstLocation){
                isFirstLocation = false;
                addEndMarker();
                drawRoute();
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
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
}
