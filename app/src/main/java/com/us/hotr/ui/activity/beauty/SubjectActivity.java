package com.us.hotr.ui.activity.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import android.support.design.widget.DisableableAppBarLayoutBehavior;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.bean.SubjectDetail;
import com.us.hotr.ui.activity.BaseLoadingActivity;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.ProductListWithFilterFragment;
import com.us.hotr.ui.fragment.beauty.SelectSubjectFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Mloong on 2017/9/6.
 */

public class SubjectActivity extends BaseLoadingActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private AppBarLayout appBarLayout;
    private FrameLayout mContainer;
    private CoordinatorLayout mContent;
    private TextView tvTitle, tvDescption, tvTreatment, tvPrice, tvPeroid, tvTimes, tvKnowMore;

    private ArrayList<String> titleList;
    private ArrayList<Fragment> fragmentList;
    private ProductListWithFilterFragment productListFragment;
    private int subjectId;

    public boolean isSubjectListOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subjectId = getIntent().getExtras().getInt(Constants.PARAM_ID);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_subject;
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    private void initStaticView(){

        titleList = new ArrayList<String>() {{
            add(getString(R.string.product_title));
            add(getString(R.string.case_title));
        }};

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        mContainer = (FrameLayout) findViewById(R.id.container);
        mContent = (CoordinatorLayout) findViewById(R.id.content);
        tvTitle = (TextView) findViewById(R.id.tb_title);
        tvDescption = (TextView) findViewById(R.id.tv_description);
        tvTreatment = (TextView) findViewById(R.id.tv_treatment);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvPeroid = (TextView) findViewById(R.id.tv_peroid);
        tvTimes = (TextView) findViewById(R.id.tv_times);
        tvKnowMore = (TextView) findViewById(R.id.tv_know_more);

        final Fragment selectSubjectFragment = new SelectSubjectFragment().newInstance(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, selectSubjectFragment).commit();

        tvTitle.setText(getIntent().getExtras().getString(Constants.PARAM_NAME));
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productListFragment.isCityListOpen || productListFragment.isTypeListOpen){
                    productListFragment.hideAnimation();
                }else {
                    if (!isSubjectListOpen) {
                        showAnimation();
                    } else {
                        hideAnimation();
                    }
                }
            }
        });

        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager, true);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(productListFragment.isCityListOpen && tab.getPosition() == 1)
                    productListFragment.hideAnimation();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        enableLoadMore(false);
        mContainer.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    public void loadData(int loadType){
        if(productListFragment == null) {
            productListFragment = ProductListWithFilterFragment.newInstance(Constants.TYPE_PRODUCT, false, subjectId);
            fragmentList = new ArrayList<Fragment>() {{
                add(productListFragment);
                add(CaseListFragment.newInstance(false));
            }};
            adapter = new PagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
            viewPager.setAdapter(adapter);
        }
        else{
            GlobalBus.getBus().post(new Events.Refresh());
            viewPager.setCurrentItem(0);
        }

        SubscriberListener mListener = new SubscriberListener<SubjectDetail>() {
            @Override
            public void onNext(SubjectDetail result) {
                tvDescption.setText(result.getProjectIntroduction());
                tvPeroid.setText(result.getRestoreCycle());
                tvPrice.setText(result.getPrice_range());
                tvTimes.setText(result.getTreatmentTimes());
                tvTreatment.setText(result.getTreatmentMethosDesc());
            }
        };
        if(loadType == Constants.LOAD_PULL_REFRESH)
            ServiceClient.getInstance().getSubjectDetail(new SilentSubscriber(mListener, this, refreshLayout), subjectId);
        if(loadType == Constants.LOAD_PAGE)
            ServiceClient.getInstance().getSubjectDetail(new LoadingSubscriber(mListener, this), subjectId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getMessage(Events.SubjectSelected subjectSelected) {
        tvTitle.setText(subjectSelected.getSelectedSubject());
        subjectId = subjectSelected.getSubjectId();
        hideAnimation();
        loadData(Constants.LOAD_PAGE);

    }

    public void hideHeader(){
        appBarLayout.setExpanded(false, true);
    }

    public void showAnimation(){

        Animation animIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_menu_in);
        animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mContainer.setVisibility(View.VISIBLE);
                mContent.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContainer.setVisibility(View.VISIBLE);
        mContainer.startAnimation(animIn);
        isSubjectListOpen = true;
    }

    public void hideAnimation(){

        Animation animOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_menu_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mContainer.setVisibility(View.GONE);
                mContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContainer.startAnimation(animOut);
        isSubjectListOpen = false;
    }

    public void enableAppBarLayoutBehavior(boolean value){
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((DisableableAppBarLayoutBehavior) layoutParams.getBehavior()).setEnabled(value);
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
