package com.us.hotr.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.beauty.CaseActivity;
import com.us.hotr.ui.activity.beauty.ProductActivity;
import com.us.hotr.ui.activity.massage.MasseurActivity;
import com.us.hotr.ui.activity.party.PartyActivity;
import com.us.hotr.ui.dialog.OneButtonDialog;
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
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);

        mWebview.setWebViewClient(new CustomWebViewClient());
        mWebview.setWebChromeClient(new MyWebChromeClient());
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
            Uri uri = Uri.parse(url);
            String node= uri.getQueryParameter("node");
            String id= uri.getQueryParameter("id");
            String type= uri.getQueryParameter("type");

            if(node!=null && node.equals("1") && id != null && type != null){
                switch (Integer.parseInt(type)){
                    case 1:
                        Intent i = new Intent(WebViewActivity.this, ProductActivity.class);
                        Bundle b = new Bundle();
                        b.putLong(Constants.PARAM_ID, Long.parseLong(id));
                        i.putExtras(b);
                        startActivity(i);
                        break;
                    case 2:
                        i =new Intent(WebViewActivity.this, MasseurActivity.class);
                        b = new Bundle();
                        b.putLong(Constants.PARAM_ID, Long.parseLong(id));
                        i.putExtras(b);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(WebViewActivity.this, PartyActivity.class);
                        b = new Bundle();
                        b.putLong(Constants.PARAM_ID, Long.parseLong(id));
                        i.putExtras(b);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(WebViewActivity.this, CaseActivity.class);
                        b = new Bundle();
                        b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                        b.putLong(Constants.PARAM_ID, Long.parseLong(id));
                        i.putExtras(b);
                        startActivity(i);
                        break;
                }
                return true;
            }else

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog,
                                      boolean userGesture, Message resultMsg) {
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        /**
         * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            OneButtonDialog.Builder alertDialogBuilder = new OneButtonDialog.Builder(WebViewActivity.this);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                            dialog.dismiss();
                        }
                    });
            alertDialogBuilder.create().show();
            return  true;
        }

        public boolean onJsBeforeUnload(WebView view, String url,
                                        String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        /**
         * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            OneButtonDialog.Builder alertDialogBuilder = new OneButtonDialog.Builder(WebViewActivity.this);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                            dialog.dismiss();
                        }
                    });
            alertDialogBuilder.create().show();
            return  true;
        }

        /**
         * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
         * window.prompt('请输入您的域名地址', '618119.com');
         */
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, final JsPromptResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle("对话框").setMessage(message);

            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            builder.setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm(et.getText().toString());
                        }

                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                    Log.v("onJsPrompt", "keyCode==" + keyCode + "event="+ event);
                    return true;
                }
            });

            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
            // return super.onJsPrompt(view, url, message, defaultValue,
            // result);
        }

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

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
        }
    }
}
