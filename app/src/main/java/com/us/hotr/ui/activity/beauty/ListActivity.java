package com.us.hotr.ui.activity.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Party;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.fragment.beauty.DoctorListFragment;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.beauty.ProductListFragment;
import com.us.hotr.ui.fragment.found.OfficialPostListFragment;
import com.us.hotr.ui.fragment.massage.MasseurListFragment;
import com.us.hotr.ui.fragment.party.PartyListFragment;

import java.util.List;

/**
 * Created by liaobo on 2017/12/11.
 */

public class ListActivity extends BaseActivity {
    private int type;
    private Fragment listFragment;
    private long hospitalId = -1, doctorId = -1, spaId = -1, userId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getExtras().getString(Constants.PARAM_TITLE);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE, -1);
        hospitalId = getIntent().getExtras().getLong(Constants.PARAM_HOSPITAL_ID, -1);
        doctorId = getIntent().getExtras().getLong(Constants.PARAM_DOCTOR_ID, -1);
        spaId = getIntent().getExtras().getLong(Constants.PARAM_SPA_ID, -1);
        userId = getIntent().getExtras().getLong(Constants.PARAM_USER_ID, -1);
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
            case Constants.TYPE_MY_DOCTOR:
                listFragment = new DoctorListFragment().newInstance(null, type, -1, hospitalId);
                break;
            case Constants.TYPE_PRODUCT:
            case Constants.TYPE_MY_PRODUCT:
                listFragment = new ProductListFragment().newInstance(null, true, type, -1, -1, -1,-1, hospitalId, doctorId);
                break;
            case Constants.TYPE_MASSEUR:
                listFragment = new MasseurListFragment().newInstance(null, -1, spaId);
                break;
            case Constants.TYPE_PARTY:
                listFragment = new PartyListFragment().newInstance((List<Party>)getIntent().getExtras().getSerializable(Constants.PARAM_DATA));
                break;
            case Constants.TYPE_OFFICIAL_POST:
                listFragment = new OfficialPostListFragment().newInstance();
                break;
            case Constants.TYPE_POST:
                listFragment = new PostListFragment().newInstance(userId);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
    }
}
