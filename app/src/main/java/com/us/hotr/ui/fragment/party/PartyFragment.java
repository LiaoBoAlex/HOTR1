package com.us.hotr.ui.fragment.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.us.hotr.R;
import com.us.hotr.ui.activity.party.CalendarActivity;
import com.us.hotr.ui.activity.party.FilterActivity;
import com.us.hotr.ui.activity.party.PartyActivity;
import com.us.hotr.util.Tools;

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

        listFragment = new PartyListFragment().newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();

        llFilter = (LinearLayout) view.findViewById(R.id.ll_filter);
        llCalendar = (LinearLayout) view.findViewById(R.id.ll_calendar);
        ivFilterNumber = (ImageView) view.findViewById(R.id.iv_filter_number);

        mBadgeView = new QBadgeView(getContext());

        llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), FilterActivity.class), RESULT_CODE);
            }
        });

        llCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CalendarActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if(resultCode==getActivity().RESULT_OK){
            if(requestCode==RESULT_CODE){
                int count = data.getIntExtra(FilterActivity.PARAM_FILTER, 0);
                Tools.Toast(getActivity(), count+"");
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


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivAvatar;
            TextView tvNumber, tvTitle, tvDate, tvStatus;
            public MyViewHolder(View view){
                super(view);
                ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvDate = (TextView) view.findViewById(R.id.tv_date);
                tvStatus = (TextView) view.findViewById(R.id.tv_status);
                tvNumber = (TextView) view.findViewById(R.id.tv_number);
            }
        }

        public MyAdapter() {

        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_party, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), PartyActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}
