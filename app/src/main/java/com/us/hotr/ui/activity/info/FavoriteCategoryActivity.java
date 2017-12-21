package com.us.hotr.ui.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;

/**
 * Created by Mloong on 2017/9/22.
 */

public class FavoriteCategoryActivity extends BaseActivity implements View.OnClickListener{
    private ConstraintLayout clBeauty, clMassage, clParty, clCase, clPost, clNews, clInterview;
    @Override
    protected int getLayout() {
        return R.layout.activity_favorite_category;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.fav_title);

        clBeauty = (ConstraintLayout) findViewById(R.id.cl_beauty);
        clMassage = (ConstraintLayout) findViewById(R.id.cl_massage);
        clParty = (ConstraintLayout) findViewById(R.id.cl_party);
        clCase = (ConstraintLayout) findViewById(R.id.cl_case);
        clPost = (ConstraintLayout) findViewById(R.id.cl_post);
        clNews = (ConstraintLayout) findViewById(R.id.cl_news);
        clInterview = (ConstraintLayout) findViewById(R.id.cl_interview);
        clBeauty.setOnClickListener(this);
        clMassage.setOnClickListener(this);
        clParty.setOnClickListener(this);
        clCase.setOnClickListener(this);
        clPost.setOnClickListener(this);
//        clNews.setOnClickListener(this);
        clInterview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int type = -1;
        String title = "";
        switch (view.getId()){
            case R.id.cl_beauty:
                type = Constants.TYPE_PRODUCT;
                title = getString(R.string.beauty_title);
                break;
            case R.id.cl_case:
                type = Constants.TYPE_CASE;
                title = getString(R.string.compare_title);
                break;
            case R.id.cl_post:
                type = Constants.TYPE_POST;
                title = getString(R.string.post_title);
                break;
            case R.id.cl_massage:
                type = Constants.TYPE_MASSAGE;
                title = getString(R.string.massage_title);
                break;
            case R.id.cl_interview:
                type = Constants.TYPE_INTERVIEW;
                title = getString(R.string.interview_title);
                break;
            case R.id.cl_party:
                type = Constants.TYPE_PARTY;
                title = getString(R.string.party_title);
                break;
        }
        Intent i = new Intent(this, FavoriteListActivity.class);
        i.putExtra(Constants.PARAM_TYPE, type);
        i.putExtra(Constants.PARAM_TITLE, title);
        startActivity(i);
    }
}
