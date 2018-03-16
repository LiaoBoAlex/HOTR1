package com.us.hotr.ui.fragment.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.us.hotr.R;
import com.us.hotr.ui.activity.search.SearchHintActivity;

import java.util.ArrayList;

/**
 * Created by Mloong on 2017/8/28.
 */

public class FoundFragment extends Fragment{

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private ImageView imgSearch;

    public static FoundFragment newInstance() {
        FoundFragment foundFragment = new FoundFragment();
        return foundFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_found, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleList = new ArrayList<String>() {{
            add(getString(R.string.popular_post_title));
            add(getString(R.string.group_title));
        }};

        fragmentList = new ArrayList<Fragment>() {{
            add(PopularPostFragment.newInstance());
            add(GroupFragment.newInstance());
        }};

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        imgSearch = (ImageView) view.findViewById(R.id.img_search);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchHintActivity.class));
            }
        });

        adapter = new PagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
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
