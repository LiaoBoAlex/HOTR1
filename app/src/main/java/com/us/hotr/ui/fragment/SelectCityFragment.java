package com.us.hotr.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.eventbus.Events;
import com.us.hotr.eventbus.GlobalBus;
import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.Provence;
import com.us.hotr.util.Tools;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.LoadingSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/5.
 */

public class SelectCityFragment extends BaseLoadingFragment {

    private RecyclerView rvProvence, rvCity;
    private ProvenceAdapter provenceAdapter;
    private CityAdapter cityAdapter;
    private int row_index = 0, cityType;
    private boolean isSelect = false;
    private long selectedCityId;

    public static SelectCityFragment newInstance(int cityType, boolean isSelect) {
        SelectCityFragment selectCityFragment = new SelectCityFragment();
        Bundle b = new Bundle();
        b.putBoolean(Constants.PARAM_IS_FAV, isSelect);
        b.putInt(Constants.PARAM_TYPE, cityType);
        selectCityFragment.setArguments(b);
        return selectCityFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_city, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isSelect = getArguments().getBoolean(Constants.PARAM_IS_FAV, false);
        cityType = getArguments().getInt(Constants.PARAM_TYPE);
        if(cityType == 0) {
            selectedCityId = HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedProductCityID();
            if (selectedCityId <= 0)
                selectedCityId = Tools.getCityCodeFromBaidu(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentCityID()).getProductCityCode();
        }else{
            selectedCityId = HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getSelectedMassageCityID();
            if (selectedCityId <= 0)
                selectedCityId = Tools.getCityCodeFromBaidu(HOTRSharePreference.getInstance(getActivity().getApplicationContext()).getCurrentCityID()).getMassageCityCode();
        }

        rvProvence = (RecyclerView) view.findViewById(R.id.rv_title);
        rvCity = (RecyclerView) view.findViewById(R.id.rv_sub_title);

        rvProvence.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProvence.setItemAnimator(new DefaultItemAnimator());
        rvCity.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCity.setItemAnimator(new DefaultItemAnimator());

        loadData(Constants.LOAD_PAGE);
    }

    @Override
    protected void loadData(int type) {

        SubscriberListener mListener = new SubscriberListener<List<Provence>>() {
            @Override
            public void onNext(List<Provence> result) {
                if(result != null && result.size()>0) {
                    boolean found = false;
                    if(isAdded()) {
                        List<Provence.City> hotCityList = new ArrayList<>();
                        for (int i = 0; i < result.size(); i++) {
                            if (result.get(i).getCityList() != null) {
                                for (Provence.City c : result.get(i).getCityList()) {
                                    if (c.getIsHotcity() == 1)
                                        hotCityList.add(c);
                                    if (selectedCityId == c.getCode()) {
                                        row_index = i;
                                        found = true;
                                    }
                                }
                            }
                        }
                        result.add(0, new Provence(getString(R.string.popular_city), hotCityList));
                        result.add(0, new Provence(getString(R.string.filter_city)));
                        if (found)
                            row_index = row_index + 2;
                        provenceAdapter = new ProvenceAdapter(result);
                        rvProvence.setAdapter(provenceAdapter);
                    }
                }
            }
        };
        ServiceClient.getInstance().getCityList(new LoadingSubscriber(mListener, this), cityType);
    }

    @Subscribe
    public void getMessage(Events.CitySelected citySelectedEvent) {
    }

    @Subscribe
    public void getMessage(Events.SubjectSelected subjectSelected) {
    }

    @Subscribe
    public void getMessage(Events.TypeSelected typeSelected) {
    }

    public class ProvenceAdapter extends RecyclerView.Adapter<ProvenceAdapter.MyViewHolder> {

        private List<Provence> provenceList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvProvence;
            View vIndicator;

            public MyViewHolder(View view) {
                super(view);
                tvProvence = (TextView) view.findViewById(R.id.tv_provence);
                vIndicator = view.findViewById(R.id.v_indicator);
            }
        }


        public ProvenceAdapter(List<Provence> provenceList) {
            this.provenceList = provenceList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_provence, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Provence provence = provenceList.get(position);
            holder.tvProvence.setText(provence.getAreaName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getString(R.string.filter_city).equals(provence.getAreaName())){
                        if(isSelect) {
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeSelectedCityName(provence.getAreaName());
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeSelectedProductCityID(Constants.ALL_CITY_ID);
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeSelectedMassageCityID(Constants.ALL_CITY_ID);
                        }
                        if(cityAdapter!=null)
                            cityAdapter.clearCities();
                        Events.CitySelected citySelectedEvent = new Events.CitySelected(provence.getAreaName(), -1);
                        GlobalBus.getBus().post(citySelectedEvent);
                    }
                    row_index=position;
                    notifyDataSetChanged();
                    if(provence.getCityList() != null && provence.getCityList().size() > 0) {
                        if (cityAdapter == null) {
                            cityAdapter = new CityAdapter(provence.getCityList());
                            rvCity.setAdapter(cityAdapter);
                        } else
                            cityAdapter.setCites(provence.getCityList());
//                    else{
//                        Events.CitySelected citySelectedEvent = new Events.CitySelected(provence.getAreaName(), "1");
//                        GlobalBus.getBus().post(citySelectedEvent);
//
//                    }
                    }
                }
            });
            if(row_index==position){
                holder.tvProvence.setBackgroundResource(R.color.white);
                holder.tvProvence.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));
                holder.vIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.tvProvence.setBackgroundResource(R.color.bg_grey);
                holder.tvProvence.setTextColor(ContextCompat.getColor(getContext(), R.color.text_grey2));
                holder.vIndicator.setVisibility(View.INVISIBLE);
            }
            if(row_index == position && provence.getCityList() != null && provence.getCityList().size() > 0) {
                if (cityAdapter == null) {
                    cityAdapter = new CityAdapter(provence.getCityList());
                    rvCity.setAdapter(cityAdapter);
                } else
                    cityAdapter.setCites(provence.getCityList());
            }
        }

        @Override
        public int getItemCount() {
            return provenceList.size();
        }
    }

    public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

        private List<Provence.City> cityList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvCity;

            public MyViewHolder(View view) {
                super(view);
                tvCity = (TextView) view.findViewById(R.id.tv_subjects);
            }
        }

        public CityAdapter(List<Provence.City> cityList) {
            this.cityList = cityList;
        }

        public void setCites(List<Provence.City> cites){
            cityList = cites;
            notifyDataSetChanged();
        }

        public void clearCities(){
            cityList = new ArrayList<>();
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_city, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Provence.City city = cityList.get(position);
            holder.tvCity.setText(city.getAreaName());
            if(city.getCode() ==selectedCityId)
                holder.tvCity.setTextColor(getResources().getColor(R.color.red));
            else
                holder.tvCity.setTextColor(getResources().getColor(R.color.text_grey2));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isSelect) {
                        HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeSelectedCityName(city.getAreaName());
                        if(cityType == 0) {
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeSelectedProductCityID(city.getCode());
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeSelectedMassageCityID(
                                    Tools.getCityCodeFromProduct(city.getCode()).getMassageCityCode());
                        }
                        else {
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeSelectedMassageCityID(city.getCode());
                            HOTRSharePreference.getInstance(getActivity().getApplicationContext()).storeSelectedProductCityID(
                                    Tools.getCityCodeFromMassage(city.getCode()).getProductCityCode());
                        }
                    }
                    selectedCityId = city.getCode();
                    notifyDataSetChanged();
                    Events.CitySelected citySelectedEvent = new Events.CitySelected(city.getAreaName(), city.getCode());
                    GlobalBus.getBus().post(citySelectedEvent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(cityList == null)
                return 0;
            return cityList.size();
        }
    }
}
