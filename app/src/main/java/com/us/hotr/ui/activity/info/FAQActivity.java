package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;

/**
 * Created by Mloong on 2017/10/13.
 */

public class FAQActivity extends BaseActivity {

    private String title;
    private TextView tvTitle;
    private WebView wvContent;

    @Override
    protected int getLayout() {
        return R.layout.activity_faq;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.faq);
        title = getIntent().getStringExtra(Constants.PARAM_TITLE);
        initStaticView();
    }

    private void initStaticView(){
        tvTitle = (TextView) findViewById(R.id.tv_title);
        wvContent = (WebView) findViewById(R.id.wv_content);

        tvTitle.setText(title);
        String url="https://mp.weixin.qq.com/s?__biz=MjM5MDczMjM2MA==&mid=2652388593&idx=1&sn=7c08949e42d61de2f022ffcee552738a&chksm=bdacc5d68adb4cc0a038f92efca95acf17ef1facbc86553441e8f47dbb0316104b80b6379485&scene=0&key=bda634fb2c7300a3b6c583ff2fe7827c6ddb195c74ff4c744b765cd8a48e9f4e67360531050b8b7addce3f97d0b9440e5a0b6bc1948c635320267447fc4b8075deffdf61ea7ecc241bf9a9f120f378cd&ascene=0&uin=OTYwOTY3Njgw&devicetype=iMac+MacBookPro11%2C4+OSX+OSX+10.11.6+build(15G31)&version=12010210&nettype=WIFI&fontScale=100&pass_ticket=kCRObwEpa%2BTF24xhAVuiq%2FBQ2Ki1t8IcSMer1q5hQg2vFO41c4RQRrTB236TDGFU";
        wvContent.loadUrl(url);
    }

}
