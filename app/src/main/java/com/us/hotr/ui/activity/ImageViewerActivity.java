package com.us.hotr.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.fragment.ImageViewerFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/22.
 */

public class ImageViewerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private PagerAdapter myAdapter;
    private ConstraintLayout clProduct;
    private TextView tvPage;
    private List<String> mPhotoes;
    private int index;
    private int type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mPhotoes = (List<String>)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        index = getIntent().getExtras().getInt(Constants.PARAM_ID, 0);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        clProduct = (ConstraintLayout) findViewById(R.id.cl_product);
        tvPage = (TextView) findViewById(R.id.tv_page);
        switch (type){
            case Constants.TYPE_CASE:
                clProduct.setVisibility(View.VISIBLE);
                break;
            case Constants.TYPE_DOCTOR:
            case Constants.TYPE_HOSPITAL:
            case Constants.TYPE_MASSAGE:
                clProduct.setVisibility(View.GONE);
                break;
        }
        if(mPhotoes!=null && mPhotoes.size()>0) {
            ArrayList<Fragment> fragmentList = new ArrayList<>();
            for(String s:mPhotoes)
                fragmentList.add(ImageViewerFragment.newInstance(s));
            myAdapter = new PagerAdapter(getSupportFragmentManager(), fragmentList);
            mViewPager.setAdapter(myAdapter);
            mViewPager.setCurrentItem(index);
            tvPage.setText((index+1) + " / " + mPhotoes.size());
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    tvPage.setText((i+1) + " / " + mPhotoes.size());
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });
        }

    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragmentList;

        public PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
            super(fm);
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
            return null;
        }
    }
}
