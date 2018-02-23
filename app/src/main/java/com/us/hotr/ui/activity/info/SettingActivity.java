package com.us.hotr.ui.activity.info;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.WebViewActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.DataCleanManager;
import com.us.hotr.util.PermissionUtil;
import com.us.hotr.util.Tools;

/**
 * Created by Mloong on 2017/10/10.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    public static final int CODE_LOGOUT = 1;
    public static final int CODE_BACK = 2;

    private ConstraintLayout clUpdateInfo, clUpdatePhone, clChangePassword, clCleanCache, clFeedBack, clAboutUs, clBusiness, clCustomer, clCheckForUpdate;
    private TextView tvLogout, tvCache, tvPhoneNumber;

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
        tvPhoneNumber = (TextView) findViewById(R.id.tv_phone);

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
        tvPhoneNumber.setText(Constants.SUPPORT_LINE);
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
                Bundle b = new Bundle();
                b.putString(Constants.PARAM_TITLE, getString(R.string.business_partner));
                i.putExtras(b);
                startActivity(i);
                break;
            case R.id.cl_customer:
                if (PermissionUtil.hasCallPermission(this)) {
                    callPhoneNumber();
                } else {
                    PermissionUtil.requestCallPermission(this);
                }
                break;
            case R.id.cl_check_for_update:
                break;
            case R.id.tv_logout:
                TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(this);
                alertDialogBuilder.setMessage(getString(R.string.confirm_logout));
                alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HOTRSharePreference.getInstance(getApplicationContext()).storeUserID("");
                                HOTRSharePreference.getInstance(getApplicationContext()).storeUserInfo(null);
                                HOTRSharePreference.getInstance(getApplicationContext()).storeDefaultAddress(null);
                                HOTRSharePreference.getInstance(getApplicationContext()).storeSelectedMassageCityID(-1);
                                HOTRSharePreference.getInstance(getApplicationContext()).storeSelectedProductCityID(-1);
                                HOTRSharePreference.getInstance(getApplicationContext()).storeSelectedCityName("");
                                setResult(CODE_LOGOUT);
                                finish();
                                dialog.dismiss();
                            }
                        });
                alertDialogBuilder.setNegativeButton(getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialogBuilder.create().show();


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PermissionUtil.PERMISSIONS_REQUEST_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void callPhoneNumber()
    {
        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(this);
        alertDialogBuilder.setMessage(Constants.SUPPORT_LINE);
        alertDialogBuilder.setPositiveButton(getString(R.string.call),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + Constants.SUPPORT_LINE));
                        startActivity(callIntent);
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(CODE_BACK);
    }
}
