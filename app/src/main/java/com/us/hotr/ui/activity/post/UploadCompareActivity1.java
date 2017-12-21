package com.us.hotr.ui.activity.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.beauty.ListWithSearchActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.Tools;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

/**
 * Created by Mloong on 2017/9/27.
 */

public class UploadCompareActivity1 extends BaseActivity {

    private TextView tvNext, tvProduct, tvHospital, tvDoctor;
    private ConstraintLayout clProduct, clDoctor;
    private ImageView ivBefore, ivAfter, ivDeleteBefore, ivDeleteAfter;
    @Override
    protected int getLayout() {
        return R.layout.activity_upload_compare1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.upload_compare);
        initStaticView();
    }

    private void initStaticView(){
        tvNext = (TextView) findViewById(R.id.tv_edit);
        tvProduct = (TextView) findViewById(R.id.tv_product);
        tvHospital = (TextView) findViewById(R.id.tv_hospital);
        tvDoctor = (TextView) findViewById(R.id.tv_doctor);
        clProduct = (ConstraintLayout) findViewById(R.id.cl_product);
        clDoctor = (ConstraintLayout) findViewById(R.id.cl_doctor);
        ivBefore = (ImageView) findViewById(R.id.iv_before);
        ivAfter = (ImageView) findViewById(R.id.iv_after);
        ivDeleteBefore = (ImageView) findViewById(R.id.iv_delete_before);
        ivDeleteAfter = (ImageView) findViewById(R.id.iv_delete_after);


        tvNext.setText(R.string.next);
        tvNext.setTextColor(getResources().getColor(R.color.text_grey2));

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadCompareActivity1.this, UploadCompareActivity2.class));
            }
        });

        clProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UploadCompareActivity1.this, ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, R.string.product_bought);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MY_PRODUCT);
                startActivity(i);
            }
        });

        clDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UploadCompareActivity1.this, ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, R.string.choose_doctor_title);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_DOCTOR);
                startActivity(i);
            }
        });

        ivDeleteBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivDeleteBefore.setVisibility(View.GONE);
                ivBefore.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivBefore.setImageResource(R.mipmap.holder_compare_before);
            }
        });

        ivDeleteAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivDeleteAfter.setVisibility(View.GONE);
                ivAfter.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivAfter.setImageResource(R.mipmap.holder_compare_after);
            }
        });

        ivBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxGalleryFinal
                        .with(UploadCompareActivity1.this)
                        .cropHideBottomControls(true)
                        .image()
                        .radio()
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                Bitmap bitmap = Tools.decodeFile(imageRadioResultEvent.getResult().getOriginalPath(), ivBefore.getWidth(), ivBefore.getHeight());
                                ivBefore.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                ivBefore.setImageBitmap(bitmap);
                                ivDeleteBefore.setVisibility(View.VISIBLE);
                            }
                        })
                        .openGallery();
            }
        });

        ivAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxGalleryFinal
                        .with(UploadCompareActivity1.this)
                        .cropHideBottomControls(true)
                        .image()
                        .radio()
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                Bitmap bitmap = Tools.decodeFile(imageRadioResultEvent.getResult().getOriginalPath(), ivAfter.getWidth(), ivAfter.getHeight());
                                ivAfter.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                ivAfter.setImageBitmap(bitmap);
                                ivDeleteAfter.setVisibility(View.VISIBLE);
                            }
                        })
                        .openGallery();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        new CancelPostDialogFragment().show(getSupportFragmentManager(), "dialog");
        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.cancel_draft));
        alertDialogBuilder.setPositiveButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UploadCompareActivity1.super.onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.create().show();
    }
}
