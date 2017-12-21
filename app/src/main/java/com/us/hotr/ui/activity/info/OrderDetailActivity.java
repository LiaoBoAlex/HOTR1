package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;

/**
 * Created by Mloong on 2017/9/19.
 */

public class OrderDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.order_detail);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_order_detail;
    }

    private void initStaticView(){

    }
}
