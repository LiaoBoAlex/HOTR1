package com.us.hotr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.Voucher;
import com.us.hotr.ui.fragment.info.VoucherListFragment;
import com.us.hotr.webservice.request.AvailableVoucherRequest;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Mloong on 2017/10/25.
 */

public class AvailableVoucherActivity extends BaseActivity {
    private ConstraintLayout clVoucher;
    private ImageView ivVoucher;
    private VoucherListFragment mFragment;

    private boolean isUseVoucher = false;
    private AvailableVoucherRequest request;
    private Voucher selectedVoucher = null;

    @Override
    protected int getLayout() {
        return R.layout.activity_available_voucher;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.voucher);
        if(getIntent().getExtras()!=null) {
            request = (AvailableVoucherRequest) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
            selectedVoucher = (Voucher) getIntent().getExtras().getSerializable(Constants.PARAM_TYPE);
            if(selectedVoucher!=null)
                isUseVoucher = true;
        }
        initStaticView();
    }

    private void initStaticView() {
        clVoucher = (ConstraintLayout) findViewById(R.id.cl_voucher);
        ivVoucher = (ImageView) findViewById(R.id.iv_voucher);

        mFragment = new VoucherListFragment().newInstance(VoucherListFragment.TYPE_AVAILABLE, request, selectedVoucher);
        mFragment.setEnableClick(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();
        if(isUseVoucher)
            ivVoucher.setImageResource(R.mipmap.ic_massage_click);
        else
            ivVoucher.setImageResource(R.mipmap.ic_massage_clicked);

        clVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUseVoucher = !isUseVoucher;
                if (isUseVoucher) {
                    ivVoucher.setImageResource(R.mipmap.ic_massage_click);
                    mFragment.setEnableClick(true);
                } else {
                    ivVoucher.setImageResource(R.mipmap.ic_massage_clicked);
                    mFragment.setEnableClick(false);
                    selectedVoucher = null;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getMessage(Events.VoucherSelected voucherSelected) {
        selectedVoucher = voucherSelected.getVoucher();
        isUseVoucher = true;
        ivVoucher.setImageResource(R.mipmap.ic_massage_click);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        if(selectedVoucher!=null) {
            Bundle b = new Bundle();
            b.putSerializable(Constants.PARAM_DATA, selectedVoucher);
            i.putExtras(b);
        }
        setResult(RESULT_OK, i);
        super.onBackPressed();
    }
}
