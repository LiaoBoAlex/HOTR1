package com.us.hotr.ui.fragment.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.ui.activity.party.CalendarActivity;
import com.us.hotr.ui.activity.party.FilterActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by macb00k on 2017/8/25.
 */

public class PartyFragment extends Fragment {
    private final int RESULT_CODE = 200;

    private LinearLayout llFilter, llCalendar;
    private ImageView ivFilterNumber;
    private QBadgeView mBadgeView;
    private PartyListFragment listFragment;

    private List<Integer> selectedStatus = new ArrayList<>();
    private boolean isLoaded = false;

    public static PartyFragment newInstance() {
        PartyFragment partyFragment = new PartyFragment();
        return partyFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_party, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llFilter = (LinearLayout) view.findViewById(R.id.ll_filter);
        llCalendar = (LinearLayout) view.findViewById(R.id.ll_calendar);
        ivFilterNumber = (ImageView) view.findViewById(R.id.iv_filter_number);

        mBadgeView = new QBadgeView(getContext());

        llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FilterActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(Constants.PARAM_DATA, (Serializable) selectedStatus);
                i.putExtras(b);
                startActivityForResult(i, RESULT_CODE);
            }
        });

        llCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CalendarActivity.class));
            }
        });

        if(selectedStatus.size()>0) {
            ivFilterNumber.setVisibility(View.INVISIBLE);
            mBadgeView.bindTarget(ivFilterNumber)
                    .setBadgeGravity(Gravity.CENTER)
                    .setBadgeBackgroundColor(getResources().getColor(R.color.cyan))
                    .setShowShadow(false)
                    .setBadgeNumber(selectedStatus.size());
        }else{
            ivFilterNumber.setVisibility(View.VISIBLE);
            mBadgeView.hide(false);
        }

        if(getUserVisibleHint() && !isLoaded){
            loadData();
        }

    }

    private void loadData(){
        isLoaded = true;
        if(listFragment == null) {
            listFragment = new PartyListFragment().newInstance(null, false);
            getChildFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && !isLoaded) {
            loadData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if(resultCode==getActivity().RESULT_OK){
            if(requestCode==RESULT_CODE){
                int count = data.getExtras().getInt(FilterActivity.PARAM_FILTER, 0);
                selectedStatus = (List<Integer>)data.getExtras().getSerializable(Constants.PARAM_DATA);
                if(listFragment != null)
                    listFragment.filterData(selectedStatus);
                if(count>0) {
                    ivFilterNumber.setVisibility(View.INVISIBLE);
                    mBadgeView.bindTarget(ivFilterNumber)
                            .setBadgeGravity(Gravity.CENTER)
                            .setBadgeBackgroundColor(getResources().getColor(R.color.cyan))
                            .setShowShadow(false)
                            .setBadgeNumber(count);
                }else{
                    ivFilterNumber.setVisibility(View.VISIBLE);
                    mBadgeView.hide(false);
                }
            }
        }
    }
}
