package com.us.hotr.ui.activity.beauty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.fragment.beauty.SelectSubjectFragment;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Mloong on 2017/9/4.
 */

public class SelectSubjectActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(getIntent().getStringExtra(Constants.PARAM_TITLE));
        initStaticView();

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_subject;
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    private void initStaticView(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = new SelectSubjectFragment().newInstance(false);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment).commit();
    }

    @Subscribe
    public void getMessage(Events.SubjectSelected subjectSelected) {
        Intent i = new Intent(SelectSubjectActivity.this, SubjectActivity.class);
        Bundle b = new Bundle();
        b.putString(Constants.PARAM_NAME, subjectSelected.getSelectedSubject());
        b.putInt(Constants.PARAM_ID, subjectSelected.getSubjectId());
        i.putExtras(b);
        startActivity(i);

    }
}
