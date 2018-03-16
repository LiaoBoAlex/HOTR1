package com.us.hotr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Product;
import com.us.hotr.ui.activity.beauty.ProductActivity;
import com.us.hotr.ui.fragment.ImageViewerFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/22.
 */

public class ImageViewerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private PagerAdapter myAdapter;
    private ConstraintLayout clProduct;
    private TextView tvPage, tvTitle, tvPrice;
    private List<String> mPhotoes;
    private List<Integer> messageList;
    private Product product;
    private int index;
    private int type;
    private String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        index = getIntent().getExtras().getInt(Constants.PARAM_ID, 0);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE);
        if(type == Constants.TYPE_CHAT) {
            messageList = (List<Integer>) getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
            userId = getIntent().getExtras().getString(Constants.PARAM_NAME);
        }
        else
            mPhotoes = (List<String>)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        if(type == Constants.TYPE_CASE)
            product = (Product) getIntent().getExtras().getSerializable(Constants.PARAM_PRODUCT_ID);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        clProduct = (ConstraintLayout) findViewById(R.id.cl_product);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvPage = (TextView) findViewById(R.id.tv_page);
        switch (type){
            case Constants.TYPE_CASE:
                if(product!=null) {
                    clProduct.setVisibility(View.VISIBLE);
                    tvTitle.setText(getString(R.string.bracket_left) + product.getProduct_name() + getString(R.string.bracket_right) + product.getProduct_usp());
                    tvPrice.setText(getString(R.string.money)+new DecimalFormat("0.00").format(product.getOnline_price()));
                    clProduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ImageViewerActivity.this, ProductActivity.class);
                            Bundle b = new Bundle();
                            b.putLong(Constants.PARAM_ID, product.getProductId());
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                }else
                    clProduct.setVisibility(View.GONE);
                break;
            case Constants.TYPE_CHAT:
            case Constants.TYPE_DOCTOR:
            case Constants.TYPE_HOSPITAL:
            case Constants.TYPE_MASSAGE:
            case Constants.TYPE_POST:
                clProduct.setVisibility(View.GONE);
                break;
        }
        ArrayList<Fragment> fragmentList = new ArrayList<>();

        int size = 0;
        if(type == Constants.TYPE_CHAT){
            if (messageList != null && messageList.size() > 0) {
                size = messageList.size();
                for (int i=0;i<messageList.size();i++) {
                    fragmentList.add(ImageViewerFragment.newInstance(messageList.get(i), userId, type));
                    if(index == messageList.get(i))
                        index = i;
                }
            }
        }else {
            if (mPhotoes != null && mPhotoes.size() > 0) {
                size = mPhotoes.size();
                for (String s : mPhotoes)
                    fragmentList.add(ImageViewerFragment.newInstance(s, type));
            }
        }
        if(fragmentList.size()>0) {
            myAdapter = new PagerAdapter(getSupportFragmentManager(), fragmentList);
            mViewPager.setAdapter(myAdapter);
            mViewPager.setCurrentItem(index);
            tvPage.setText((index + 1) + " / " + size);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    if(type == Constants.TYPE_CHAT)
                        tvPage.setText((i + 1) + " / " + messageList.size());
                    else
                        tvPage.setText((i + 1) + " / " + mPhotoes.size());
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });
        }
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {

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
