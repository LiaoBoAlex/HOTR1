package com.us.hotr.ui.activity;

import android.app.Activity;
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

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.fragment.SelectCityFragment;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Mloong on 2017/8/28.
 */

public class SelectCityActivity extends BaseActivity {

    private TextView tvCurrentCity;
    private HOTRSharePreference p;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(R.string.city_list);
        p = HOTRSharePreference.getInstance(getApplicationContext());
        initStaticView();

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_city;
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

    @Subscribe
    public void getMessage(Events.CitySelected citySelectedEvent) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.PARAM_NAME, citySelectedEvent.getSelectedCity());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    private void initStaticView(){

        tvCurrentCity = (TextView) findViewById(R.id.tv_current_city);
        tvCurrentCity.setText(p.getCurrentCityName());
        tvCurrentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.storeSelectedCityName("");
                p.storeSelectedCityID(-1);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constants.PARAM_NAME, tvCurrentCity.getText().toString().trim());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        Fragment fragment = new SelectCityFragment().newInstance(true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, fragment).commit();
    }
}
