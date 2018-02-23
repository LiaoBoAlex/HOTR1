package cn.finalteam.rxgalleryfinal.rxjob.job;

import android.content.Context;
import android.util.Log;

import java.io.File;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxjob.Job;
import cn.finalteam.rxgalleryfinal.utils.BitmapUtils;
import cn.finalteam.rxgalleryfinal.utils.MediaUtils;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/31 上午11:46
 */
public class ImageThmbnailJob implements Job {

    private final MediaBean mediaBean;
    private final Context context;
    private final JobFinishedListener listener;

    public ImageThmbnailJob(Context context, Params params, JobFinishedListener listener) {
        this.context = context;
        this.mediaBean = (MediaBean) params.getRequestData();
        this.listener = listener;
    }

    @Override
    public Result onRunJob() {
        String originalPath = mediaBean.getOriginalPath();
        File bigThumFile = MediaUtils.createThumbnailBigFileName(context, originalPath);
        File smallThumFile = MediaUtils.createThumbnailSmallFileName(context, originalPath);
        if (!bigThumFile.exists()) {
            BitmapUtils.createThumbnailBig(bigThumFile, originalPath);
        }
        if (!smallThumFile.exists()) {
            BitmapUtils.createThumbnailSmall(smallThumFile, originalPath);
        }
        Result result = Result.SUCCESS;
        result.setResultData(mediaBean);
        return result;
    }

    @Override
    public void onJobFinished() {
        listener.OnJobFinished();
    }

    public interface JobFinishedListener{
        void OnJobFinished();
    }

}
