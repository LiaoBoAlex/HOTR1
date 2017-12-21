package com.us.hotr.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.us.hotr.R;
import com.us.hotr.ui.fragment.info.VoucherFragment;

/**
 * Created by Mloong on 2017/10/25.
 */

public class AvailableVoucherActivity extends BaseActivity {

    private ConstraintLayout clVoucher;
    private ImageView ivVoucher;
    private VoucherFragment mFragment;

    private boolean isUseVoucher = true;
    @Override
    protected int getLayout() {
        return R.layout.activity_available_voucher;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.voucher);
        initStaticView();
    }

    private void initStaticView(){
        clVoucher = (ConstraintLayout) findViewById(R.id.cl_voucher);
        ivVoucher = (ImageView) findViewById(R.id.iv_voucher);

        mFragment = new VoucherFragment().newInstance(VoucherFragment.TYPE_VALID);
        mFragment.setEnableClick(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();

        clVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUseVoucher = !isUseVoucher;
                if(isUseVoucher) {
                    ivVoucher.setImageResource(R.mipmap.ic_massage_clicked);
                    mFragment.setEnableClick(true);
                }else {
                    ivVoucher.setImageResource(R.mipmap.ic_massage_click);
                    mFragment.setEnableClick(false);
                }
            }
        });
    }
}
