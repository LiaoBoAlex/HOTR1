package com.us.hotr.ui.fragment.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.CloseFragmentListener;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Hospital;
import com.us.hotr.ui.activity.beauty.SubjectActivity;
import com.us.hotr.ui.fragment.SelectCityFragment;
import com.us.hotr.ui.fragment.SelectTypeFragment;
import com.us.hotr.ui.fragment.massage.MassageListFragment;
import com.us.hotr.util.Tools;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.internal.operators.flowable.FlowableTakeWhile;

/**
 * Created by Mloong on 2017/9/6.
 */

public class ProductListWithFilterFragment extends Fragment implements CloseFragmentListener{

    private TextView tvFilterCity, tvFilterType;
    private FrameLayout mContainerCity, mContainerList;
    private List<Hospital> hospitalList;
    public boolean isCityListOpen = false, isTypeListOpen = false;
    private SubjectActivity parentActivity;
    private boolean haveParent = false;
    private Fragment productListFragment, selectCityFragment, selectTypeFragment;
    private Integer type;
    private Long id;
    private HOTRSharePreference hotrSharePreference;
    private String keyword;

    public static ProductListWithFilterFragment newInstance(String keyword, int type, boolean enableRefresh, Long id) {
        ProductListWithFilterFragment productListFragment = new ProductListWithFilterFragment();
        Bundle b = new Bundle();
        b.putBoolean(Constants.PARAM_ENABLE_REFRESH, enableRefresh);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        b.putInt(Constants.PARAM_TYPE, type);
        if(id!=null)
            b.putLong(Constants.PARAM_NAME, id);
        productListFragment.setArguments(b);
        return productListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type = getArguments().getInt(Constants.PARAM_TYPE);
        id = getArguments().getLong(Constants.PARAM_NAME);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);
        hotrSharePreference = HOTRSharePreference.getInstance(getActivity().getApplicationContext());

        if(getActivity() instanceof  SubjectActivity) {
            parentActivity = (SubjectActivity) getActivity();
            haveParent = true;
        }else
            haveParent = false;

        tvFilterType = (TextView) view.findViewById(R.id.tv_filter_type);
        tvFilterCity = (TextView) view.findViewById(R.id.tv_filter_city);
        mContainerCity = (FrameLayout) view.findViewById(R.id.container1);
        mContainerList = (FrameLayout) view.findViewById(R.id.container2);
        long cityId = -1;



        switch(type){
            case Constants.TYPE_PRODUCT:
                if(hotrSharePreference.getSelectedProductCityID() > 0) {
                    tvFilterCity.setText(hotrSharePreference.getSelectedCityName());
                    cityId = hotrSharePreference.getSelectedProductCityID();
                }
                else{
                    if(Tools.getCityCodeFromBaidu(hotrSharePreference.getCurrentCityID()).getProductCityCode()>=0){
                        tvFilterCity.setText(hotrSharePreference.getCurrentCityName());
                        cityId = Tools.getCityCodeFromBaidu(hotrSharePreference.getCurrentCityID()).getProductCityCode();
                    }else{
                        tvFilterCity.setText(getString(R.string.filter_city));
                        cityId = Constants.ALL_CITY_ID;
                    }
                }
                break;
            case Constants.TYPE_MASSAGE:
                if(hotrSharePreference.getSelectedMassageCityID() > 0) {
                    tvFilterCity.setText(hotrSharePreference.getSelectedCityName());
                    cityId = hotrSharePreference.getSelectedMassageCityID();
                }
                else{
                    if(Tools.getCityCodeFromBaidu(hotrSharePreference.getCurrentCityID()).getMassageCityCode()>=0){
                        tvFilterCity.setText(hotrSharePreference.getCurrentCityName());
                        cityId = Tools.getCityCodeFromBaidu(hotrSharePreference.getCurrentCityID()).getMassageCityCode();
                    }else{
                        tvFilterCity.setText(getString(R.string.filter_city));
                        cityId = Constants.ALL_CITY_ID;
                    }
                }
                break;
        }

        if(keyword!=null && !keyword.isEmpty()){
            tvFilterCity.setText(getString(R.string.filter_city));
            cityId = Constants.ALL_CITY_ID;
        }

        if(cityId == Constants.ALL_CITY_ID)
            cityId = -1;

        tvFilterCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveParent && parentActivity.isSubjectListOpen){
                    parentActivity.hideAnimation();
                }else if (isTypeListOpen){
                    hideAnimation();
                }else{
                    if (!isCityListOpen) {
                        showAnimation(selectCityFragment);
                        isCityListOpen = true;
                    } else {
                        hideAnimation();
                    }
                }
            }
        });

        tvFilterType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveParent && parentActivity.isSubjectListOpen){
                    parentActivity.hideAnimation();
                }else if (isCityListOpen){
                    hideAnimation();
                }else{
                    if (!isTypeListOpen) {
                        showAnimation(selectTypeFragment);
                        isTypeListOpen = true;
                    } else {
                        hideAnimation();
                    }
                }
            }
        });

        hospitalList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Hospital h = new Hospital();
            hospitalList.add(h);
        }
        int cityType = 0;
        switch(type){
            case Constants.TYPE_PRODUCT:
                cityType = 0;
                productListFragment = new ProductListFragment().newInstance(keyword, getArguments().getBoolean(Constants.PARAM_ENABLE_REFRESH),type, -1, -1, id, cityId, -1, -1);
                break;
            case Constants.TYPE_MASSAGE:
                cityType = 1;
                productListFragment = new MassageListFragment().newInstance(keyword,false, id, cityId, -1);
                break;
        }

        selectCityFragment = new SelectCityFragment().newInstance(cityType, false);
        selectTypeFragment = new SelectTypeFragment().newInstance(type, true);
        getChildFragmentManager().beginTransaction().add(R.id.container2, productListFragment).commit();
        getChildFragmentManager().beginTransaction().add(R.id.container1, selectCityFragment).hide(selectCityFragment).commit();
        getChildFragmentManager().beginTransaction().add(R.id.container1, selectTypeFragment).hide(selectTypeFragment).commit();
        mContainerList.bringToFront();
        mContainerCity.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Subscribe
    public void getMessage(Events.CitySelected citySelectedEvent) {
        tvFilterCity.setText(citySelectedEvent.getSelectedCity());
        hideAnimation();
    }

    @Subscribe
    public void getMessage(Events.TypeSelected typeSelected) {
        tvFilterType.setText(typeSelected.getType());
        hideAnimation();

    }


    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    public void showAnimation(Fragment fragment){
        if(haveParent)
            parentActivity.hideHeader();
        Animation animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_menu_in);
        mContainerCity.setVisibility(View.VISIBLE);
        mContainerCity.bringToFront();
        mContainerCity.startAnimation(animIn);
        if(haveParent)
            parentActivity.enableAppBarLayoutBehavior(false);
        if(fragment.isAdded())
            getChildFragmentManager().beginTransaction().show(fragment).commit();
        else
            getChildFragmentManager().beginTransaction().add(R.id.container2, fragment).commit();
    }

    public void hideAnimation(){

        Animation animOut = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_menu_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mContainerList.bringToFront();
                mContainerCity.setVisibility(View.GONE);
                if(selectCityFragment.isAdded() && !selectCityFragment.isHidden()) {
                    getChildFragmentManager().beginTransaction().hide(selectCityFragment).commit();
                    isCityListOpen = false;
                }
                if(selectTypeFragment.isAdded() && !selectTypeFragment.isHidden()) {
                    getChildFragmentManager().beginTransaction().hide(selectTypeFragment).commit();
                    isTypeListOpen = false;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContainerCity.startAnimation(animOut);
        if(haveParent)
            parentActivity.enableAppBarLayoutBehavior(true);

    }

    @Override
    public void onFragmentClose() {
        if(haveParent && parentActivity.isSubjectListOpen){
            parentActivity.hideAnimation();
        }else if (isTypeListOpen){
            hideAnimation();
        }
    }
}
