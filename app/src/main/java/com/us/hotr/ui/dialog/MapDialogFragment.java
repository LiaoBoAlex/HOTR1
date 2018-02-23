package com.us.hotr.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.util.Tools;

import java.io.File;

import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

/**
 * Created by liaobo on 2018/1/29.
 */

public class MapDialogFragment extends BottomSheetDialogFragment {
    TextView tvAutonavi, tvBaidu, tvTencent;
    private BottomSheetBehavior mBehavior;
    private boolean mapFound = false;
    private LatLng mLatlng;
    private String mName;

    public static MapDialogFragment newInstance(LatLng mLatlng, String name) {
        MapDialogFragment f = new MapDialogFragment();
        Bundle b = new Bundle();
        b.putParcelable(Constants.PARAM_DATA, mLatlng);
        b.putString(Constants.PARAM_TITLE, name);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLatlng = getArguments().getParcelable(Constants.PARAM_DATA);
        mName = getArguments().getString(Constants.PARAM_TITLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialog_map, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        tvAutonavi = (TextView) view.findViewById(R.id.tv_autonavi);
        tvBaidu = (TextView) view.findViewById(R.id.tv_baidu);
        tvTencent = (TextView) view.findViewById(R.id.tv_tencent);

        if(!Tools.isPackageInstalled(Constants.AUTONAVI_MAP_PACKAGE_NAME))
            tvAutonavi.setVisibility(View.GONE);
        if(!Tools.isPackageInstalled(Constants.BAIDU_MAP_PACKAGE_NAME))
            tvBaidu.setVisibility(View.GONE);
        if(!Tools.isPackageInstalled(Constants.TENCENT_MAP_PACKAGE_NAME))
            tvTencent.setVisibility(View.GONE);

        tvAutonavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                Intent naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat="+ mLatlng.latitude +"&dlon="+ mLatlng.longitude+"&dname="+mName+"&dev=0&t=2"));
                startActivity(naviIntent);
            }
        });
        tvBaidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                Intent naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("baidumap://map/direction?destination=name:"+mName+"|latlng:"+mLatlng.latitude+","+mLatlng.longitude));


                startActivity(naviIntent);
            }
        });
        tvTencent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                Intent naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to="+mName+"&tocoord=" + mLatlng.latitude + "," + mLatlng.longitude + "&policy=0&referer=appName"));
                startActivity(naviIntent);

            }
        });

        return dialog;

    }

    @Override
    public void onStart()
    {
        super.onStart();
        mBehavior.setState(STATE_EXPANDED);
    }

//    public static boolean isPackageInstalled(Context mContext, String packagename) {
//        PackageInfo packageInfo = null;
//        try {
//            packageInfo = mContext.getPackageManager().getPackageInfo(packagename, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            packageInfo = null;
//            e.printStackTrace();
//        } finally {
//            return packageInfo == null ? false : true;
//        }
//    }

    public void doclick(View v)
    {
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}
