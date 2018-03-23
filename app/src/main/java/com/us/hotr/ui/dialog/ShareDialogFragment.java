package com.us.hotr.ui.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.receiver.Share;
import com.us.hotr.ui.activity.SinaShareActivity;
import com.us.hotr.util.Tools;

import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

/**
 * Created by Mloong on 2017/11/3.
 */

public class ShareDialogFragment extends BottomSheetDialogFragment {

    private ImageView ivFriend, ivMoment, ivWeibo;
    private TextView tvCancel;
    private BottomSheetBehavior mBehavior;

    private Share share;

    public static ShareDialogFragment newInstance(Share share) {
        ShareDialogFragment shareDialogFragment = new ShareDialogFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.PARAM_DATA, share);
        shareDialogFragment.setArguments(b);
        return shareDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getActivity(), R.layout.dialog_share, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        share = (Share) getArguments().getSerializable(Constants.PARAM_DATA);

        ivFriend = (ImageView) view.findViewById(R.id.iv_friend);
        ivMoment = (ImageView) view.findViewById(R.id.iv_moment);
        ivWeibo = (ImageView) view.findViewById(R.id.iv_weibo);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        ivFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                Tools.shareToWechatFriend(getContext(), share);
            }
        });
        ivMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                Tools.shareToWechatTimeLine(getContext(), share);
            }
        });
        ivWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                Intent i = new Intent(getActivity(), SinaShareActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_DATA, share);
                i.putExtras(b);
                startActivity(i);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        return dialog;

    }

    @Override
    public void onStart()
    {
        super.onStart();
        mBehavior.setState(STATE_EXPANDED);
    }

    public void doclick(View v)
    {
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}
