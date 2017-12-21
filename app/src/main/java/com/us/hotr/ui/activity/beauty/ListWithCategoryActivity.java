package com.us.hotr.ui.activity.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.storage.bean.TypeWithCount;
import com.us.hotr.ui.activity.BaseActivity;
import com.us.hotr.ui.fragment.beauty.CaseListFragment;
import com.us.hotr.ui.fragment.beauty.ProductListFragment;
import com.us.hotr.ui.fragment.massage.MassageListFragment;
import com.us.hotr.webservice.ServiceClient;
import com.us.hotr.webservice.rxjava.SilentSubscriber;
import com.us.hotr.webservice.rxjava.SubscriberListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/9/8.
 */

public class ListWithCategoryActivity extends BaseActivity {
    private int hospitalId = -1, doctorId = -1, spaId = -1;

    private FlowLayout flSubject;
    private int type;
    private Fragment listFragment;
    private int selectedSubjectID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(Constants.PARAM_TITLE);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE, -1);
        hospitalId = getIntent().getExtras().getInt(Constants.PARAM_HOSPITAL_ID, -1);
        doctorId = getIntent().getExtras().getInt(Constants.PARAM_DOCTOR_ID, -1);
        spaId = getIntent().getExtras().getInt(Constants.PARAM_SPA_ID, -1);
        flSubject = (FlowLayout) findViewById(R.id.fl_subject);
        setMyTitle(title);
        loadData();
        loadPage();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_case_list;
    }

    private void loadPage(){
        switch (type){
            case Constants.TYPE_CASE:
                listFragment = new CaseListFragment().newInstance(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;
            case Constants.TYPE_PRODUCT:
                listFragment = new ProductListFragment().newInstance(true, selectedSubjectID, -1, hospitalId, doctorId);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;
            case Constants.TYPE_MASSAGE:
                listFragment = new MassageListFragment().newInstance(selectedSubjectID, -1, spaId);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
        }
    }

    private void loadData(){
        switch (type){
            case Constants.TYPE_CASE:
                break;
            case Constants.TYPE_PRODUCT:
                break;
            case Constants.TYPE_MASSAGE:
                SubscriberListener mListener = new SubscriberListener<List<TypeWithCount>>() {
                    @Override
                    public void onNext(final List<TypeWithCount> result) {
                        if(result != null && result.size()>0){
                            flSubject.setVisibility(View.VISIBLE);
                            int count = 0;
                            for(TypeWithCount t:result)
                                count = count + t.getProduct_num();
                            result.add(0, new TypeWithCount(0, count, getString(R.string.all)));

                            List<String> subjects = new ArrayList<>();
                            for(TypeWithCount t:result)
                                subjects.add(t.getType_name() + " " + t.getProduct_num());

                            flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
                                @Override
                                public void onItemClick(String content, int position) {
                                    selectedSubjectID = result.get(position).getType_id();
                                    loadPage();
                                    for(int i=0;i<flSubject.getTextViewList().size();i++){
                                        if(i == position){
                                            flSubject.getTextViewList().get(i).setTextColor(getResources().getColor(R.color.white));
                                            flSubject.getTextViewList().get(i).setBackgroundResource(R.drawable.bg_search_pressed);
                                        }else{
                                            flSubject.getTextViewList().get(i).setTextColor(getResources().getColor(R.color.text_black));
                                            flSubject.getTextViewList().get(i).setBackgroundResource(R.drawable.bg_search);
                                        }
                                    }
                                }
                            });
                            flSubject.getTextViewList().get(0).setTextColor(getResources().getColor(R.color.white));
                            flSubject.getTextViewList().get(0).setBackgroundResource(R.drawable.bg_search_pressed);
                        }
                    }
                };
                ServiceClient.getInstance().getMassageTypeListBySpa(new SilentSubscriber(mListener, this, null), spaId);
                break;
        }

    }
}
