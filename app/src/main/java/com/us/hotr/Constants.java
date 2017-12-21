package com.us.hotr;

import java.util.HashMap;

/**
 * Created by Mloong on 2017/8/31.
 */

public class Constants {

    public static final String SERVER_URL = "http://192.168.0.125:8080/hotr-api-web/";
    public static final int SERVER_TIMEOUT = 30;
    public static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    public static final String WECHAT_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?";

    public static final String WECHAT_APP_ID = "wx162b31804e04a141";
    public static final String WECHAT_APP_SECRET = "4791e3ede549087797a2cea44d9d01de";
    public static final String WECHAT_PACKAGE_VALUE = "Sign=WXPay";
    public static final String SINA_APP_ID = "1546502328";
    public static final String SINA_REDIRECT_URL = "www.sina.com";
    public static final String SINA_SCOPE = "all";

    public static final String SHARE_TO_WECHAT_FRIEND = "SHARE_TO_WECHAT_FRIEND";
    public static final String SHARE_TO_WECHAT_TIMELINE = "SHARE_TO_WECHAT_TIMELINE";
    public static final String LOGIN_TO_WECHAT = "LOGIN_TO_WECHAT";
    public static final String LOGIN_TO_WECHAT_SCOPE = "snsapi_userinfo";

    public static final String BANNER_ID = "BANNER_ID";
    public static final String MODULE_ID = "MODULE_ID";
    public static final String NEWS_ID = "NEWS_ID";
    public static final String DIVIDER_ID = "DIVIDER_ID";
    public static final String AD1_ID = "AD1_ID";
    public static final String AD3_ID = "AD3_ID";
    public static final String TITLE_IMGE_ID = "TITLE_IMGE_ID";
    public static final String TITLE_TEXT_ID = "TITLE_TEXT_ID";
    public static final String COMPARE_ID = "COMPARE_ID";
    public static final String POST_ID = "POST_ID";
    public static final String DOCTOR_ID = "DOCTOR_ID";
    public static final String DOCTOR_HEADER_ID = "DOCTOR_HEADER_ID";
    public static final String HOSPITAL_ID = "HOSPITAL_ID";
    public static final String HOSPITAL_HEADER_ID = "HOSPITAL_HEADER_ID";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String SUBJECT_ID = "SUBJECT_ID";
    public static final String MASSEUR_ID = "MASSEUR_ID";
    public static final String INTERVIEW_LIST_ID = "INTERVIEW_LIST_ID";
    public static final String SPA_ID = "SPA_ID";
    public static final String MASSEUR_HEADER_ID = "MASSEUR_HEADER_ID";
    public static final String SPA_HEADER_ID = "SPA_HEADER_ID";
    public static final String INTERVIEW_ID = "INTERVIEW_ID";
    public static final String GROUP_ID = "GROUP_ID";

    public static final int RECEIPT_STATUS_UNUSED = 200;
    public static final int RECEIPT_STATUS_USED = 201;
    public static final int RECEIPT_STATUS_EXPITED = 202;
    public static final int RECEIPT_STATUS_REFUNDING = 203;
    public static final int RECEIPT_STATUS_REFUNDED = 204;

    public static final int TYPE_DOCTOR = 101;
    public static final int TYPE_CASE = 102;
    public static final int TYPE_HOSPITAL = 103;
    public static final int TYPE_PRODUCT = 104;
    public static final int TYPE_FRIEND = 105;
    public static final int TYPE_MASSAGE = 106;
    public static final int TYPE_MASSEUR = 107;
    public static final int TYPE_SPA = 108;
    public static final int TYPE_POST = 109;
    public static final int TYPE_PARTY = 110;
    public static final int TYPE_INTERVIEW = 120;
    public static final int TYPE_MY_PRODUCT = 130;
    public static final int TYPE_NEARBY_PEOPLE = 140;
    public static final int TYPE_OFFICIAL_POST = 141;

    public static final String PARAM_TITLE = "PARAM_TITLE";
    public static final String PARAM_NAME = "PARAM_NAME";
    public static final String PARAM_ID = "PARAM_ID";
    public static final String PARAM_TYPE = "PARAM_TYPE";
    public static final String PARAM_SEARCH_STRING = "PARAM_SEARCH_STRING";
    public static final String PARAM_ENABLE_REFRESH = "PARAM_ENABLE_REFRESH";
    public static final String PARAM_DATA = "PARAM_DATA";
    public static final String PARAM_HOSPITAL_ID = "PARAM_HOSPITAL_ID";
    public static final String PARAM_DOCTOR_ID = "PARAM_DOCTOR_ID";
    public static final String PARAM_SPA_ID = "PARAM_SPA_ID";
    public static final String PARAM_MASSEUR_ID = "PARAM_MASSEUR_ID";

    public static final int SORT_BY_DEFAULT = 0;
    public static final int SORT_BY_APPOINTMENT_ASC = 1;
    public static final int SORT_BY_APPOINTMENT_DESC = 2;
    public static final int SORT_BY_PRICE_ASC = 3;
    public static final int SORT_BY_PRICE_DESC = 4;
    public static final int SORT_BY_CASE_ASC = 5;
    public static final int SORT_BY_CASE_DESC = 6;
    public static final int SORT_BY_AMOUNT_ASC = 7;
    public static final int SORT_BY_AMOUNT_DESC = 8;
    public static final int SORT_BY_DISTANCE = 9;

    public static final int LOAD_MORE = 500;
    public static final int LOAD_PULL_REFRESH = 501;
    public static final int LOAD_DIALOG = 502;
    public static final int LOAD_PAGE = 503;


    public static final int MAX_PAGE_ITEM = 20;

    public static final int ALL_CITY_ID = 5000;

    public static final int NORMAL_PRODUCT = 1;
    public static final int PROMOTION_PRODUCT = 2;

    public static final int PRE_PAYMENT = 1;
    public static final int FULL_PAYMENT = 2;

    public static final int SUCCESS = 200;
    public static final int ERROR_WRONG_PASSWORD = 500;
    public static final int ERROR_INVALID_SESSIONID = 501;

}
