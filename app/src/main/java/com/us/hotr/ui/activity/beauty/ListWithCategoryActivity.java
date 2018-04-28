package com.us.hotr.ui.activity.beauty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.customview.FlowLayout;
import com.us.hotr.storage.bean.Type;
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
    private long hospitalId = -1, doctorId = -1, spaId = -1, subjectId = -1, productId = -1;

    private FlowLayout flSubject;
    private int type;
    private Fragment listFragment;
    private long selectedSubjectID = 0;
    private List<Type> typeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(Constants.PARAM_TITLE);
        type = getIntent().getExtras().getInt(Constants.PARAM_TYPE, -1);
        hospitalId = getIntent().getExtras().getLong(Constants.PARAM_HOSPITAL_ID, -1);
        doctorId = getIntent().getExtras().getLong(Constants.PARAM_DOCTOR_ID, -1);
        spaId = getIntent().getExtras().getLong(Constants.PARAM_SPA_ID, -1);
        subjectId = getIntent().getExtras().getLong(Constants.PARAM_SUBJECT_ID, -1);
        productId = getIntent().getExtras().getLong(Constants.PARAM_PRODUCT_ID, -1);
        typeList = (List<Type>)getIntent().getExtras().getSerializable(Constants.PARAM_DATA);
        flSubject = (FlowLayout) findViewById(R.id.fl_subject);
        if(subjectId>=0)
            selectedSubjectID = subjectId;
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
                if(hospitalId>=0)
                listFragment = new CaseListFragment().newInstance(null, true, false, 3, hospitalId, subjectId);
                if(doctorId>=0)
                    listFragment = new CaseListFragment().newInstance(null, true, false, 4, doctorId, subjectId);
                if(productId>=0)
                    listFragment = new CaseListFragment().newInstance(null, true, false, 2, productId, subjectId);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;
            case Constants.TYPE_PRODUCT:
                listFragment = new ProductListFragment().newInstance(null, true, type, selectedSubjectID, -1, -1, hospitalId, doctorId);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
                break;
            case Constants.TYPE_MASSAGE:
                listFragment = new MassageListFragment().newInstance(null, false, selectedSubjectID, -1, spaId);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
        }
    }

    private void loadData(){
        if(type==Constants.TYPE_CASE && typeList!=null){
            setupPage(typeList);
        }else {
            SubscriberListener mListener = new SubscriberListener<List<Type>>() {
                @Override
                public void onNext(final List<Type> result) {
                    if (result != null && result.size() > 0) {
                        flSubject.setVisibility(View.VISIBLE);
                        int count = 0;
                        for (Type t : result)
                            count = count + t.getProduct_num();
                        result.add(0, new Type(-1, count, getString(R.string.all)));
                        setupPage(result);
                    }
                }
            };
            switch (type) {
                case Constants.TYPE_CASE:
                    break;
                case Constants.TYPE_PRODUCT:
                    if (doctorId != -1)
                        ServiceClient.getInstance().getProductTypeListByDoctor(new SilentSubscriber(mListener, this, null), doctorId);
                    if (hospitalId != -1)
                        ServiceClient.getInstance().getProductTypeListByHospital(new SilentSubscriber(mListener, this, null), hospitalId);
                    break;
                case Constants.TYPE_MASSAGE:
                    ServiceClient.getInstance().getMassageTypeListBySpa(new SilentSubscriber(mListener, this, null), spaId);
                    break;
            }
        }
    }
    private void setupPage(final List<Type> result){
        List<String> subjects = new ArrayList<>();
        int index = 0;
        for(int i=0;i<result.size();i++) {
            subjects.add(result.get(i).getTypeName() + " " + result.get(i).getProduct_num());
            if(selectedSubjectID == result.get(i).getTypeId())
                index = i;
        }
        flSubject.setVisibility(View.VISIBLE);
        flSubject.setFlowLayout(subjects, new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content, int position) {
                selectedSubjectID = result.get(position).getTypeId();
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
        flSubject.getTextViewList().get(index).setTextColor(getResources().getColor(R.color.white));
        flSubject.getTextViewList().get(index).setBackgroundResource(R.drawable.bg_search_pressed);
    }
}
