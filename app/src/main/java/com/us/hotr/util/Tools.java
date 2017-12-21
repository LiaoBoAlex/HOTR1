package com.us.hotr.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Type;
import com.us.hotr.storage.bean.WechatBill;
import com.us.hotr.ui.HOTRApplication;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mloong on 2017/8/31.
 */

public class Tools {

    private static final int[] backendCityCode={2,   5,   9,  12};
    private static final int[] baiduCityCode={131, 150,  19, 153};
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getWindowHeight(Context context){
        Rect outRect1 = new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        return outRect1.height();
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float dpToPxInt(Context context, float dp) {
        return (int)(dpToPx(context, dp) + 0.5f);
    }

    public static float pxToDpCeilInt(Context context, float px) {
        return (int)(pxToDp(context, px) + 0.5f);
    }

    public static Bitmap decodeFile(String path, int WIDTH, int HIGHT){
        try {
            //Decode image size
            File f = new File(path);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public static void Toast(Context context, String s){
        Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void validatePasswordInput(EditText editText){
        editText.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) {
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z0-9]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });
    }

    public static void shareToWechatFriend(Context mContext) {
        if (!HOTRApplication.getIwxApi().isWXAppInstalled()) {
            Tools.Toast(mContext, mContext.getString(R.string.no_wechat_installed));
            return;
        }
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = "http://blog.csdn.net/jing_unique_da/article/details/47254993";
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = "this is title";
        msg.description = "this is description";
        Bitmap loadedImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.placeholder_post3);
        msg.setThumbImage(loadedImage);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(Constants.SHARE_TO_WECHAT_FRIEND);
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        HOTRApplication.getIwxApi().sendReq(req);
    }

    public static void shareToWechatTimeLine(Context mContext) {
        if (!HOTRApplication.getIwxApi().isWXAppInstalled()) {
            Tools.Toast(mContext, mContext.getString(R.string.no_wechat_installed));
            return;
        }
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = "http://blog.csdn.net/jing_unique_da/article/details/47254993";
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = "this is title";
        msg.description = "this is description";
        Bitmap loadedImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.placeholder_post3);
        msg.setThumbImage(loadedImage);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(Constants.SHARE_TO_WECHAT_TIMELINE);
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        HOTRApplication.getIwxApi().sendReq(req);
    }

    public static void loginWechat(Context mContext){
        if (!HOTRApplication.getIwxApi().isWXAppInstalled()) {
            Tools.Toast(mContext, mContext.getString(R.string.no_wechat_installed));
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = Constants.LOGIN_TO_WECHAT_SCOPE;
        req.state = Constants.LOGIN_TO_WECHAT;
        HOTRApplication.getIwxApi().sendReq(req);
    }

    public static void wechatPay(Context mContext, WechatBill wechatBill){
        PayReq payRequest = new PayReq();
        payRequest.appId = Constants.WECHAT_APP_ID;
        payRequest.partnerId = wechatBill.getPartnerid();
        payRequest.prepayId = wechatBill.getPrepayid();
        payRequest.packageValue = Constants.WECHAT_PACKAGE_VALUE;
        payRequest.nonceStr = wechatBill.getNoncestr();
        payRequest.timeStamp = wechatBill.getTimestamp();
        payRequest.sign = wechatBill.getSign();

        HOTRApplication.getIwxApi().sendReq(payRequest);
    }

    public static void aliPay(final Activity mActivity, final String order, SubscriberListener listener){
        Observable.create(new ObservableOnSubscribe<AliPayResult>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<AliPayResult> subscriber) throws Exception {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(order,true);
                AliPayResult payResult = new AliPayResult(result);
                subscriber.onNext(payResult);
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber(listener, mActivity));
//        switch (payResult.getResultStatus()) {
//            case "9000":
//                GMToastUtil.showToast("支付成功");
//                break;\
//            case "8000":
//                GMToastUtil.showToast("正在处理中");
//                break;
//            case "4000":
//                GMToastUtil.showToast("订单支付失败");
//                break;
//            case "5000":
//                GMToastUtil.showToast("重复请求");
//                break;
//            case "6001":
//                GMToastUtil.showToast("已取消支付");
//                break;
//            case "6002":
//                GMToastUtil.showToast("网络连接出错");
//                break;
//            case "6004":
//                GMToastUtil.showToast("正在处理中");
//                break;
//            default:
//                GMToastUtil.showToast("支付失败");
//                break;
//        }
    }

    public static List<Type> getTypes(int type, Context mContext){
        List<Type> types = new ArrayList<>();
        types.add(new Type(Constants.SORT_BY_DEFAULT, mContext.getResources().getString(R.string.filter_default)));
        switch (type){
            case Constants.TYPE_DOCTOR:
            case Constants.TYPE_HOSPITAL:
                types.add(new Type(Constants.SORT_BY_APPOINTMENT_ASC, mContext.getResources().getString(R.string.filter_appointment)));
                types.add(new Type(Constants.SORT_BY_CASE_ASC, mContext.getResources().getString(R.string.filter_case)));
                break;
            case Constants.TYPE_SPA:
            case Constants.TYPE_MASSEUR:
                types.add(new Type(Constants.SORT_BY_AMOUNT_ASC, mContext.getResources().getString(R.string.filter_amount)));
                types.add(new Type(Constants.SORT_BY_DISTANCE, mContext.getResources().getString(R.string.filter_distance)));
                break;
            case Constants.TYPE_MASSAGE:

                types.add(new Type(Constants.SORT_BY_AMOUNT_ASC, mContext.getResources().getString(R.string.filter_amount)));
                types.add(new Type(Constants.SORT_BY_PRICE_ASC, mContext.getResources().getString(R.string.filter_price_asc)));
                types.add(new Type(Constants.SORT_BY_PRICE_DESC, mContext.getResources().getString(R.string.filter_price_desc)));
        }
        return types;
    }

    public static List<Type> getProductTypes(int type, Context mContext){
        List<Type> types = new ArrayList<>();
        types.add(new Type(Constants.SORT_BY_DEFAULT, mContext.getResources().getString(R.string.filter_default)));
        switch (type){
            case Constants.TYPE_PRODUCT:
                types.add(new Type(Constants.SORT_BY_AMOUNT_ASC, mContext.getResources().getString(R.string.filter_amount)));
                types.add(new Type(Constants.SORT_BY_PRICE_ASC, mContext.getResources().getString(R.string.filter_price_asc)));
                types.add(new Type(Constants.SORT_BY_PRICE_DESC, mContext.getResources().getString(R.string.filter_price_desc)));
                types.add(new Type(Constants.SORT_BY_CASE_ASC, mContext.getResources().getString(R.string.filter_case)));
                break;
            case Constants.TYPE_MASSAGE:
                types.add(new Type(Constants.SORT_BY_AMOUNT_ASC, mContext.getResources().getString(R.string.filter_amount)));
                types.add(new Type(Constants.SORT_BY_PRICE_ASC, mContext.getResources().getString(R.string.filter_price_asc)));
                types.add(new Type(Constants.SORT_BY_PRICE_DESC, mContext.getResources().getString(R.string.filter_price_desc)));
                types.add(new Type(Constants.SORT_BY_APPOINTMENT_ASC, mContext.getResources().getString(R.string.filter_appointment)));
                break;
        }
        return types;
    }

    public static int getCityCode(String  baiduCode){
        try {
            if (baiduCode == null)
                return -1;
            for (int i = 0; i < baiduCityCode.length; i++) {
                if (baiduCityCode[i] == Integer.parseInt(baiduCode))
                    return backendCityCode[i];
            }
            return -1;
        }catch(Throwable th){
            return -1;
        }
    }

    public static String validatePhotoString(String s){
        return s.replace("[", "").replace("]", "");
    }

    public static String convertTime(String s){
        try {
            return new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("HH:mm:ss").parse(s));

        }catch (Throwable th){
            return "";
        }
    }
}
