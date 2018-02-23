package com.us.hotr.ui.activity.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Group;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.post.UploadPostActivity1;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.found.GroupDetailFragment;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.ProgressSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;
import com.us.hotr.webservice.rxjava.SubscriberWithReloadListener;

import java.util.ArrayList;

/**
 * Created by Mloong on 2017/11/1.
 */

public class GroupDetailActivity extends BaseActivity {

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private TextView tvFav;
    private FloatingActionButton fabPost;

    private Group mGroup;
    private long mGroupId;
    private boolean isReload = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isReload = false;
        mGroup = (Group)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        mGroupId = getIntent().getExtras().getLong(Constants.PARAM_ID);
        initStaticView();
        if(mGroup == null)
            getMyGroup();
        else {
            mGroupId = mGroup.getId();
            loadData();
        }


    }

    private void getMyGroup(){
            SubscriberListener mListener = new SubscriberListener<Group>() {
                @Override
                public void onNext(Group result) {
                    mGroup = result;
                    loadData();
                }
            };
            ServiceClient.getInstance().getGroupById(new SilentSubscriber(mListener, GroupDetailActivity.this, null),
                    mGroupId, HOTRSharePreference.getInstance(GroupDetailActivity.this.getApplicationContext()).getUserID());
    }

    private void initStaticView(){


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        fabPost = (FloatingActionButton) findViewById(R.id.fab_post);
        tvFav = (TextView) findViewById(R.id.tv_fav);

        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager, true);
        fabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GroupDetailActivity.this, UploadPostActivity1.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, mGroupId);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    private void loadData(){
        setMyTitle(mGroup.getCoshow_name());
        if(mGroup.getIs_attention() == 1){
            tvFav.setText(R.string.fav_ed);
            tvFav.setTextColor(getResources().getColor(R.color.text_grey2));
        }else{
            tvFav.setText(R.string.fav);
            tvFav.setTextColor(getResources().getColor(R.color.text_black));
            tvFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteGroup(mGroup.getId());
                }
            });
        }

        titleList = new ArrayList<String>() {
            {
                add(getString(R.string.all));
                add(getString(R.string.fresh));
                add(getString(R.string.hot_in_the_week));
            }};

        fragmentList = new ArrayList<Fragment>() {{
            add(GroupDetailFragment.newInstance(mGroup));
            add(PostListFragment.newInstance(null, true, Constants.TYPE_POST, mGroup.getId(), 8));
            add(PostListFragment.newInstance(null, true, Constants.TYPE_POST, mGroup.getId(), 7));
        }};

        adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);
    }

    private void favoriteGroup(final long id){
        SubscriberWithReloadListener mListener = new SubscriberWithReloadListener<String>() {
            @Override
            public void onNext(String result) {
                Tools.Toast(GroupDetailActivity.this, getString(R.string.fav_people_success));
                isReload = true;
                tvFav.setText(R.string.fav_ed);
                tvFav.setTextColor(getResources().getColor(R.color.text_grey2));
                tvFav.setOnClickListener(null);

            }

            @Override
            public void reload() {
                loadData();
            }
        };
        ServiceClient.getInstance().favoriteGroup(new ProgressSubscriber(mListener, this),
                HOTRSharePreference.getInstance(this.getApplicationContext()).getUserID(), id, 1);
    }

    @Override
    public void onBackPressed() {
        if(isReload)
            setResult(RESULT_OK);
        super.onBackPressed();
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
