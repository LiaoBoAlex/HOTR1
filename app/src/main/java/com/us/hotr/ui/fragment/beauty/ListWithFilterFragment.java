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
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.ui.fragment.SelectCityFragment;
import com.us.hotr.ui.fragment.SelectTypeFragment;
import com.us.hotr.ui.fragment.massage.MassageListFragment;
import com.us.hotr.ui.fragment.massage.MasseurListFragment;
import com.us.hotr.ui.fragment.massage.SelectMassageSubjectFragment;
import com.us.hotr.ui.fragment.massage.SpaListFragment;
import com.us.hotr.util.Tools;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Mloong on 2017/9/20.
 */

public class ListWithFilterFragment extends Fragment {

    private TextView tvFilterCity, tvFilterSubject, tvFilterType;
    private FrameLayout mContainerList, mContainerFilter;
    private View vDivider1;

    private boolean isCityListOpen = false, isSubjectListOpen = false, isTypeListOpen = false;
    private int type, subjectId = -1;
    private Fragment selectCityFragment, selectSubjectFragment, listFragment, selectTypeFragment;
    private HOTRSharePreference hotrSharePreference;
    private String keyword;


    public static ListWithFilterFragment newInstance(String keyword, int type, int subjectId, boolean enableRefresh) {
        ListWithFilterFragment listWithFilterFragment = new ListWithFilterFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.PARAM_TYPE, type);
        b.putBoolean(Constants.PARAM_ENABLE_REFRESH, enableRefresh);
        b.putString(Constants.PARAM_KEYWORD, keyword);
        b.putInt(Constants.PARAM_ID, subjectId);
        listWithFilterFragment.setArguments(b);
        return listWithFilterFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_with_filter_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        type = getArguments().getInt(Constants.PARAM_TYPE);
        keyword = getArguments().getString(Constants.PARAM_KEYWORD);
        subjectId = getArguments().getInt(Constants.PARAM_ID, -1);
        hotrSharePreference = HOTRSharePreference.getInstance(getActivity().getApplicationContext());

        tvFilterType = (TextView) view.findViewById(R.id.tv_filter_type);
        tvFilterCity = (TextView) view.findViewById(R.id.tv_filter_city);
        tvFilterSubject = (TextView) view.findViewById(R.id.tv_filter_subject);
        mContainerList = (FrameLayout) view.findViewById(R.id.container1);
        mContainerFilter = (FrameLayout) view.findViewById(R.id.container2);
        vDivider1 = view.findViewById(R.id.v_divider1);
        long cityID = -1;

        switch(type){
            case Constants.TYPE_DOCTOR:
            case Constants.TYPE_HOSPITAL:
                if(hotrSharePreference.getSelectedProductCityID() > 0) {
                    tvFilterCity.setText(hotrSharePreference.getSelectedCityName());
                    cityID = hotrSharePreference.getSelectedProductCityID();
                }
                else{
                    if(Tools.getCityCodeFromBaidu(hotrSharePreference.getCurrentCityID()).getProductCityCode()>=0) {
                        tvFilterCity.setText(hotrSharePreference.getCurrentCityName());
                        cityID = Tools.getCityCodeFromBaidu(hotrSharePreference.getCurrentCityID()).getProductCityCode();
                    }else{
                        tvFilterCity.setText(getString(R.string.filter_city));
                        cityID = Constants.ALL_CITY_ID;
                    }
                }
                break;
            case Constants.TYPE_MASSAGE:
            case Constants.TYPE_SPA:
            case Constants.TYPE_MASSEUR:
                if(hotrSharePreference.getSelectedMassageCityID() > 0) {
                    tvFilterCity.setText(hotrSharePreference.getSelectedCityName());
                    cityID = hotrSharePreference.getSelectedMassageCityID();
                }
                else{
                    if(Tools.getCityCodeFromBaidu(hotrSharePreference.getCurrentCityID()).getMassageCityCode()>=0) {
                        tvFilterCity.setText(hotrSharePreference.getCurrentCityName());
                        cityID = Tools.getCityCodeFromBaidu(hotrSharePreference.getCurrentCityID()).getMassageCityCode();
                    }else{
                        tvFilterCity.setText(getString(R.string.filter_city));
                        cityID = Constants.ALL_CITY_ID;
                    }
                }
                break;
        }

        if(cityID == Constants.ALL_CITY_ID)
            cityID = -1;

        tvFilterCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSubjectListOpen){
                    hideAnimation();
                    isSubjectListOpen = false;
                }else if (isTypeListOpen) {
                    hideAnimation();
                    isTypeListOpen = false;
                }else{
                    if (!isCityListOpen) {
                        showAnimation(selectCityFragment);
                        isCityListOpen = true;
                    } else {
                        hideAnimation();
                        isCityListOpen = false;
                    }
                }
            }
        });

        tvFilterSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCityListOpen){
                    hideAnimation();
                    isCityListOpen = false;
                }else if (isTypeListOpen){
                    hideAnimation();
                    isTypeListOpen = false;

                }else{
                    if (!isSubjectListOpen) {
                        showAnimation(selectSubjectFragment);
                        isSubjectListOpen = true;
                    } else {
                        hideAnimation();
                        isSubjectListOpen = false;
                    }
                }
            }
        });

        tvFilterType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCityListOpen){
                    hideAnimation();
                    isCityListOpen = false;
                }else if (isSubjectListOpen){
                    hideAnimation();
                    isSubjectListOpen = false;

                }else{
                    if (!isTypeListOpen) {
                        showAnimation(selectTypeFragment);
                        isTypeListOpen = true;
                    } else {
                        hideAnimation();
                        isTypeListOpen = false;
                    }
                }
            }
        });
        int cityType = 0;
        switch (type){
            case Constants.TYPE_DOCTOR:
                listFragment = new DoctorListFragment().newInstance(keyword, type, cityID, -1);
                selectSubjectFragment = new SelectSubjectFragment().newInstance(true, -1);
                break;
            case Constants.TYPE_HOSPITAL:
                listFragment = new HospitalListFragment().newInstance(keyword, false, cityID);
                selectSubjectFragment = new SelectSubjectFragment().newInstance(true, -1);
                break;
            case Constants.TYPE_SPA:
                cityType = 1;
                listFragment = new SpaListFragment().newInstance(keyword, cityID);
                selectSubjectFragment = new SelectMassageSubjectFragment().newInstance();
                break;
            case Constants.TYPE_MASSEUR:
                cityType = 1;
                listFragment = new MasseurListFragment().newInstance(keyword, cityID, -1);
                selectSubjectFragment = new SelectMassageSubjectFragment().newInstance();
                break;
            case Constants.TYPE_MASSAGE:
                cityType = 1;
                listFragment = new MassageListFragment().newInstance(keyword, false, subjectId, cityID, -1);
                selectSubjectFragment = new SelectMassageSubjectFragment().newInstance();
                break;
        }
        selectCityFragment = new SelectCityFragment().newInstance(cityType, false);
        selectTypeFragment = new SelectTypeFragment().newInstance(type, false);

        getChildFragmentManager().beginTransaction().add(R.id.container1, listFragment).commit();
        getChildFragmentManager().beginTransaction().add(R.id.container2, selectCityFragment).hide(selectCityFragment).commit();
        getChildFragmentManager().beginTransaction().add(R.id.container2, selectTypeFragment).hide(selectTypeFragment).commit();
        if(subjectId>=0 && type ==Constants.TYPE_MASSAGE){
            tvFilterSubject.setVisibility(View.GONE);
            vDivider1.setVisibility(View.GONE);
        }else
            getChildFragmentManager().beginTransaction().add(R.id.container2, selectSubjectFragment).hide(selectSubjectFragment).commit();
        mContainerList.bringToFront();
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

    @Subscribe
    public void getMessage(Events.CitySelected citySelectedEvent) {
        tvFilterCity.setText(citySelectedEvent.getSelectedCity());
        hideAnimation();
        isCityListOpen = false;

    }

    @Subscribe
    public void getMessage(Events.SubjectSelected subjectSelected) {
        tvFilterSubject.setText(subjectSelected.getSelectedSubject());
        hideAnimation();
        isSubjectListOpen = false;

    }

    @Subscribe
    public void getMessage(Events.TypeSelected typeSelected) {
        tvFilterType.setText(typeSelected.getType());
        hideAnimation();
        isTypeListOpen = false;

    }

    private void showAnimation(Fragment fragment){
        Animation animIn = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_menu_in);
        mContainerFilter.setVisibility(View.VISIBLE);
        mContainerFilter.bringToFront();
        if(fragment.isAdded())
            getChildFragmentManager().beginTransaction().show(fragment).commit();
        else
            getChildFragmentManager().beginTransaction().add(R.id.container2, fragment).commit();
        mContainerFilter.startAnimation(animIn);
    }

    private void hideAnimation(){

        Animation animOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_menu_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mContainerList.bringToFront();
                if(selectCityFragment.isAdded() && !selectCityFragment.isHidden())
                    getChildFragmentManager().beginTransaction().hide(selectCityFragment).commit();
                if(selectSubjectFragment.isAdded() && !selectSubjectFragment.isHidden())
                    getChildFragmentManager().beginTransaction().hide(selectSubjectFragment).commit();
                if(selectTypeFragment.isAdded() && !selectTypeFragment.isHidden())
                    getChildFragmentManager().beginTransaction().hide(selectTypeFragment).commit();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContainerFilter.startAnimation(animOut);

    }
}