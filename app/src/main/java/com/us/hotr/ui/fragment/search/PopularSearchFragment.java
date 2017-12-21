package com.us.hotr.ui.fragment.search;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.Data;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.storage.greendao.DataBaseHelper;
import com.us.hotr.ui.activity.search.SearchHintActivity;

import java.util.List;

/**
 * Created by Mloong on 2017/8/29.
 */

public class PopularSearchFragment extends Fragment{

    private FlowLayout flKeyword;
    private TextView tvClear;
    private LinearLayout llSearchHistory;
    private ConstraintLayout clSearchHistory;

    public static PopularSearchFragment newInstance() {
        PopularSearchFragment popularSearchFragment = new PopularSearchFragment();
        return popularSearchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llSearchHistory = (LinearLayout) view.findViewById(R.id.ll_search_history);
        clSearchHistory = (ConstraintLayout) view.findViewById(R.id.cl_search_history);
        tvClear = (TextView) view.findViewById(R.id.tv_clear);
        flKeyword = (FlowLayout) view.findViewById(R.id.fl_keyword);
        flKeyword.setHorizontalSpacing(6);
        flKeyword.setTextPaddingH(14);

        flKeyword.setFlowLayout(Data.getSearchPopular(), new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content, int position) {
                ((SearchHintActivity)getActivity()).setSearchText(content);
            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                DataBaseHelper.clearSearchHistory();
                                updateSearchHistory();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.delete_search_history)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.no), dialogClickListener).show();
            }
        });

        updateSearchHistory();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            updateSearchHistory();
        }
    }

    private void updateSearchHistory() {
        if (clSearchHistory != null){
            List<String> searchHistory = DataBaseHelper.getAllSearchHistory();
            if (searchHistory != null && searchHistory.size() > 0) {
                clSearchHistory.setVisibility(View.VISIBLE);
                llSearchHistory.removeAllViews();
                for (final String h : searchHistory) {
                    final LinearLayout llHistory = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_search_history, null);
                    TextView tvHistory = (TextView) llHistory.findViewById(R.id.tv_history);
                    tvHistory.setText(h);
                    llHistory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((SearchHintActivity) getActivity()).setSearchText(h);
                        }
                    });
                    llSearchHistory.addView(llHistory);
                }
            } else
                clSearchHistory.setVisibility(View.GONE);
        }
    }
}
