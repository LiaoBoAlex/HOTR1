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
import com.us.hotr.storage.bean.Case;
import com.us.hotr.storage.bean.Doctor;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.beauty.ListActivity;
import com.us.hotr.ui.activity.beauty.ListWithSearchActivity;
import com.us.hotr.ui.dialog.TwoButtonDialog;
import com.us.hotr.util.Tools;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
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

    private Product selectedProduct;
    private Doctor selectedDoctor;
    private String photoBeforeFilePath, photoAfterFilePath;
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
                if(selectedProduct == null)
                    Tools.Toast(UploadCompareActivity1.this, getString(R.string.choose_product));
                else if(selectedDoctor == null)
                    Tools.Toast(UploadCompareActivity1.this, getString(R.string.choose_doctor));
                else{
                    Case aCase = new Case();
                    aCase.setProductId(selectedProduct.getProductId());
                    aCase.setProductName(selectedProduct.getProduct_name());
                    aCase.setDoctorId(selectedDoctor.getDoctor_id());
                    aCase.setDoctorName(selectedDoctor.getDoctor_name());
                    aCase.setHospitalId(selectedProduct.getHospital_id());
                    aCase.setHospitalName(selectedProduct.getHospital_name());
                    aCase.setProjectId(selectedProduct.getProject_id());
                    aCase.setProjectName(selectedProduct.getProject_name());
                    aCase.setContrastPhotoTitle(selectedProduct.getProduct_name()+getString(R.string.photo_before_after));
                    aCase.setFilePathBefore(photoBeforeFilePath);
                    aCase.setFilePathAfter(photoAfterFilePath);
                    Intent i = new Intent(UploadCompareActivity1.this, UploadCompareActivity2.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, aCase);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });

        clProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UploadCompareActivity1.this, ListActivity.class);
                Bundle b = new Bundle();
                b.putString(Constants.PARAM_TITLE, getString(R.string.product_bought));
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MY_PRODUCT);
                i.putExtras(b);
                startActivityForResult(i, 0);
            }
        });

        clDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UploadCompareActivity1.this, ListActivity.class);
                Bundle b = new Bundle();
                b.putString(Constants.PARAM_TITLE, getString(R.string.choose_doctor_title));
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_MY_DOCTOR);
                b.putLong(Constants.PARAM_HOSPITAL_ID, selectedProduct.getHospital_id());
                i.putExtras(b);
                startActivityForResult(i, 1);
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
                                photoBeforeFilePath = imageRadioResultEvent.getResult().getOriginalPath();
                                Bitmap bitmap = Tools.decodeFile(imageRadioResultEvent.getResult().getOriginalPath(), ivBefore.getWidth(), ivBefore.getHeight());
                                ivBefore.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                ivBefore.setImageBitmap(bitmap);
                                ivDeleteBefore.setVisibility(View.VISIBLE);
                            }
                        })
                        .openGallery();
                RxGalleryFinalApi.setImgSaveRxSDCard("HOTR");
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
                                photoAfterFilePath = imageRadioResultEvent.getResult().getOriginalPath();
                                Bitmap bitmap = Tools.decodeFile(imageRadioResultEvent.getResult().getOriginalPath(), ivAfter.getWidth(), ivAfter.getHeight());
                                ivAfter.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                ivAfter.setImageBitmap(bitmap);
                                ivDeleteAfter.setVisibility(View.VISIBLE);
                            }
                        })
                        .openGallery();
                RxGalleryFinalApi.setImgSaveRxSDCard("HOTR");
            }
        });
    }

    @Override
    public void onBackPressed() {
//        new CancelPostDialogFragment().show(getSupportFragmentManager(), "dialog");
        TwoButtonDialog.Builder alertDialogBuilder = new TwoButtonDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.cancel_draft));
        alertDialogBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UploadCompareActivity1.super.onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                selectedProduct = (Product) data.getExtras().getSerializable(Constants.PARAM_DATA);
                tvProduct.setText(getString(R.string.bracket_left)+selectedProduct.getProduct_name()+getString(R.string.bracket_right)+selectedProduct.getProduct_usp());
                selectedDoctor = new Doctor();
                selectedDoctor.setDoctor_id(selectedProduct.getDoctor_id());
                selectedDoctor.setDoctor_name(selectedProduct.getDoctor_name());
                tvDoctor.setText(selectedDoctor.getDoctor_name());
            }
        }
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                selectedDoctor = (Doctor)data.getExtras().getSerializable(Constants.PARAM_DATA);
                tvDoctor.setText(selectedDoctor.getDoctor_name());
            }
        }
    }
}
