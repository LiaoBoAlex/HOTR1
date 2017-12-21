package com.us.hotr.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.us.hotr.Constants;
import com.us.hotr.R;

/**
 * Created by Mloong on 2017/10/13.
 */

public class WebViewActivity extends BaseActivity {

    private WebView mWebview;
    private ProgressBar mProgressBar;

    @Override
    protected int getLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(getIntent().getStringExtra(Constants.PARAM_TITLE));
        initStaticView();
    }

    private void initStaticView(){
        mWebview = (WebView) findViewById(R.id.wv_content);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
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
        String url="https://mp.weixin.qq.com/s?__biz=MjM5MDczMjM2MA==&mid=2652388593&idx=1&sn=7c08949e42d61de2f022ffcee552738a&chksm=bdacc5d68adb4cc0a038f92efca95acf17ef1facbc86553441e8f47dbb0316104b80b6379485&scene=0&key=bda634fb2c7300a3b6c583ff2fe7827c6ddb195c74ff4c744b765cd8a48e9f4e67360531050b8b7addce3f97d0b9440e5a0b6bc1948c635320267447fc4b8075deffdf61ea7ecc241bf9a9f120f378cd&ascene=0&uin=OTYwOTY3Njgw&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.6+build(15G31)&version=12010210&nettype=WIFI&fontScale=100&pass_ticket=kCRObwEpa%2BTF24xhAVuiq%2FBQ2Ki1t8IcSMer1q5hQg2vFO41c4RQRrTB236TDGFU";
        mWebview.loadUrl(url);
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
