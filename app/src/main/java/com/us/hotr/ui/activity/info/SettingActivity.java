package com.us.hotr.ui.activity.info;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.WebViewActivity;
import com.us.hotr.ui.activity.beauty.ProductActivity;
import com.us.hotr.util.DataCleanManager;
import com.us.hotr.util.Tools;

import java.io.File;

/**
 * Created by Mloong on 2017/10/10.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ConstraintLayout clUpdateInfo, clUpdatePhone, clChangePassword, clCleanCache, clFeedBack, clAboutUs, clBusiness, clCustomer, clCheckForUpdate;
    private TextView tvLogout, tvCache;
    private final int CALL_REQUEST = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.settings);
        initStaticView();
    }

    private void initStaticView() {
        clUpdateInfo = (ConstraintLayout) findViewById(R.id.cl_update_info);
        clUpdatePhone = (ConstraintLayout) findViewById(R.id.cl_phone);
        clChangePassword = (ConstraintLayout) findViewById(R.id.cl_password);
        clCleanCache = (ConstraintLayout) findViewById(R.id.cl_clean_cache);
        clFeedBack = (ConstraintLayout) findViewById(R.id.cl_feedback);
        clAboutUs = (ConstraintLayout) findViewById(R.id.cl_about_us);
        clBusiness = (ConstraintLayout) findViewById(R.id.cl_business);
        clCustomer = (ConstraintLayout) findViewById(R.id.cl_customer);
        clCheckForUpdate = (ConstraintLayout) findViewById(R.id.cl_check_for_update);
        tvLogout = (TextView) findViewById(R.id.tv_logout);
        tvCache = (TextView) findViewById(R.id.tv_cache);

        clUpdateInfo.setOnClickListener(this);
        clUpdatePhone.setOnClickListener(this);
        clChangePassword.setOnClickListener(this);
        clCleanCache.setOnClickListener(this);
        clFeedBack.setOnClickListener(this);
        clAboutUs.setOnClickListener(this);
        clBusiness.setOnClickListener(this);
        clCustomer.setOnClickListener(this);
        clCheckForUpdate.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvCache.setText(DataCleanManager.getTotalCacheSize(getBaseContext()));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cl_update_info:
                startActivity(new Intent(SettingActivity.this, EditInfoActivity.class));
                break;
            case R.id.cl_phone:
                startActivity(new Intent(SettingActivity.this, ChangePhoneNumberActivity.class));
                break;
            case R.id.cl_password:
                startActivity(new Intent(SettingActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.cl_clean_cache:
                if (DataCleanManager.clearAllCache(getBaseContext())){
                    tvCache.setText(DataCleanManager.getTotalCacheSize(getBaseContext()));
                    Tools.Toast(SettingActivity.this, getString(R.string.clear_cache_success));
                }

                break;
            case R.id.cl_feedback:
                startActivity(new Intent(SettingActivity.this, HelpAndFeedbackActivity.class));
                break;
            case R.id.cl_about_us:
                startActivity(new Intent(SettingActivity.this, AboutUsActivity.class));
                break;
            case R.id.cl_business:
                Intent i = new Intent(SettingActivity.this, WebViewActivity.class);
                i.putExtra(Constants.PARAM_TITLE, getString(R.string.business_partner));
                startActivity(i);
                break;
            case R.id.cl_customer:
                callPhoneNumber();
                break;
            case R.id.cl_check_for_update:
                break;
            case R.id.tv_logout:
                HOTRSharePreference.getInstance(getApplicationContext()).storeUserID("");
//                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
//                intent.putExtra(LoginActivity.PARAM_FROM_PAGE, 0);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                setResult(Activity.RESULT_OK);
                finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == CALL_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    callPhoneNumber();
                }

            } else {
                Tools.Toast(SettingActivity.this, "Call failed");
            }
        }
    }

    private void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + "123456"));
            startActivity(callIntent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
