package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;

public class AuditActivity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_audit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(getIntent().getExtras().getString(Constants.PARAM_TITLE));
    }
}
