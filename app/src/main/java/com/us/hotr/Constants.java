package com.us.hotr;

/**
 * Created by Mloong on 2017/8/31.
 */

public class Constants {

    public static final String SERVER_URL = "http://192.168.0.234:8080/hotr-api-web/";
    public static final int SERVER_TIMEOUT = 30;
    public static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    public static final String WECHAT_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?";

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

    public static final int TYPE_PRODUCT = 101;
    public static final int TYPE_HOSPITAL = 102;
    public static final int TYPE_DOCTOR = 103;
    public static final int TYPE_MASSAGE = 104;
    public static final int TYPE_SPA = 105;
    public static final int TYPE_MASSEUR = 106;
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
    public static final int SORT_BY_APPOINTMENT_ASC = 11;
    public static final int SORT_BY_APPOINTMENT_DESC = 12;
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
    public static final int RECEIPT_STATUS_REFUNDING = 2;
    public static final int RECEIPT_STATUS_REFUNDED = 4;

    public static final int MAX_PAGE_ITEM = 10;
    public static final long ORDER_VALID_TIME_IN_MINISEC = 24*60*60*1000;
    public static final String SUPPORT_LINE = "4001118518";
    public static final String ABOUT_US = "http://hotr.us-app.com/share/about.html";

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
