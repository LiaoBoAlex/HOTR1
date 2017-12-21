package com.us.hotr.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.us.hotr.R;

/**
 * Created by Mloong on 2017/9/13.
 */

public class PaySuccessActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.pay_order_title);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_pay_success;
    }
}

