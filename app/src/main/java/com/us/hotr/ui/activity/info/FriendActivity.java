package com.us.hotr.ui.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.User;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.info.PersonalIntroFragment;

import java.util.ArrayList;

/**
 * Created by Mloong on 2017/9/21.
 */

public class FriendActivity extends BaseLoadingActivity {
    public static final String PARAM_IS_SELF = "PARAM_IS_SELF";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ImageView ivAdd, ivMsg, ivAvatar;
    private TextView tvFollow, tvFollowedBy, tvName;
    private RefreshLayout refreshLayout;

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private boolean iSelf = false;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iSelf = getIntent().getExtras().getBoolean(PARAM_IS_SELF, false);
        mUser = (User)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        initStaticView();
    }

    @Override
    protected void loadData(int type) {

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
        ivAvatar = (ImageView) findViewById(R.id.iv_product_avatar);
        tvFollow = (TextView) findViewById(R.id.tv_product_fav);
        tvFollowedBy = (TextView) findViewById(R.id.tv_followed_by);
        tvName = (TextView) findViewById(R.id.tv_name);

        enableLoadMore(false);
        if(iSelf){
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
            Glide.with(this).load(mUser.getHead_portrait()).error(R.drawable.placeholder_post3).placeholder(R.drawable.placeholder_post3).into(ivAvatar);
            tvFollow.setText(String.format(getString(R.string.follow_count), mUser.getAttentionCount()));
            tvFollowedBy.setText(String.format(getString(R.string.followed_by_count), mUser.getFanCount()));
            tvName.setText(mUser.getNickname());
        }else{
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            ivMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        titleList = new ArrayList<String>() {{
            add(getString(R.string.home_title));
            add(getString(R.string.compare_title));
            add(getString(R.string.post_title));
        }};

        fragmentList = new ArrayList<Fragment>() {{
            add(PersonalIntroFragment.newInstance(mUser));
            add(CaseListFragment.newInstance(false));
            add(PostListFragment.newInstance(false));
        }};

        adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    public class PagerAdapter extends FragmentPagerAdapter {

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
