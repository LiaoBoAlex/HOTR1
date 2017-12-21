package com.us.hotr.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.util.Tools;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Mloong on 2017/11/3.
 */

public class SinaShareActivity extends AppCompatActivity implements WbShareCallback {

    private WbShareHandler shareHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj();
        weiboMessage.imageObject = getImageObj();
//        weiboMessage.mediaObject = getWebpageObj();
//        weiboMessage.multiImageObject = getMultiImageObject();
//        weiboMessage.videoSourceObject = getVideoObject();
        shareHandler.shareMessage(weiboMessage, false);
    }

    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "content...http://news.sina.com.cn/c/2013-10-22/021928494669.shtml";
        textObject.title = "title";
        textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_post3);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title ="测试title";
        mediaObject.description = "测试描述";
        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_share_weibo);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "http://news.sina.com.cn/c/2013-10-22/021928494669.shtml";
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    private MultiImageObject getMultiImageObject(){
        MultiImageObject multiImageObject = new MultiImageObject();
        //pathList设置的是本地本件的路径,并且是当前应用可以访问的路径，现在不支持网络路径（多图分享依靠微博最新版本的支持，所以当分享到低版本的微博应用时，多图分享失效
        // 可以通过WbSdk.hasSupportMultiImage 方法判断是否支持多图分享,h5分享微博暂时不支持多图）多图分享接入程序必须有文件读写权限，否则会造成分享失败
        ArrayList<Uri> pathList = new ArrayList<Uri>();
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/aaa.png")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/bbbb.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/ccc.JPG")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/ddd.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/fff.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/ggg.JPG")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/eee.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/hhhh.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/kkk.JPG")));
        multiImageObject.setImageList(pathList);
        return multiImageObject;
    }

    private VideoSourceObject getVideoObject(){
        //获取视频
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        videoSourceObject.videoPath = Uri.fromFile(new File(getExternalFilesDir(null)+"/eeee.mp4"));
        return videoSourceObject;
    }


    @Override
    public void onWbShareSuccess() {
        Tools.Toast(this, getString(R.string.sina_share_success));
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
