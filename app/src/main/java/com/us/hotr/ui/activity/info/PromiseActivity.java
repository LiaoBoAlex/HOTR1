package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Info;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.webservice.response.GetProductDetailResponse;

/**
 * Created by Mloong on 2017/10/13.
 */

public class PromiseActivity extends BaseActivity {

    private GetProductDetailResponse.Promise promise;
    private TextView tvTitle;
    private TextView tvContent;

    @Override
    protected int getLayout() {
        return R.layout.activity_promise;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.promise_title);
        promise = (GetProductDetailResponse.Promise)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        initStaticView();
    }

    private void initStaticView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);

        tvTitle.setText(promise.getPromise_title());
        tvContent.setText(promise.getPromise_content());
    }
}
