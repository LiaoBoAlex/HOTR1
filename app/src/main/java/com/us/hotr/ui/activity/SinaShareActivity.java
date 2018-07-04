package com.us.hotr.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.receiver.Share;
import com.us.hotr.ui.HOTRApplication;
import com.us.hotr.util.Tools;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Mloong on 2017/11/3.
 */

public class SinaShareActivity extends AppCompatActivity implements WbShareCallback {

    private WbShareHandler shareHandler;
    private Share share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        share = (Share) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        WbSdk.install(this,new AuthInfo(this, Constants.SINA_APP_ID, Constants.SINA_REDIRECT_URL, Constants.SINA_SCOPE));
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        sendMultiMessage();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent,this);
    }

    private void sendMultiMessage() {
        final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj();
        final WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title =share.getTitle();
        mediaObject.description = share.getDescription();
        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_post3);
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = share.getUrl();
        Glide.with(this).load(share.getImageUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mediaObject.setThumbImage(Bitmap.createScaledBitmap(resource, 120, 120, false));
                weiboMessage.mediaObject = mediaObject;
                shareHandler.shareMessage(weiboMessage, false);

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                weiboMessage.mediaObject = mediaObject;
                shareHandler.shareMessage(weiboMessage, false);
            }
        });
    }

    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = share.getSinaContent()+getString(R.string.at_hotr);
        return textObject;
    }

    @Override
    public void onWbShareSuccess() {
        Tools.Toast(this, getString(R.string.sina_share_success));
        GlobalBus.getBus().post(new Events.CreateVoucher());
        finish();
    }

    @Override
    public void onWbShareCancel() {
        Tools.Toast(this, getString(R.string.sina_share_cancel));
        finish();
    }

    @Override
    public void onWbShareFail() {
        Tools.Toast(this, getString(R.string.sina_share_fail));
        finish();
    }
}
