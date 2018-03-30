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

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.HOTRApplication;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberWithFinishListener;

import java.io.File;

/**
 * Created by Mloong on 2017/11/2.
 */

public class SplashActivity extends AppCompatActivity {
    private ImageView ivImage;
    private VideoView vvVideo;
    private boolean isVideoPlaying = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initStaticView();
        getVideoUrl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isVideoPlaying)
            startActivity();
    }

    private void playVideo(final boolean isVideoDownloaded, final String videoPath){
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
                            isVideoPlaying = false;
                            startActivity();
                        }
                    });

                    vvVideo.setVisibility(View.VISIBLE);
                    Uri videoUri = Uri.parse(videoPath);
                    vvVideo.setVideoURI(videoUri);
                    vvVideo.start();
                    isVideoPlaying = true;
                }
                else{
                    startActivity();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivImage.startAnimation(alphaAnimation);
    }

    private void downloadVideo(String url, final String videoPath){
        SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<Boolean>() {
            @Override
            public void onNext(Boolean result) {
                playVideo(true, videoPath);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                playVideo(false, videoPath);

            }
        };
        ServiceClient.getInstance().downloadFileWithDynamicUrlSync(new SilentSubscriber(mListener, this, null),
                url, videoPath);
    }

    private void initStaticView(){
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);

        ivImage = (ImageView) findViewById(R.id.iv_image);
        vvVideo = (VideoView) findViewById(R.id.vv_video);
    }

    private void getVideoUrl(){
        SubscriberWithFinishListener mListener = new SubscriberWithFinishListener<String>() {
            @Override
            public void onNext(String result) {
                String[] s = result.split("/");
                String fileName = s[s.length-1];
                File f = new File(Environment.getExternalStorageDirectory() + "/DCIM" + File.separator + "HOTR");
                if(!f.exists())
                    f.mkdir();
                String videoPath = Environment.getExternalStorageDirectory() + "/DCIM" + File.separator + "HOTR" + File.separator + fileName;

                f = new File(videoPath);
                if(f.exists())
                    playVideo(true, videoPath);
                else
                    downloadVideo(result, videoPath);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                playVideo(false, "");
            }
        };
        ServiceClient.getInstance().getStartVideoUrl(mListener);

    }

    private void startActivity(){
        if(!HOTRApplication.isMaintaince) {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            Bundle b = new Bundle();
            b.putBoolean(Constants.PARAM_DATA, true);
            i.putExtras(b);
            startActivity(i);
            SplashActivity.this.finish();
        }
    }
}
