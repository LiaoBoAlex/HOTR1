package com.us.hotr.ui.fragment.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.MasseurExtraData;
import com.us.hotr.ui.activity.beauty.ListWithSearchActivity;
import com.us.hotr.ui.activity.info.AuditActivity;
import com.us.hotr.ui.activity.info.FavoriteCategoryActivity;
import com.us.hotr.ui.activity.info.FriendActivity;
import com.us.hotr.ui.activity.info.IdentityActivity;
import com.us.hotr.ui.activity.info.InviteFriendActivity;
import com.us.hotr.ui.activity.info.LinkSpaActivity;
import com.us.hotr.ui.activity.info.NoticeActivity;
import com.us.hotr.ui.activity.info.OrderListActivity;
import com.us.hotr.ui.activity.info.SettingActivity;
import com.us.hotr.ui.activity.info.ShopOrderActivity;
import com.us.hotr.ui.activity.info.VoucherActivity;
import com.us.hotr.ui.activity.party.DeliverAddressListActivity;
import com.us.hotr.ui.activity.search.SearchSpaActivity;
import com.us.hotr.ui.fragment.BaseLoadingFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.GetLoginResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Mloong on 2017/8/28.
 */

public class InfoFragment extends BaseLoadingFragment {
    private ImageView ivAvatar, ivFav, ivFollow, ivSetting, ivCert;
    private TextView tvTitle, tvCertified, tvCase, tvPost;
    private TextView tvMyOrder, tvAnnouncment, tvVoucher, tvDraft, tvFav, tvFriend, tvMasseurCert, tvOrg;
    private ConstraintLayout clMyOrder, clAnnouncment, clVoucher, clDraft, clFav, clFriend, clAddress, clYouzanOrder, clApply, clMyOrg;
    private HOTRSharePreference p;
    private boolean isLoaded = false;
    private QBadgeView messageBadgeView, noticeBadgeView;

    public static InfoFragment newInstance() {
        InfoFragment infoFragment = new InfoFragment();
        return infoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        p = HOTRSharePreference.getInstance(getActivity().getApplicationContext());

        ivAvatar = (ImageView) view.findViewById(R.id.iv_user_avatar);
        ivFav = (ImageView) view.findViewById(R.id.iv_fav);
        ivFollow = (ImageView) view.findViewById(R.id.iv_follow);
        ivSetting = (ImageView) view.findViewById(R.id.iv_setting);
        ivCert = (ImageView) view.findViewById(R.id.iv_certificate);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvCertified = (TextView) view.findViewById(R.id.tv_certified);
        tvCase = (TextView) view.findViewById(R.id.tv_case);
        tvPost = (TextView) view.findViewById(R.id.tv_post);
        tvMasseurCert = (TextView) view.findViewById(R.id.tv_masseur_cert);
        tvOrg = (TextView) view.findViewById(R.id.tv_my_organization);

        tvMyOrder = (TextView) view.findViewById(R.id.badge_my_order);
        tvAnnouncment = (TextView) view.findViewById(R.id.badge_announcement);
        tvVoucher = (TextView) view.findViewById(R.id.badge_voucher);
        tvDraft = (TextView) view.findViewById(R.id.badge_draft);
        tvFav = (TextView) view.findViewById(R.id.badge_fav);
        tvFriend = (TextView) view.findViewById(R.id.badge_friend);
        clMyOrder = (ConstraintLayout) view.findViewById(R.id.cl_my_order);
        clAnnouncment = (ConstraintLayout) view.findViewById(R.id.cl_announcement);
        clVoucher = (ConstraintLayout) view.findViewById(R.id.cl_voucher);
        clDraft = (ConstraintLayout) view.findViewById(R.id.cl_draft);
        clFav = (ConstraintLayout) view.findViewById(R.id.cl_fav);
        clFriend = (ConstraintLayout) view.findViewById(R.id.cl_friend);
        clAddress = (ConstraintLayout) view.findViewById(R.id.cl_address);
        clYouzanOrder = (ConstraintLayout) view.findViewById(R.id.cl_my_youzan_order);
        clApply = (ConstraintLayout) view.findViewById(R.id.cl_apply);
        clMyOrg = (ConstraintLayout) view.findViewById(R.id.cl_my_organization);


        messageBadgeView = new QBadgeView(getContext());
        noticeBadgeView = new QBadgeView(getContext());
        messageBadgeView
                .bindTarget(tvAnnouncment)
                .setBadgeGravity(Gravity.CENTER_VERTICAL | Gravity.END)
                .setBadgeBackgroundColor(getResources().getColor(R.color.red))
                .setShowShadow(false);
        noticeBadgeView
                .bindTarget(tvMyOrder)
                .setBadgeGravity(Gravity.CENTER_VERTICAL | Gravity.END)
                .setBadgeBackgroundColor(getResources().getColor(R.color.red))
                .setShowShadow(false);

        clMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OrderListActivity.class));
            }
        });
        clVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VoucherActivity.class));
            }
        });
        clAnnouncment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NoticeActivity.class));
            }
        });
        ivFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_FANS);
                i.putExtra(Constants.PARAM_TITLE, getString(R.string.my_follower));
                startActivity(i);
            }
        });
        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_FAVORITE);
                i.putExtra(Constants.PARAM_TITLE, getString(R.string.my_fav));
                startActivity(i);
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FriendActivity.class);
                startActivityForResult(i, Activity.RESULT_FIRST_USER);
            }
        });
        clFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FavoriteCategoryActivity.class));
            }
        });

        clAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DeliverAddressListActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.PARAM_TYPE, DeliverAddressListActivity.TYPE_SHOW_LIST);
                i.putExtras(b);
                startActivity(i);
            }
        });

        clFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), InviteFriendActivity.class);
                startActivity(i);
            }
        });

        clApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscriberListener mListener = new SubscriberListener<MasseurExtraData>() {
                    @Override
                    public void onNext(MasseurExtraData result) {
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeMasseurInfo(result);
                        if(result.getCheckState() == null){
                            Intent i = new Intent(getActivity(), IdentityActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(Constants.PARAM_TYPE, IdentityActivity.TYPE_NEW);
                            b.putString(Constants.PARAM_TITLE, getString(R.string.my_identity));
                            i.putExtras(b);
                            startActivity(i);
                        }else if (result.getCheckState() == 0){
                            Intent i = new Intent(getActivity(), AuditActivity.class);
                            Bundle b = new Bundle();
                            b.putString(Constants.PARAM_TITLE, getString(R.string.my_identity_audit));
                            i.putExtras(b);
                            startActivity(i);
                        }else if (result.getCheckState() == 2){
                            Intent i = new Intent(getActivity(), IdentityActivity.class);
                            Bundle b = new Bundle();
                            b.putInt(Constants.PARAM_TYPE, IdentityActivity.TYPE_NEW);
                            b.putString(Constants.PARAM_TITLE, getString(R.string.my_identity));
                            b.putSerializable(Constants.PARAM_DATA, result);
                            i.putExtras(b);
                            startActivity(i);
                        }else if (result.getCheckState() == 1){
                            loadData(Constants.LOAD_DIALOG);
                        }

                    }
                };
                ServiceClient.getInstance().masseurCheckState(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            }
        });

        clMyOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscriberListener mListener = new SubscriberListener<MasseurExtraData>() {
                    @Override
                    public void onNext(MasseurExtraData result) {
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeMasseurInfo(result);
                        switch (p.getMasseurInfo().getMassagistState()){
                            case 0:
                            case 3:
                                startActivityForResult(new Intent(getActivity(), SearchSpaActivity.class), 0);
                                break;
                            case 1:
                                if(result.getMassageName()!=null)
                                    tvOrg.setText(result.getMassageName());
                                Intent i = new Intent(getActivity(), LinkSpaActivity.class);
                                Bundle b = new Bundle();
                                b.putString(Constants.PARAM_TITLE, p.getMasseurInfo().getMassageName());
                                b.putLong(Constants.PARAM_ID, p.getMasseurInfo().getMassageId());
                                b.putString(Constants.PARAM_DATA, p.getMasseurInfo().getProductList());
                                i.putExtras(b);
                                startActivityForResult(i, 0);
                                break;
                            case 2:
                                i = new Intent(getActivity(), AuditActivity.class);
                                b = new Bundle();
                                b.putString(Constants.PARAM_TITLE, getString(R.string.connect_spa_audit));
                                i.putExtras(b);
                                startActivity(i);
                                break;
                        }
                    }
                };
                ServiceClient.getInstance().masseurCheckState(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            }
        });

        clYouzanOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ShopOrderActivity.class);
                Bundle b = new Bundle();
                b.putString(Constants.PARAM_TITLE, getString(R.string.youzan_order));
                b.putString(Constants.PARAM_DATA, Constants.YOUZAN_CLIENT_ORDER_URL);
                i.putExtras(b);
                startActivity(i);
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(new Intent(getActivity(), SettingActivity.class), 0);
            }
        });

        enableLoadMore(false);
        if(getUserVisibleHint() && !isLoaded){
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && !isLoaded) {
            loadData(Constants.LOAD_PAGE);
        }
    }

    @Subscribe
    public void getMessage(final Events.GetNoticeCount getNoticeCount) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getNoticeCount.getMessageCount()>0)
                    messageBadgeView.setBadgeNumber(getNoticeCount.getMessageCount());
                else
                    messageBadgeView.hide(false);
                if(getNoticeCount.getNoticeCount()>0)
                    noticeBadgeView.setBadgeNumber(getNoticeCount.getNoticeCount());
                else
                    noticeBadgeView.hide(false);
            }
        });

    }

    @Override
    public void loadData(int type) {
        SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<GetLoginResponse>() {
            @Override
            public void onNext(GetLoginResponse result) {
                isLoaded = true;
                if(result!=null && result.getUser() != null)
                    p.storeUserInfo(result.getUser());
                if(result!=null && result.getMasseurData() != null)
                    p.storeMasseurInfo(result.getMasseurData());
                updateUserInfo();
            }

            public void reload() {
                loadData(Constants.LOAD_PAGE);
            }
        };
        if(isAdded()) {
            if (type == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getMyDetail(new LoadingSubscriber(mListener, InfoFragment.this),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            if (type == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getMyDetail(new SilentSubscriber(mListener, getActivity(), refreshLayout),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
            if (type == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getMyDetail(new ProgressSubscriber(mListener, getActivity()),
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getUserID());
        }
    }

    public void updateUserInfo(){
        switch (p.getUserInfo().getUser_typ()){
            case 1:
                tvCertified.setVisibility(View.VISIBLE);
                tvCertified.setText(getString(R.string.normal_user));
                clMyOrg.setVisibility(View.GONE);
                clApply.setVisibility(View.VISIBLE);
                ivCert.setVisibility(View.INVISIBLE);
                tvTitle.setText(p.getUserInfo().getNickname());
                Glide.with(InfoFragment.this).load(p.getUserInfo().getHead_portrait()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
                break;
            case 5:
                tvCertified.setVisibility(View.VISIBLE);
                tvCertified.setText(getString(R.string.certificated_massuer));
                clMyOrg.setVisibility(View.VISIBLE);
                ivCert.setVisibility(View.VISIBLE);
                clApply.setVisibility(View.GONE);
                if(p.getMasseurInfo()!=null && p.getMasseurInfo().getMassagistName()!=null)
                    tvTitle.setText(p.getMasseurInfo().getMassagistName());
                if(p.getMasseurInfo()!=null && p.getMasseurInfo().getMassagistPhotos()!=null)
                    Glide.with(InfoFragment.this).load(Tools.getMainPhoto(p.getMasseurInfo().getMassagistPhotos())).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
                break;
            default:
                tvCertified.setVisibility(View.INVISIBLE);
                clMyOrg.setVisibility(View.GONE);
                ivCert.setVisibility(View.INVISIBLE);
                clApply.setVisibility(View.VISIBLE);

                tvTitle.setText(p.getUserInfo().getNickname());
                Glide.with(InfoFragment.this).load(p.getUserInfo().getHead_portrait()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
        }
        tvCase.setText(p.getUserInfo().getContrastPhotoCount()+"");
        tvPost.setText(p.getUserInfo().getHotTopicCount()+"");
        if(p.getMasseurInfo()!=null && p.getMasseurInfo().getMassageName()!=null)
            tvOrg.setText(p.getMasseurInfo().getMassageName());
        else
            tvOrg.setText(getString(R.string.connect_org));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            updateUserInfo();
        if(resultCode == SearchSpaActivity.CHANGE_SPA)
            loadData(Constants.LOAD_DIALOG);
    }
}
