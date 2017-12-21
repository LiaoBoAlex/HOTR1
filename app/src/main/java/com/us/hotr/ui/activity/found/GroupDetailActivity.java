package com.us.hotr.ui.activity.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.activity.post.UploadPostActivity1;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.found.GroupDetailFragment;

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
    private FloatingActionButton fabPost;

    @Override
    protected int getLayout() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.all_group);
        initStaticView();
    }

    private void initStaticView(){
        titleList = new ArrayList<String>() {
            {
                add(getString(R.string.all));
                add(getString(R.string.fresh));
                add(getString(R.string.hot_in_the_week));
            }};

        fragmentList = new ArrayList<Fragment>() {{
            add(GroupDetailFragment.newInstance());
            add(PostListFragment.newInstance(true));
            add(PostListFragment.newInstance(true));
        }};

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        fabPost = (FloatingActionButton) findViewById(R.id.fab_post);

        adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager, true);
        fabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroupDetailActivity.this, UploadPostActivity1.class));
            }
        });
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
