package com.us.hotr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.R;
import com.us.hotr.ui.activity.search.SearchHintActivity;
import com.us.hotr.ui.dialog.ShareDialogFragment;

/**
 * Created by Mloong on 2017/9/19.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected TextView tvTitle, tvBack;
    protected ImageView ivBack, ivSearch, ivShare, ivClose;
    protected Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tb_title);
        ivBack = (ImageView) findViewById(R.id.img_back);
        ivClose = (ImageView)findViewById(R.id.img_close);
        tvBack = (TextView) findViewById(R.id.tv_back);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivSearch = (ImageView) findViewById(R.id.img_search);
        if(tvBack!=null)
            tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        if(ivBack!=null)
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        if(ivClose!=null)
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        if(ivSearch!=null)
            ivSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BaseActivity.this, SearchHintActivity.class));
                }
            });
    }

    protected void showToolBar(boolean value){
        if(value)
            mToolBar.setVisibility(View.VISIBLE);
        else
            mToolBar.setVisibility(View.GONE);
    }

    protected void setBackButtonListener(View.OnClickListener listener){
        if(ivBack!=null)
            ivBack.setOnClickListener(listener);
    }

    protected void setSearchButtonListener(View.OnClickListener listener){
        if(ivSearch!=null)
            ivSearch.setOnClickListener(listener);
    }

    protected void setBackTextButtonListener(View.OnClickListener listener){
        if(tvBack!=null)
            tvBack.setOnClickListener(listener);
    }

    protected void setShareButtonListener(View.OnClickListener listener){
        if(ivShare!=null)
            ivShare.setOnClickListener(listener);
    }

    protected  void setMyTitle(String s){
        if(tvTitle!=null)
            tvTitle.setText(s);
    }

    protected  void setMyTitle(int i){
        if(tvTitle!=null)
            tvTitle.setText(i);
    }

    protected abstract int getLayout();

}
