package com.us.hotr.ui.activity.info;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.WebViewActivity;

/**
 * Created by Mloong on 2017/10/13.
 */

public class AboutUsActivity extends BaseActivity {

    private TextView tvVersion;
    private ConstraintLayout clComment, clContract;
    @Override
    protected int getLayout() {
        return R.layout.about_us_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.about_us);
        initStaticView();
    }

    private void initStaticView(){
        tvVersion = (TextView) findViewById(R.id.tv_version);
        clComment = (ConstraintLayout) findViewById(R.id.cl_comment);
        clContract = (ConstraintLayout) findViewById(R.id.cl_contract);

//        try {
//            PackageInfo info = this.getPackageManager().getPackageInfo(
//                    this.getPackageName(), 0);
//            tvVersion.setText(info.versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        clComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        clContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AboutUsActivity.this, WebViewActivity.class);
                Bundle b = new Bundle();
                b.putString(Constants.PARAM_TITLE, getString(R.string.hotr_contract));
                i.putExtras(b);
                startActivity(i);
            }
        });
    }
}
