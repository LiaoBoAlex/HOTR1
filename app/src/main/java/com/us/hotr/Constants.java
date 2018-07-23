package com.us.hotr;


import com.google.gson.Gson;
import com.us.hotr.storage.bean.VoucherSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/8/31.
 */

public class Constants {
//development server
//    public static final String SERVER_URL = "http://.hotr-app.com/hotr-api-web/";
//production server
    public static final String SERVER_URL = "https://hotr.us-app.com/hotr-api-web/";
    public static final String SHARE_URL = "https://hotr.hotr-app.com/hotr-api-web/share";
    public static final int SERVER_TIMEOUT = 30;
    public static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    public static final String WECHAT_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?";
    public static final String LOGO_URL = "http://image-product-web.oss-cn-beijing.aliyuncs.com/headPortrait/logo_hotr.png";

    public static final String WECHAT_APP_ID = "wx162b31804e04a141";
    public static final String WECHAT_APP_SECRET = "4791e3ede549087797a2cea44d9d01de";
    public static final String WECHAT_PACKAGE_VALUE = "Sign=WXPay";
    public static final String SINA_APP_ID = "1546502328";
    public static final String SINA_REDIRECT_URL = "www.sina.com";
    public static final String SINA_SCOPE = "all";
    public static final String ALIPAY_PARTENER_ID = "2088811922494101";

    public static final String SHARE_TO_WECHAT_FRIEND = "SHARE_TO_WECHAT_FRIEND";
    public static final String SHARE_TO_WECHAT_TIMELINE = "SHARE_TO_WECHAT_TIMELINE";
    public static final String LOGIN_TO_WECHAT = "LOGIN_TO_WECHAT";
    public static final String LOGIN_TO_WECHAT_SCOPE = "snsapi_userinfo";

    public static final String BAIDU_MAP_PACKAGE_NAME = "com.baidu.BaiduMap";
    public static final String AUTONAVI_MAP_PACKAGE_NAME = "com.autonavi.minimap";
    public static final String TENCENT_MAP_PACKAGE_NAME = "com.tencent.map";


    public static final String MTA_ID_SPLASH_SCREEN = "splash_screen";
    public static final String MTA_ID_CLICK_SHARE = "click_share";
    public static final String MTA_ID_PRODUCT_MAIN_SCREEN = "product_main_screen";
    public static final String MTA_ID_MASSAGE_MAIN_SCREEN = "massage_main_screen";
    public static final String MTA_ID_PARTY_MAIN_SCREEN = "party_main_screen";
    public static final String MTA_ID_POST_SCREEN = "post_screen";
    public static final String MTA_ID_CASE_SCREEN = "case_screen";
    public static final String MTA_ID_CLICK_POST_BUTTON = "click_post_button";
    public static final String MTA_ID_CLICK_CASE_BUTTON = "click_case_button";
    public static final String MTA_ID_ADD_FAV_PEOPLE = "add_favirote_people";
    public static final String MTA_ID_SEARCH = "search";
    public static final String MTA_ID_CLICK_PRODUCT_BANNER = "click_product_banner";
    public static final String MTA_ID_CLICK_MASSAGE_BANNER = "click_massage_banner";
    public static final String MTA_ID_CLICK_PRODUCT_MODULE = "click_product_module";
    public static final String MTA_ID_CLICK_MASSAGE_MODULE = "click_massage_module";
    public static final String MTA_ID_CLICK_PURPOSE_PRODUCT = "click_purpose_product";
    public static final String MTA_ID_CLICK_PURPOSE_MASSAGE = "click_purpose_massage";
    public static final String MTA_ID_CLICK_PRODUCT_ADV = "click_product_adv";
    public static final String MTA_ID_CLICK_MASSAGE_ADV = "click_massage_adv";
    public static final String MTA_ID_CLICK_PRODUCT_ADV_3 = "click_product_adv_3";
    public static final String MTA_ID_CLICK_MASSAGE_ADV_3 = "click_massage_adv_3";
    public static final String MTA_ID_CLICK_PURPOSE_HOSPITAL = "click_purpose_hospital";
    public static final String MTA_ID_CLICK_PURPOSE_MASSEUR = "click_purpose_masseur";
    public static final String MTA_ID_PARTY_DETAIL_SCREEN = "party_detail_screen";
    public static final String MTA_ID_MASSEUR_DETAIL_SCREEN = "masseur_detail_screen";
    public static final String MTA_ID_SPA_DETAIL_SCREEN = "spa_detail_screen";
    public static final String MTA_ID_PRODUCT_DETAIL_SCREEN = "product_detail_screen";
    public static final String MTA_ID_HOSPITAL_DETAIL_SCREEN = "hospital_detail_screen";
    public static final String MTA_ID_GROUP_DETAIL_SCREEN = "group_detail_screen";
    public static final String MTA_ID_FAV_MASSEUR = "fav_masseur";
    public static final String MTA_ID_FAV_HOSPITAL = "fav_hospital";
    public static final String MTA_ID_FAV_PRODUCT = "fav_product";
    public static final String MTA_ID_FAV_DOCTOR = "fav_doctor";
    public static final String MTA_ID_FAV_SPA = "fav_spa";
    public static final String MTA_ID_FAV_MASSAGE = "fav_massage";





    public static final List<Long> NEW_USER_VOUCHER = new ArrayList<Long>(){{
        add(4l);add(4l);add(4l);add(4l);add(4l);
        add(5l);add(5l);add(5l);add(5l);add(5l);
        add(6l);add(6l);add(6l);add(6l);add(6l);
        add(7l);add(7l);add(7l);add(7l);add(7l);
        add(8l);add(9l);add(10l);}};

    public static final List<Long> SHARE_FRIEND_VOUCHER = new ArrayList<Long>(){{
        add(16l);}};

//    public static String getNewUserVoucher(){
//        List<VoucherSet> voucherSetList = new ArrayList<>();
//        voucherSetList.add(new VoucherSet(4l, 5));
//        voucherSetList.add(new VoucherSet(5l, 5));
//        voucherSetList.add(new VoucherSet(6l, 5));
//        voucherSetList.add(new VoucherSet(7l, 5));
//        voucherSetList.add(new VoucherSet(8l, 1));
//        voucherSetList.add(new VoucherSet(9l, 1));
//        voucherSetList.add(new VoucherSet(10l, 1));
//        return new Gson().toJson(voucherSetList);
//    }
//
//    public static String getShareFriendVoucher(){
//        List<VoucherSet> voucherSetList = new ArrayList<>();
//        voucherSetList.add(new VoucherSet(12l, 1));
//        return new Gson().toJson(voucherSetList);
//    }

    public static final int TYPE_PRODUCT = 103;
    public static final int TYPE_HOSPITAL = 101;
    public static final int TYPE_DOCTOR = 102;
    public static final int TYPE_MASSAGE = 106;
    public static final int TYPE_SPA = 104;
    public static final int TYPE_MASSEUR = 105;
    public static final int TYPE_CASE = 107;
    public static final int TYPE_SEARCH_POST = 108;
    public static final int TYPE_GROUP = 109;
    public static final int TYPE_PARTY = 110;
    public static final int TYPE_SEARCH_PEOPLE = 111;
    public static final int TYPE_INTERVIEW = 112;

    public static final int TYPE_MY_PRODUCT = 120;
    public static final int TYPE_MY_DOCTOR = 121;
    public static final int TYPE_POST = 122;
    public static final int TYPE_FAVORITE = 130;
    public static final int TYPE_FANS = 131;
    public static final int TYPE_NEARBY_PEOPLE = 132;
    public static final int TYPE_OFFICIAL_POST = 140;
    public static final int TYPE_FAVORITE_POST = 141;
    public static final int TYPE_NEARBY_POST = 142;
    public static final int TYPE_CHAT = 150;

    public static final String PARAM_TITLE = "PARAM_TITLE";
    public static final String PARAM_NAME = "PARAM_NAME";
    public static final String PARAM_ID = "PARAM_ID";
    public static final String PARAM_TYPE = "PARAM_TYPE";
    public static final String PARAM_SEARCH_STRING = "PARAM_SEARCH_STRING";
    public static final String PARAM_ENABLE_REFRESH = "PARAM_ENABLE_REFRESH";
    public static final String PARAM_IS_FAV = "PARAM_IS_FAV";
    public static final String PARAM_DATA = "PARAM_DATA";
    public static final String PARAM_HOSPITAL_ID = "PARAM_HOSPITAL_ID";
    public static final String PARAM_DOCTOR_ID = "PARAM_DOCTOR_ID";
    public static final String PARAM_SPA_ID = "PARAM_SPA_ID";
    public static final String PARAM_SUBJECT_ID = "PARAM_SUBJECT_ID";
    public static final String PARAM_PRODUCT_ID = "PARAM_PRODUCT_ID";
    public static final String PARAM_MASSEUR_ID = "PARAM_MASSEUR_ID";
    public static final String PARAM_MASSAGE_ID = "PARAM_MASSAGE_ID";
    public static final String PARAM_KEYWORD = "PARAM_KEYWORD";
    public static final String PARAM_CATEGORY= "PARAM_CATEGORY";

    public static final int SORT_BY_DEFAULT = 6;
//    public static final int SORT_BY_APPOINTMENT_ASC = 11;
//    public static final int SORT_BY_APPOINTMENT_DESC = 12;
    public static final int SORT_BY_PRICE_ASC = 3;
    public static final int SORT_BY_PRICE_DESC = 4;
    public static final int SORT_BY_CASE_ASC = 9;
    public static final int SORT_BY_CASE_DESC = 10;
    public static final int SORT_BY_AMOUNT_ASC = 0;
    public static final int SORT_BY_AMOUNT_DESC = 1;
    public static final int SORT_BY_DISTANCE = 2;
//type 本周最热 7 时间最新 8

    public static final int LOAD_MORE = 500;
    public static final int LOAD_PULL_REFRESH = 501;
    public static final int LOAD_DIALOG = 502;
    public static final int LOAD_PAGE = 503;

    public static final int RECEIPT_STATUS_UNUSED = 0;
    public static final int RECEIPT_STATUS_USED = 1;
    public static final int RECEIPT_STATUS_EXPIRED = 5;
    public static final int RECEIPT_STATUS_REFUNDING = 6;
    public static final int RECEIPT_STATUS_REFUNDED = 4;

    public static final int MAX_PAGE_ITEM = 10;
    public static final long ORDER_VALID_TIME_IN_MINISEC = 24*60*60*1000;
    public static final String SUPPORT_LINE = "01059458967";
    public static final String ABOUT_US = "https://hotr.us-app.com/share/about.html";
    public static final String RECIEPT_URL = "http://hotr.hotr-app.com/toIndex.do#/?ticket_sn=";
    public static final String BUSINESS_PARTENER = "https://hotr.us-app.com/share/activity/cooperation/index.html";

    public static final int ALL_CITY_ID = 5000;

    public static final int NORMAL_PRODUCT = 1;
    public static final int PROMOTION_PRODUCT = 2;

    public static final int PRE_PAYMENT = 1;
    public static final int FULL_PAYMENT = 2;

    public static final int ORDER_UNPAID = 0;
    public static final int ORDER_PAID = 1;

    public static final int NO_ID_REQUIRED = 0;
    public static final int REQUIRE_HK_PASS = 1;
    public static final int REQUIRE_PASSPORT = 2;


    public static final int SUCCESS = 200;
    public static final int ERROR_WRONG_PASSWORD = 500;
    public static final int ERROR_INVALID_SESSIONID = 501;
}
