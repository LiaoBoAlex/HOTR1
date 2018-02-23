package com.us.hotr.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.CityCode;
import com.us.hotr.storage.bean.Provence;
import com.us.hotr.storage.bean.Type;
import com.us.hotr.storage.bean.WechatBill;
import com.us.hotr.ui.HOTRApplication;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    //                                                  北京市， 上海市，   成都市，  重庆市， 广州市， 深圳市， 大连市，  杭州市， 南京市
    private static final long[] backendMassageCityCode={120100, 200100,   210100,  220100, 230100, 230200, 240100,  250100, 260100};
    private static final long[] backendProductCityCode={110100, 140100,   130100,  150100, 160100, 160200, 170100,  180100, 190100};
    private static final long[] baiduCityCode=         {131,    289,      75,      132,    257,    340,    167,     179,    315};

    private static final String[] baiduProvinceName =      {"北京市", "上海市", "四川省", "重庆市", "广东省", "辽宁省", "浙江省", "江苏省"};
    private static final long[] backendMassageProvinceCode={120000,  200000,   210000,  220000,  230000,  240000,   250000,  260100};
    private static final long[] backendProductProvinceCode={110000,  140000,   130000,  150000,  160000,  170000,   180000,  190100};
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

//    public static Bitmap getCompressedBitmap(String filePath, int boud){
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, options);
//        int height = options.outHeight;
//        int width= options.outWidth;
//        int inSampleSize = 2;
//        int minLen = Math.min(height, width);
//        if(minLen > boud) {
//            float ratio = (float)minLen / 100.0f;
//            inSampleSize = (int)ratio;
//        }
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = inSampleSize;
//        return BitmapFactory.decodeFile(filePath, options);
//    }

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

    public static CityCode getCityCodeFromBaidu(String  baiduCode){
        CityCode cityCode = new CityCode();
        try {
            if (baiduCode == null)
                return cityCode;
            for (int i = 0; i < baiduCityCode.length; i++) {
                if (baiduCityCode[i] == Long.parseLong(baiduCode)){
                    cityCode.setBaiduCityCode(baiduCityCode[i]);
                    cityCode.setMassageCityCode(backendMassageCityCode[i]);
                    cityCode.setProductCityCode(backendProductCityCode[i]);
                    return cityCode;
                }
            }
            return cityCode;
        }catch(Throwable th){
            return cityCode;
        }
    }

    public static CityCode getCityCodeFromProduct(long  productCode){
        CityCode cityCode = new CityCode();
        for (int i = 0; i < backendProductCityCode.length; i++) {
            if (backendProductCityCode[i] == productCode){
                cityCode.setBaiduCityCode(baiduCityCode[i]);
                cityCode.setMassageCityCode(backendMassageCityCode[i]);
                cityCode.setProductCityCode(backendProductCityCode[i]);
                return cityCode;
            }
        }
        return cityCode;
    }

    public static CityCode getCityCodeFromMassage(long  massageCode){
        CityCode cityCode = new CityCode();
        for (int i = 0; i < backendMassageCityCode.length; i++) {
            if (backendMassageCityCode[i] == massageCode){
                cityCode.setBaiduCityCode(baiduCityCode[i]);
                cityCode.setMassageCityCode(backendMassageCityCode[i]);
                cityCode.setProductCityCode(backendProductCityCode[i]);
                return cityCode;
            }
        }
        return cityCode;
    }

    public static long getMassageProvinceCode(String provinceName){
        for(int i=0; i<baiduProvinceName.length;i++){
            if(baiduProvinceName[i].equals(provinceName))
                return backendMassageProvinceCode[i];
        }
        return 0;
    }

    public static long getProductProvinceCode(String provinceName){
        for(int i=0; i<baiduProvinceName.length;i++){
            if(baiduProvinceName[i].equals(provinceName))
                return backendProductProvinceCode[i];
        }
        return 0;
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

    public static String getPartyTime(Context mContext, String startTime, String endTime){
        try {
            Calendar calStart=Calendar.getInstance();
            calStart.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            Calendar calEnd=Calendar.getInstance();
            calEnd.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            return String.format(mContext.getString(R.string.party_date1),
                    calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH)+1, calStart.get(Calendar.DATE), calEnd.get(Calendar.MONTH)+1, calEnd.get(Calendar.DATE));
        } catch (Throwable tr) {
            return "";
        }
    }

    public static String getPartyTime(Context mContext, String startTime){
        try {
            Calendar calStart=Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
            calStart.setTime(date);
            return String.format(mContext.getString(R.string.party_date3),
                    calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH)+1, calStart.get(Calendar.DATE))+" "+new SimpleDateFormat("HH:mm").format(date);
        } catch (Throwable tr) {
            return "";
        }
    }

    public static String getPartyTime2(Context mContext, String startTime){
        try {
            Calendar calStart=Calendar.getInstance();
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
            calStart.setTime(date);
            return mContext.getString(R.string.party_date4)+String.format(mContext.getString(R.string.party_date3),
                    calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH)+1, calStart.get(Calendar.DATE))+" "+new SimpleDateFormat("HH:mm").format(date);
        } catch (Throwable tr) {
            return "";
        }
    }

    public static String getPartyTime2(Context mContext, String startTime, String endTime){
        try {
            Calendar calStart=Calendar.getInstance();
            calStart.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            Calendar calEnd=Calendar.getInstance();
            calEnd.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            return String.format(mContext.getString(R.string.party_date2),
                    calStart.get(Calendar.MONTH)+1, calStart.get(Calendar.DATE), calEnd.get(Calendar.MONTH)+1, calEnd.get(Calendar.DATE));
        } catch (Throwable tr) {
            return "";
        }
    }

    public static String getSaleTime(Context mContext, String time){
        try {
            Calendar cal =Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
            return String.format(mContext.getString(R.string.status_pre_sale),
                    cal.get(Calendar.MONTH)+1, cal.get(Calendar.DATE));
        } catch (Throwable tr) {
            return "";
        }
    }

    public static String getVoucherTime(Context mContext, String startTime, String endTime){
        try {
            Calendar calStart=Calendar.getInstance();
            calStart.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
            Calendar calEnd=Calendar.getInstance();
            calEnd.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            String time1 = calStart.get(Calendar.YEAR) + "." + (calStart.get(Calendar.MONTH)+1) + "." + calStart.get(Calendar.DATE);
            String time2 = calEnd.get(Calendar.YEAR) + "." + (calEnd.get(Calendar.MONTH)+1) + "." + calEnd.get(Calendar.DATE);
            return String.format(mContext.getString(R.string.voucher_time), time1, time2);
        } catch (Throwable tr) {
            return "";
        }
    }

    public static String getZipFileName(String fileName){
        File f = new File(Environment.getExternalStorageDirectory() + "/DCIM/HOTR/ZIP/");
        if (!f.exists()) {
            f.mkdirs();
        }
        return f.getAbsolutePath();
    }

    public static boolean isUserLogin(Context mContext){
        if(HOTRSharePreference.getInstance(mContext).getUserID().isEmpty())
            return false;
        else
            return true;
    }

    public static  String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width:100% !important; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body style:'height:auto;max-width: 100%; width:auto;'>" + bodyHTML + "</body></html>";
    }

    public static String getPostTime(Context mContext, String date){
        try {
            Calendar calCreate=Calendar.getInstance();
            calCreate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
            Calendar calNow = Calendar.getInstance();
            if(calNow.get(Calendar.YEAR) != calCreate.get(Calendar.YEAR))
                return String.format(mContext.getString(R.string.date1), calCreate.get(Calendar.YEAR)+"", (calCreate.get(Calendar.MONTH)+1)+"", calCreate.get(Calendar.DATE)+"");
            else if(calNow.getTimeInMillis()-calCreate.getTimeInMillis()>172800000)
                return String.format(mContext.getString(R.string.date2), calCreate.get(Calendar.MONTH)+1, calCreate.get(Calendar.DATE));
            else if(calNow.getTimeInMillis()-calCreate.getTimeInMillis()>86400000)
                return mContext.getString(R.string.one_day_ago);
            else if(calNow.getTimeInMillis()-calCreate.getTimeInMillis()>3600000)
                return String.format(mContext.getString(R.string.hours_ago), (calNow.getTimeInMillis()-calCreate.getTimeInMillis())/3600000);
            else if(calNow.getTimeInMillis()-calCreate.getTimeInMillis()>60000)
                return String.format(mContext.getString(R.string.mins_ago), (calNow.getTimeInMillis()-calCreate.getTimeInMillis())/60000);
            else
                return mContext.getString(R.string.now);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long getOrderTimeInMinSec(String date){
        Calendar calCreate=Calendar.getInstance();
        try {
            calCreate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
            Calendar calNow = Calendar.getInstance();
            if(calNow.getTimeInMillis()-calCreate.getTimeInMillis()<Constants.ORDER_VALID_TIME_IN_MINISEC)
                return Constants.ORDER_VALID_TIME_IN_MINISEC -(calNow.getTimeInMillis()-calCreate.getTimeInMillis());
            else
                return 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getOrderTime(Context mContext, long minisec){
        if(minisec>3600000)
            return mContext.getString(R.string.order_time_left)+(minisec/3600000) + mContext.getString(R.string.hour);
        else if(minisec>60000)
            return mContext.getString(R.string.order_time_left)+(minisec/60000) + mContext.getString(R.string.min);
        else
            return mContext.getString(R.string.order_time_left)+(minisec/1000) + mContext.getString(R.string.sec);
    }

    public static Map<String ,String>  gsonStringToMap(String jsonStr){
        Gson gson = new Gson();
        Map<String ,String> map = null;
        if(!jsonStr.isEmpty()){
            java.lang.reflect.Type type = new TypeToken<Map<String,String>>(){}.getType();
            map = gson.fromJson(jsonStr,type);
        }
        return map;
    }

    public static String getMainPhoto(Map<String, String> map){
        for (String key : map.keySet()) {
            if(key.equals("index"))
                return map.get(key);
        }
        return null;
    }

    public static List<String> mapToList(Map<String, String> map){
        List<String> list = new ArrayList<>();
        int i = 0, index = 0;
        for (String key : map.keySet()) {
            list.add(map.get(key));
            if(key.equals("index"))
                index = i;
            i++;
        }
        String s = list.get(index);
        list.remove(index);
        list.add(0, s);
        return list;
    }

    public static boolean isPackageInstalled(String packageName) {
//        return new File("/data/data/" + packageName).exists();
        return true;
    }
}
