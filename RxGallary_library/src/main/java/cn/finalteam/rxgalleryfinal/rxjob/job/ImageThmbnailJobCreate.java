package cn.finalteam.rxgalleryfinal.rxjob.job;

import android.content.Context;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxjob.Job;
import cn.finalteam.rxgalleryfinal.rxjob.JobCreator;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/31 上午11:46
 */
public class ImageThmbnailJobCreate implements JobCreator {

    private final MediaBean mediaBean;
    private final Context context;
    private final ImageThmbnailJob.JobFinishedListener listener;

    public ImageThmbnailJobCreate(Context context, MediaBean mediaBean, ImageThmbnailJob.JobFinishedListener listener) {
        this.context = context;
        this.mediaBean = mediaBean;
        this.listener = listener;
    }

    @Override
    public Job create() {
        Job.Params params = new Job.Params(mediaBean.getOriginalPath(), mediaBean);
        return new ImageThmbnailJob(context, params, listener);
    }
}
