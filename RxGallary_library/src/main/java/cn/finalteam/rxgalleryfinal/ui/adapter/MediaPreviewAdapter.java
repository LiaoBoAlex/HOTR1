package cn.finalteam.rxgalleryfinal.ui.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxjob.Job;
import cn.finalteam.rxgalleryfinal.rxjob.RxJob;
import cn.finalteam.rxgalleryfinal.rxjob.job.ImageThmbnailJob;
import cn.finalteam.rxgalleryfinal.rxjob.job.ImageThmbnailJobCreate;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import uk.co.senab.photoview.PhotoView;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/21 下午10:12
 */
public class MediaPreviewAdapter extends RecyclingPagerAdapter {

    private final List<MediaBean> mMediaList;
    private final Configuration mConfiguration;
    private final Drawable mDefaultImage;
    private final int mScreenWidth;
    private final int mScreenHeight;
    private final int mPageColor;
    private final MediaActivity mMediaActivity;

    public MediaPreviewAdapter(MediaActivity mediaActivity,
                               List<MediaBean> list,
                               int screenWidth,
                               int screenHeight,
                               Configuration configuration,
                               int pageColor,
                               Drawable drawable) {
        this.mMediaActivity = mediaActivity;
        this.mMediaList = list;
        this.mScreenWidth = screenWidth;
        this.mScreenHeight = screenHeight;
        this.mConfiguration = configuration;
        this.mPageColor = pageColor;
        this.mDefaultImage = drawable;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        MediaBean mediaBean = mMediaList.get(position);
        if (convertView == null) {
            convertView = View.inflate(container.getContext(), R.layout.gallery_media_image_preview_item, null);
        }
        PhotoView ivImage = (PhotoView) convertView.findViewById(R.id.iv_media_image);
        ivImage.setBackgroundColor(mPageColor);
        ivImage.setImageResource(R.drawable.gallery_default_image);
//        if (mediaBean.getWidth() > 1200 || mediaBean.getHeight() > 1200){
//            if (!new File(mediaBean.getThumbnailBigPath()).exists()) {
//                Job job = new ImageThmbnailJobCreate(mMediaActivity, mediaBean, new ImageThmbnailJob.JobFinishedListener() {
//                    @Override
//                    public void OnJobFinished() {
//                        String path = mediaBean.getThumbnailBigPath();
//                        if (TextUtils.isEmpty(path)) {
//                            path = mediaBean.getOriginalPath();
//                        }
//                        mConfiguration.getImageLoader().displayImage(container.getContext(), path, ivImage, mDefaultImage, mConfiguration.getImageConfig(),
//                                false, mConfiguration.isPlayGif(), mScreenWidth, mScreenHeight, mediaBean.getOrientation());
//                    }
//                }).create();
//                RxJob.getDefault().addJob(job);
//            }else {
//                mConfiguration.getImageLoader().displayImage(container.getContext(), mediaBean.getThumbnailBigPath(), ivImage, mDefaultImage, mConfiguration.getImageConfig(),
//                        false, mConfiguration.isPlayGif(), mScreenWidth, mScreenHeight, mediaBean.getOrientation());
//            }
//        }else{
            mConfiguration.getImageLoader().displayImage(container.getContext(), mediaBean.getOriginalPath(), ivImage, mDefaultImage, mConfiguration.getImageConfig(),
                    false, mConfiguration.isPlayGif(), mScreenWidth, mScreenHeight, mediaBean.getOrientation());
//        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mMediaList.size();
    }
}
