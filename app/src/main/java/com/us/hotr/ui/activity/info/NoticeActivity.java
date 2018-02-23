package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.ViewGroup;

import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.fragment.info.NoticeCommentFragment;
import com.us.hotr.ui.fragment.info.NoticeFriendFragment;
import com.us.hotr.ui.fragment.info.NoticeLikeFragment;
import com.us.hotr.ui.fragment.info.NoticeMessageFragment;
import com.us.hotr.ui.fragment.info.NoticeNoticeFragment;

import java.util.ArrayList;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Mloong on 2017/9/20.
 */

public class NoticeActivity extends BaseActivity {

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.notice_title);
        initStaticView();
    }

    private void initStaticView(){

        titleList = new ArrayList<String>() {{
            add(getString(R.string.notice_message));
            add(getString(R.string.notice_comment));
            add(getString(R.string.notice_like));
            add(getString(R.string.notice_friend));
            add(getString(R.string.notice_notice));
        }};

        fragmentList = new ArrayList<Fragment>() {{
            add(NoticeMessageFragment.newInstance());
            add(NoticeCommentFragment.newInstance());
            add(NoticeLikeFragment.newInstance());
            add(NoticeFriendFragment.newInstance());
            add(NoticeNoticeFragment.newInstance());
        }};

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);

        new QBadgeView(this)
                .bindTarget(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0))
                .setBadgeGravity(Gravity.TOP | Gravity.END)
                .setBadgeBackgroundColor(getResources().getColor(R.color.red))
                .setShowShadow(false)
                .setBadgeNumber(42);
        new QBadgeView(this)
                .bindTarget(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(4))
                .setBadgeGravity(Gravity.TOP | Gravity.END)
                .setBadgeBackgroundColor(getResources().getColor(R.color.red))
                .setShowShadow(false)
                .setBadgeNumber(2);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_voucher;
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
