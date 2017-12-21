package com.us.hotr.ui.activity.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.PostListFragment;
import com.us.hotr.ui.fragment.beauty.ProductListFragment;
import com.us.hotr.ui.fragment.massage.InterviewListFragment;
import com.us.hotr.ui.fragment.massage.MassageListFragment;
import com.us.hotr.ui.fragment.party.PartyListFragment;

/**
 * Created by Mloong on 2017/9/22.
 */

public class FavoriteListActivity extends BaseActivity {

    private Fragment listFragment;
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
    protected int getLayout() {
        return R.layout.activity_list_fav;
    }


    private void initStaticView(){
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvDelete = (TextView) findViewById(R.id.tv_delete);

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
                Events.EnableEdit event = new Events.EnableEdit(Events.EnableEdit.ACTION_DELETE);
                GlobalBus.getBus().post(event);
            }
        });
        switch (type){
            case Constants.TYPE_POST:
                listFragment = new PostListFragment().newInstance(true);
                break;
            case Constants.TYPE_CASE:
                listFragment = new CaseListFragment().newInstance(true);
                break;
            case Constants.TYPE_PRODUCT:
                listFragment = new ProductListFragment().newInstance(true, -1, -1, -1,-1);
                break;
            case Constants.TYPE_MASSAGE:
                listFragment = new MassageListFragment().newInstance(-1, -1 , -1);
                break;
            case Constants.TYPE_INTERVIEW:
                listFragment = new InterviewListFragment().newInstance();
                break;
            case Constants.TYPE_PARTY:
                listFragment = new PartyListFragment().newInstance();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
    }
}