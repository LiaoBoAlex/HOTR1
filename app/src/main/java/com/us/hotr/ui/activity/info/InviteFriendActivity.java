package com.us.hotr.ui.activity.info;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.receiver.Share;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.dialog.ShareDialogFragment;
import com.us.hotr.ui.dialog.VoucherDialog;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

public class InviteFriendActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalBus.getBus().register(this);
        setMyTitle(R.string.invite_friend);
        findViewById(R.id.iv_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share share = new Share();
                share.setDescription(getString(R.string.invite_friend_content));
                share.setImageUrl(Constants.LOGO_URL);
                share.setTitle(getString(R.string.invite_friend_title));
                share.setUrl("http://hotr.hotr-app.com/hotr-api-web/gift/index.html?invitation_id="
                        + HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo().getUserId()
                +"&telephone=" + HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo().getMobile());
                share.setSinaContent(getString(R.string.invite_friend_content));
                ShareDialogFragment.newInstance(share).show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Subscribe
    public void getMessage(Events.CreateVoucher createVoucher) {
        if(HOTRSharePreference.getInstance(getApplicationContext()).isFirstinviteFriend()) {
            SubscriberListener mListener = new SubscriberListener<String>() {
                @Override
                public void onNext(final String result) {
                    HOTRSharePreference.getInstance(getApplicationContext()).storeFirstInviteFriend(false);
                }
            };
            ServiceClient.getInstance().addAllVoucher(new ProgressSubscriber(mListener, InviteFriendActivity.this),
                    HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), Constants.SHARE_FRIEND_VOUCHER);
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_invite_friend;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }
}
