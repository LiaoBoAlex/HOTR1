package com.us.hotr.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.util.PermissionUtil;
import com.us.hotr.util.Tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liaobo on 2018/3/8.
 */

public class CameraActivity extends AppCompatActivity{
    private final String IMAGE_STORE_FILE_NAME = "IMG_%s.jpg";
    private final String TAKE_URL_STORAGE_KEY = "take_url_storage_key";

    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        takePhoto();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Intent i = new Intent();
            Bundle b = new Bundle();
            b.putString(Constants.PARAM_DATA, mImagePath);
            i.putExtras(b);
            setResult(RESULT_OK, i);
            finish();
        }else
            finish();
    }

    private void takePhoto(){
        if (PermissionUtil.hasCameraPermission(this)) {
            openCamera();
        } else {
            PermissionUtil.requestCameraPermission(this);
        }
    }

    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(getPackageManager()) == null) {
            Tools.Toast(this, getString(cn.finalteam.rxgalleryfinal.R.string.gallery_device_camera_unable));
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String filename = String.format(IMAGE_STORE_FILE_NAME, dateFormat.format(new Date()));
        String mImageStoreDir = Environment.getExternalStorageDirectory()+"/DCIM/HOTR";
        File fileImagePath = new File(mImageStoreDir, filename);
        mImagePath = fileImagePath.getAbsolutePath();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagePath));
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, mImagePath);
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        captureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(captureIntent, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PermissionUtil.PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }else
            finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mImagePath)) {
            outState.putString(TAKE_URL_STORAGE_KEY, mImagePath);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        mImagePath = savedInstanceState.getString(TAKE_URL_STORAGE_KEY);
    }
}
