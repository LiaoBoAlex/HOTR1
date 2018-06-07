package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.receiver.Share;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.dialog.ShareDialogFragment;

public class InviteFriendActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.invite_friend);
        findViewById(R.id.iv_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share share = new Share();
                share.setDescription(getString(R.string.invite_friend_content));
                share.setImageUrl(Constants.LOGO_URL);
                share.setTitle(getString(R.string.invite_friend_title));
                share.setUrl("http://hotr.hotr-app.com/hotr-api-web/#/party?id=7");
                share.setSinaContent(getString(R.string.invite_friend_content));
                ShareDialogFragment.newInstance(share).show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_invite_friend;
    }
}
