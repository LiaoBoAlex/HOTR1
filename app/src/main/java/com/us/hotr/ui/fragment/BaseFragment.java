package com.us.hotr.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.R;

/**
 * Created by Mloong on 2017/9/19.
 */

public class BaseFragment extends Fragment {

    private TextView tvTitle;
    protected ImageView ivBack;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = (TextView) view.findViewById(R.id.tb_title);
        ivBack = (ImageView) view.findViewById(R.id.img_back);

//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
    }

    protected void setBackButtonListener(View.OnClickListener listener){
        if(ivBack!=null)
            ivBack.setOnClickListener(listener);
    }

    protected  void setMyTitle(String s){
        if(tvTitle!=null)
            tvTitle.setText(s);
    }

    protected  void setMyTitle(int i){
        if(tvTitle!=null)
            tvTitle.setText(i);
    }
}
