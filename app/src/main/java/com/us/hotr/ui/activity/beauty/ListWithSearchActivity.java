package com.us.hotr.ui.activity.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.ListWithFilterFragment;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.beauty.ProductListFragment;
import com.us.hotr.ui.fragment.found.OfficialPostListFragment;
import com.us.hotr.ui.fragment.info.FriendListFragment;
import com.us.hotr.ui.fragment.massage.InterviewListFragment;

/**
 * Created by Mloong on 2017/9/5.
 */

public class ListWithSearchActivity extends BaseActivity {

    private Fragment listFragment;
    private int type;
    private int subjectId;
    private ImageView ivSearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(Constants.PARAM_TITLE);
        type = getIntent().getIntExtra(Constants.PARAM_TYPE, -1);
        subjectId = getIntent().getIntExtra(Constants.PARAM_ID, -1);
        setMyTitle(title);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list_search;
    }


    private void initStaticView(){
        ivSearch = (ImageView) findViewById(R.id.img_search);
        switch (type){
            case Constants.TYPE_DOCTOR:
            case Constants.TYPE_HOSPITAL:
            case Constants.TYPE_PRODUCT:
            case Constants.TYPE_SPA:
            case Constants.TYPE_MASSEUR:
            case Constants.TYPE_MASSAGE:
                listFragment = new ListWithFilterFragment().newInstance(null, type, subjectId, true);
                break;
            case Constants.TYPE_FANS:
            case Constants.TYPE_FAVORITE:
                listFragment = new FriendListFragment().newInstance(null, type);
                ivSearch.setVisibility(View.GONE);
                break;
            case Constants.TYPE_CASE:
                listFragment = new CaseListFragment().newInstance(null, true, false);
                break;
            case Constants.TYPE_INTERVIEW:
                listFragment = new InterviewListFragment().newInstance();
                break;
            case Constants.TYPE_POST:
                listFragment = new PostListFragment().newInstance(null, true, Constants.TYPE_POST, -1, -1);

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
    }
}

