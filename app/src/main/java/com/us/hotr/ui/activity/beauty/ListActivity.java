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
import com.us.hotr.ui.fragment.beauty.DoctorListFragment;
import com.us.hotr.ui.fragment.beauty.ListWithFilterFragment;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.beauty.ProductListFragment;
import com.us.hotr.ui.fragment.found.OfficialPostListFragment;
import com.us.hotr.ui.fragment.info.FriendListFragment;
import com.us.hotr.ui.fragment.massage.InterviewListFragment;
import com.us.hotr.ui.fragment.massage.MasseurListFragment;

/**
 * Created by liaobo on 2017/12/11.
 */

public class ListActivity extends BaseActivity {
    private int type;
    private Fragment listFragment;
    private int hospitalId = -1, doctorId = -1, spaId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(Constants.PARAM_TITLE);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE, -1);
        hospitalId = getIntent().getExtras().getInt(Constants.PARAM_HOSPITAL_ID, -1);
        doctorId = getIntent().getExtras().getInt(Constants.PARAM_DOCTOR_ID, -1);
        spaId = getIntent().getExtras().getInt(Constants.PARAM_SPA_ID, -1);
        setMyTitle(title);
        initStaticView();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list;
    }

    private void initStaticView(){
        ivSearch = (ImageView) findViewById(R.id.img_search);
        switch (type){
            case Constants.TYPE_DOCTOR:
                listFragment = new DoctorListFragment().newInstance(-1, hospitalId);
                break;
            case Constants.TYPE_PRODUCT:
                listFragment = new ProductListFragment().newInstance(true, -1,-1, hospitalId, doctorId);
                break;
            case Constants.TYPE_MASSEUR:
                listFragment = new MasseurListFragment().newInstance(-1, spaId);
                break;
            case Constants.TYPE_SPA:
            case Constants.TYPE_MASSAGE:
            case Constants.TYPE_FRIEND:
            case Constants.TYPE_MY_PRODUCT:
            case Constants.TYPE_CASE:
            case Constants.TYPE_INTERVIEW:
            case Constants.TYPE_POST:
            case Constants.TYPE_OFFICIAL_POST:
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
    }
}
