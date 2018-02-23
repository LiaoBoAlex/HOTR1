package com.us.hotr.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;


public class PermissionUtil {
    public static final int PERMISSIONS_REQUEST_CONTACTS = 10;
    public static final int PERMISSIONS_REQUEST_STORAGE = 20;
    public static final int PERMISSIONS_REQUEST_CAMERA = 30;
    public static final int PERMISSIONS_REQUEST_CALL = 40;
    public static final int PERMISSIONS_REQUEST_READ_SMS = 50;
    public static final int PERMISSIONS_REQUEST_LOCATION = 60;
    public static final int PERMISSIONS_REQUEST_SEND_SMS = 70;
    public static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 80;
    public static final int PERMISSIONS_REQUEST_CALENDAR = 90;
    public static final int PERMISSIONS_REQUEST_CALL_LOG = 100;
    public static final int PERMISSIONS_REQUEST_CALL_PHONE = 101;
    public static final int PERMISSIONS_REQUEST_READ_PHONE_STATUS = 110;
    public static final int PERMISSIONS_REQUEST_READ_PHONE_STATUS_REGISTER = 112;
    private PermissionUtil() {}

    public static boolean hasReadContactPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasWriteContactPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;
    }
    public static boolean hasLocationPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestLocationPermission(Activity mActivity) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                PERMISSIONS_REQUEST_LOCATION);
    }

    public static void requestLocationPermission(Fragment mfragment) {
        mfragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                PERMISSIONS_REQUEST_LOCATION);
    }
    public static void requestContactPermission(Activity mActivity) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS},
                PERMISSIONS_REQUEST_CONTACTS);
    }

    public static boolean hasStorageWritePermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasCalendarWritePermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCalendarPermission(Activity mActivity) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.WRITE_CALENDAR},
                PERMISSIONS_REQUEST_CALENDAR);
    }

    public static boolean hasStorageReadPermission(Context mContext) {
        boolean hasPermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            hasPermission = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return hasPermission;
    }

    public static void requestStoragePermission(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_STORAGE);
        } else {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_STORAGE);
        }
    }

    public static boolean hasCameraPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCameraPermission(Activity mActivity) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.CAMERA},
                PERMISSIONS_REQUEST_CAMERA);
    }

    public static boolean hasCallPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCallPermission(Activity mActivity) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.CALL_PHONE},
                PERMISSIONS_REQUEST_CALL);
    }

    public static boolean hasReadSMSPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestReadSMSPermission(Activity mActivity) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.READ_SMS},
                PERMISSIONS_REQUEST_READ_SMS);
    }

    public static boolean hasSendSMSPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestSendSMSPermission(Activity mActivity) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.SEND_SMS},
                PERMISSIONS_REQUEST_SEND_SMS);
    }

    public static boolean hasReadPhoneStatusPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestPhoneStatusPermission(Activity mActivity) {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                PERMISSIONS_REQUEST_READ_PHONE_STATUS);
    }

    public static boolean hasRecordAudioPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestRecordAudiPermission(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CALL_LOG},
                    PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
    }

    public static boolean hasCallPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasReadPhoneStatusPermission(Activity activity){
        return ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }
}
