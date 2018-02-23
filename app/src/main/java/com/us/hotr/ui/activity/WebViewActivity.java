package com.us.hotr.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.util.Tools;

/**
 * Created by Mloong on 2017/10/13.
 */

public class WebViewActivity extends BaseActivity {
    public static final int TYPE_DATA = 1;
    public static final int TYPE_URL = 2;

    private WebView mWebview;
    private ProgressBar mProgressBar;
    private ImageView ivBack1;
    private String content;
    private int type = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        content = getIntent().getExtras().getString(Constants.PARAM_DATA);
        initStaticView();
    }

    private void initStaticView(){
        mWebview = (WebView) findViewById(R.id.wv_content);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        ivBack1 = (ImageView) findViewById(R.id.iv_back);
        String title = getIntent().getExtras().getString(Constants.PARAM_TITLE);
        if(title!=null) {
            setMyTitle(title);
            ivBack1.setVisibility(View.GONE);
        }
        else
            showToolBar(false);
        ivBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mProgressBar.setMax(100);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        mWebview.setWebViewClient(new CustomWebViewClient());

        mWebview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100 && mProgressBar.getVisibility() == ProgressBar.GONE) {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
                mProgressBar.setProgress(0);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });
        mWebview.clearFormData();
        mWebview.clearCache(true);
        if(type == TYPE_DATA)
            mWebview.loadData(Tools.getHtmlData(content), "text/html; charset=UTF-8", null);
        if(type == TYPE_URL)
            mWebview.loadUrl(content);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }
}
