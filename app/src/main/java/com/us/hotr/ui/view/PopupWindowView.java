package com.us.hotr.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.R;

/**
 * Created by liaobo on 2018/1/29.
 */

public class PopupWindowView extends LinearLayout {

    private TextView tvName, tvAddress;
    private OnClickListener listener;

    public PopupWindowView(Context context) {
        super(context);
        init();
    }

    public PopupWindowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_map_popup, this);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvAddress = (TextView) findViewById(R.id.tv_place);
    }

    public void setData(String name, String address, View.OnClickListener listener){
        tvName.setText(name);
        tvAddress.setText(address);
        setOnClickListener(listener);
    }
}
