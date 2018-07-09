package com.us.hotr.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.us.hotr.R;

public class HotrHeaderView extends RelativeLayout {
    private CircularProgressView progressView;
    private boolean isLoading = false;

    public HotrHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public HotrHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_hotr_header, this);
        progressView = (CircularProgressView) findViewById(R.id.progressBar);
    }

    public void startLoading() {
        if(!isLoading) {
            progressView.startAnimation();
            isLoading = true;
        }
    }

    public void stopLoading(){
        progressView.stopAnimation();
        isLoading = false;
    }
}
