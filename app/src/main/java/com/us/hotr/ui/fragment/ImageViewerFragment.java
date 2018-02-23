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

/**
 * Created by Mloong on 2017/9/22.
 */

public class ImageViewerFragment extends Fragment {

    private PhotoView mPhotoView;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_viewer, container, false);
    }

    public static ImageViewerFragment newInstance(String url) {
        ImageViewerFragment imageViewerFragment = new ImageViewerFragment();
        Bundle b = new Bundle();
        b.putString(Constants.PARAM_DATA, url);
        imageViewerFragment.setArguments(b);
        return imageViewerFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        url = getArguments().getString(Constants.PARAM_DATA);
        mPhotoView = (PhotoView) view.findViewById(R.id.photo_view);
        Glide.with(this).load(url).skipMemoryCache(true).into(mPhotoView);
    }

    @Override
    public void onStop() {
        super.onStop();
        Drawable drawable = mPhotoView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
    }
}
