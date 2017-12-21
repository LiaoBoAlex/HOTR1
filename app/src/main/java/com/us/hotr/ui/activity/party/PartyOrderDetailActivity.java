package com.us.hotr.ui.activity.party;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;

/**
 * Created by Mloong on 2017/10/24.
 */

public class PartyOrderDetailActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.order_detail);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_party_order_detail;
    }

    private void initStaticView(){

    }
}
