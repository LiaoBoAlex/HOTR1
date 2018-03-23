package com.us.hotr.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.VideoView;

import com.us.hotr.R;
import com.us.hotr.storage.bean.Adv;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

import java.io.File;
import java.util.List;

/**
 * Created by Mloong on 2017/11/2.
 */

public class SplashActivity extends AppCompatActivity {
    private ImageView ivImage;
    private VideoView vvVideo;

    private String url, videoPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        url = "http://us-shop-2.oss-cn-beijing.aliyuncs.com/video/start_video/66.mp4";
        File f = new File(Environment.getExternalStorageDirectory() + "/DCIM" + File.separator + "HOTR");
        if(!f.exists())
            f.mkdir();
        videoPath = Environment.getExternalStorageDirectory() + "/DCIM" + File.separator + "HOTR" + File.separator + "welcome.mp4";

        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);

        ivImage = (ImageView) findViewById(R.id.iv_image);
        vvVideo = (VideoView) findViewById(R.id.vv_video);
        f = new File(videoPath);
        if(f.exists())
            playVideo(true);
        else
            downloadVideo();
    }

    private void playVideo(final boolean isVideoDownloaded){
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setStartOffset(1000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivImage.setVisibility(View.GONE);
                if(isVideoDownloaded) {
                    vvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    });

                    vvVideo.setVisibility(View.VISIBLE);
                    Uri videoUri = Uri.parse(videoPath);
                    vvVideo.setVideoURI(videoUri);
                    vvVideo.start();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivImage.startAnimation(alphaAnimation);
    }

    private void downloadVideo(){
        SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<Boolean>() {
            @Override
            public void onNext(Boolean result) {
                playVideo(true);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                playVideo(false);

            }
        };
            ServiceClient.getInstance().downloadFileWithDynamicUrlSync(new SilentSubscriber(mListener, this, null),
                    url, videoPath);
    }

    private void next(){
        SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<List<Adv>>() {
            @Override
            public void onNext(List<Adv> result) {
//                if(result!=null && result.size()>0){
//                    Intent i = new Intent(SplashActivity.this, ViewpagerActivity.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable(Constants.PARAM_DATA, new ArrayList<>(result));
//                    i.putExtras(b);
//                    startActivity(i);
//
//                }else
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        };
        ServiceClient.getInstance().getAdvList(new SilentSubscriber(mListener, this, null),
                Tools.getScreenWidth(this),
                Tools.getWindowHeight(this),
                1);

    }
}
