package com.us.hotr.ui.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.info.PersonalIntroFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.response.BaseListResponse;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mloong on 2017/9/21.
 */

public class FriendActivity extends BaseLoadingActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ImageView ivAdd, ivMsg, ivAvatar, ivCertified;
    private TextView tvFollow, tvFollowedBy, tvName;

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private PersonalIntroFragment personalIntroFragment;

    private boolean iSelf = false, isFav = false;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo();
        ivShare.setVisibility(View.GONE);
        initStaticView();
        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int type) {
        if (getIntent().getExtras() == null) {
            mUser = HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo();
            iSelf = true;
            loadUserInfo();
        } else if (HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo() != null
                && getIntent().getExtras().getLong(Constants.PARAM_ID) == HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo().getUserId()) {
            mUser = HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo();
            iSelf = true;
            loadUserInfo();
        } else {
            SubscriberListener mListener = new SubscriberListener<User>() {
                @Override
                public void onNext(User result) {
                    mUser = result;
                    iSelf = false;
                    loadUserInfo();
                }
            };
            if (type == Constants.LOAD_PAGE)
                ServiceClient.getInstance().getUserDetail(new LoadingSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), getIntent().getExtras().getLong(Constants.PARAM_ID));
            else if (type == Constants.LOAD_DIALOG)
                ServiceClient.getInstance().getUserDetail(new ProgressSubscriber(mListener, this),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), getIntent().getExtras().getLong(Constants.PARAM_ID));
            else if (type == Constants.LOAD_PULL_REFRESH)
                ServiceClient.getInstance().getUserDetail(new SilentSubscriber(mListener, this, refreshLayout),
                        HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), getIntent().getExtras().getLong(Constants.PARAM_ID));
        }
    }

    private void loadUserInfo(){
        personalIntroFragment = PersonalIntroFragment.newInstance(mUser);
        fragmentList = new ArrayList<Fragment>() {{
            add(personalIntroFragment);
            add(CaseListFragment.newInstance(mUser.getUserId()));
            add(PostListFragment.newInstance(mUser.getUserId()));
        }};

        adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
        updateUserInfo();
    }

    private void updateUserInfo(){
        Glide.with(this).load(mUser.getHead_portrait()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
        tvFollow.setText(String.format(getString(R.string.follow_count), mUser.getAttentionCount()));
        tvFollowedBy.setText(String.format(getString(R.string.followed_by_count), mUser.getFanCount()));
        tvName.setText(mUser.getNickname());
        switch (mUser.getUser_typ()){
            case 1:
            case 7:
                ivCertified.setVisibility(View.INVISIBLE);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                ivCertified.setVisibility(View.VISIBLE);
                ivCertified.setImageResource(R.mipmap.ic_certified);
                break;
            case 6:
                ivCertified.setVisibility(View.VISIBLE);
                ivCertified.setImageResource(R.mipmap.ic_offical);
                break;
        }
        if(iSelf) {
            personalIntroFragment.updateUserInfo(HOTRSharePreference.getInstance(getApplicationContext()).getUserInfo());
            adapter.notifyDataSetChanged();
        }
        if(iSelf) {
            enablePullDownRefresh(false);
            ivMsg.setVisibility(View.INVISIBLE);
            ivAdd.setImageResource(R.mipmap.ic_edit);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(FriendActivity.this, EditInfoActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.PARAM_DATA, mUser);
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }else{
            isFav = mUser.getIs_attention() ==1?true:false;
            if(isFav)
                ivAdd.setImageResource(R.mipmap.ic_click);
            else
                ivAdd.setImageResource(R.mipmap.ic_add);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isFav) {
                        SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<String>() {
                            @Override
                            public void onNext(String result) {
                                isFav = true;
                                Tools.Toast(FriendActivity.this, getString(R.string.fav_people_success));
                                ivAdd.setImageResource(R.mipmap.ic_click);
                            }
                            @Override
                            public void reload() {
                                loadData(Constants.LOAD_DIALOG);
                            }
                        };
                        ServiceClient.getInstance().favoritePeople(new ProgressSubscriber(mListener, FriendActivity.this),
                                HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), mUser.getUserId());
                    }else{
                        SubscriberListener mListener = new SubscriberListener<String>() {
                            @Override
                            public void onNext(String result) {
                                isFav = false;
                                Tools.Toast(FriendActivity.this, getString(R.string.remove_fav_people_success));
                                ivAdd.setImageResource(R.mipmap.ic_add);
                            }
                        };
                        ServiceClient.getInstance().deleteFavoritePeople(new ProgressSubscriber(mListener, FriendActivity.this),
                                HOTRSharePreference.getInstance(getApplicationContext()).getUserID(), mUser.getUserId());
                    }

                }
            });
            ivMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(FriendActivity.this, ChatActivity.class);
                    Bundle b = new Bundle();
                    b.putLong(Constants.PARAM_DATA, mUser.getUserId());
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_friend;
    }

    private void initStaticView(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivMsg = (ImageView) findViewById(R.id.iv_msg);
        ivAvatar = (ImageView) findViewById(R.id.iv_user_avatar);
        tvFollow = (TextView) findViewById(R.id.tv_address);
        tvFollowedBy = (TextView) findViewById(R.id.tv_followed_by);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivCertified = (ImageView) findViewById(R.id.iv_certified);

        enableLoadMore(false);
        if(iSelf){
            enablePullDownRefresh(false);
            ivMsg.setVisibility(View.INVISIBLE);
            ivAdd.setImageResource(R.mipmap.ic_edit);
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(FriendActivity.this, EditInfoActivity.class);
                    startActivityForResult(i, 0);
                }
            });
        }

        titleList = new ArrayList<String>() {{
            add(getString(R.string.home_title));
            add(getString(R.string.compare_title));
            add(getString(R.string.post_title));
        }};
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            updateUserInfo();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<String> titleList;
        private ArrayList<Fragment> fragmentList;

        public PagerAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
