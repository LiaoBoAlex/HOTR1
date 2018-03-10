package com.us.hotr.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.us.hotr.Constants;
import com.us.hotr.R;

import java.io.File;
import java.io.Serializable;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by Mloong on 2017/9/22.
 */

public class ImageViewerFragment extends Fragment {

    private PhotoView mPhotoView;
    private String url;
    private int messageId;
    private int type;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_viewer, container, false);
    }

    public static ImageViewerFragment newInstance(String url, int type) {
        ImageViewerFragment imageViewerFragment = new ImageViewerFragment();
        Bundle b = new Bundle();
        b.putString(Constants.PARAM_DATA, url);
        b.putInt(Constants.PARAM_TYPE, type);
        imageViewerFragment.setArguments(b);
        return imageViewerFragment;
    }

    public static ImageViewerFragment newInstance(int messageId, String userId, int type) {
        ImageViewerFragment imageViewerFragment = new ImageViewerFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_DATA, messageId);
        b.putInt(Constants.PARAM_TYPE, type);
        b.putString(Constants.PARAM_NAME, userId);
        imageViewerFragment.setArguments(b);
        return imageViewerFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt(Constants.PARAM_TYPE);
        if(type == Constants.TYPE_CHAT) {
            messageId = getArguments().getInt(Constants.PARAM_DATA);
            userId  = getArguments().getString(Constants.PARAM_NAME);
        }
        else
            url = getArguments().getString(Constants.PARAM_DATA);
        mPhotoView = (PhotoView) view.findViewById(R.id.photo_view);
        if(type == Constants.TYPE_CHAT){
            Message message = JMessageClient.getSingleConversation(userId).getMessage(messageId);
            ImageContent imageContent = (ImageContent) message.getContent();
            if(imageContent.getLocalPath()!=null && !imageContent.getLocalPath().isEmpty() && new File(imageContent.getLocalPath()).exists()){
                Glide.with(this).load(new File(imageContent.getLocalPath())).into(mPhotoView);
            }else if(imageContent.getLocalThumbnailPath()!=null && !imageContent.getLocalThumbnailPath().isEmpty() && new File(imageContent.getLocalThumbnailPath()).exists()){
                Glide.with(this).load(new File(imageContent.getLocalThumbnailPath())).into(mPhotoView);
                imageContent.downloadOriginImage(message, new DownloadCompletionCallback() {
                    @Override
                    public void onComplete(int status, String desc, final File file) {
                        if(status == 0){
                            Glide.with(ImageViewerFragment.this).load(file).into(mPhotoView);
                        }
                    }
                });
            }
        }
        else
            Glide.with(this).load(url).skipMemoryCache(true).into(mPhotoView);
    }

    @Override
    public void onStop() {
        super.onStop();
        Drawable drawable = mPhotoView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if(bitmap!=null)
                bitmap.recycle();
        }
    }
}
