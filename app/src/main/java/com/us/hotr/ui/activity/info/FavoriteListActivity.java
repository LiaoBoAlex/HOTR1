package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.DoctorListFragment;
import com.us.hotr.ui.fragment.beauty.HospitalListFragment;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.beauty.ProductListFragment;
import com.us.hotr.ui.fragment.massage.InterviewListFragment;
import com.us.hotr.ui.fragment.massage.MassageListFragment;
import com.us.hotr.ui.fragment.massage.MasseurBigListFragment;
import com.us.hotr.ui.fragment.massage.SpaBigListFragment;
import com.us.hotr.ui.fragment.party.PartyListFragment;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Mloong on 2017/9/22.
 */

public class FavoriteListActivity extends BaseActivity {

    private Fragment listFragment;
    private ConstraintLayout clEmpty;
    private FrameLayout container;
    private int type;
    private TextView tvEdit, tvDelete;

    private boolean isEditing = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(Constants.PARAM_TITLE);
        type = getIntent().getIntExtra(Constants.PARAM_TYPE, -1);
        setMyTitle(title);
        initStaticView();
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list_fav;
    }


    private void initStaticView(){
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        clEmpty = (ConstraintLayout) findViewById(R.id.cl_empty);
        container = (FrameLayout) findViewById(R.id.container);

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEditing){
                    tvDelete.setVisibility(View.VISIBLE);
                    tvEdit.setText(R.string.done);
                    isEditing = true;
                    Events.EnableEdit event = new Events.EnableEdit(Events.EnableEdit.ACTION_EDIT);
                    GlobalBus.getBus().post(event);
                }else{
                    tvDelete.setVisibility(View.GONE);
                    tvEdit.setText(R.string.edit);
                    isEditing = false;
                    Events.EnableEdit event = new Events.EnableEdit(Events.EnableEdit.ACTION_DONE);
                    GlobalBus.getBus().post(event);
                }
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDelete.setVisibility(View.GONE);
                tvEdit.setText(R.string.edit);
                isEditing = false;
                Events.EnableEdit event = new Events.EnableEdit(Events.EnableEdit.ACTION_DELETE);
                GlobalBus.getBus().post(event);
            }
        });
        switch (type){
            case Constants.TYPE_POST:
                listFragment = new PostListFragment().newInstance(null, true, Constants.TYPE_FAVORITE_POST, -1, -1);
                break;
            case Constants.TYPE_CASE:
                listFragment = new CaseListFragment().newInstance(null, true, true, -1 ,-1, -1);
                break;
            case Constants.TYPE_PRODUCT:
                listFragment = new ProductListFragment().newInstance(null, true, Constants.TYPE_FAVORITE, -1, -1, -1, -1, -1,-1);
                break;
            case Constants.TYPE_DOCTOR:
                listFragment = new DoctorListFragment().newInstance(null, Constants.TYPE_FAVORITE, -1, -1);
                break;
            case Constants.TYPE_HOSPITAL:
                listFragment = new HospitalListFragment().newInstance(null, true, -1);
                break;
            case Constants.TYPE_MASSAGE:
                listFragment = new MassageListFragment().newInstance(null, true, -1, -1 , -1);
                break;
            case Constants.TYPE_MASSEUR:
                listFragment = new MasseurBigListFragment().newInstance(-1, -1);
                break;
            case Constants.TYPE_SPA:
                listFragment = new SpaBigListFragment().newInstance(-1);
                break;
            case Constants.TYPE_INTERVIEW:
                listFragment = new InterviewListFragment().newInstance();
                break;
            case Constants.TYPE_PARTY:
                listFragment = new PartyListFragment().newInstance(null, true);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
    }

    @Subscribe
    public void getMessage(Events.GetSearchCount getSearchCount) {
        if(getSearchCount.getSearchCount() == 0){
            clEmpty.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
        }else{
            clEmpty.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }

    }
}