package com.us.hotr.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tencent.stat.StatService;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Adv;
import com.us.hotr.storage.bean.Module;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.WebViewActivity;
import com.us.hotr.ui.activity.beauty.CaseActivity;
import com.us.hotr.ui.activity.beauty.DoctorActivity;
import com.us.hotr.ui.activity.beauty.HospitalActivity;
import com.us.hotr.ui.activity.beauty.ListActivity;
import com.us.hotr.ui.activity.beauty.ListWithSearchActivity;
import com.us.hotr.ui.activity.beauty.ProductActivity;
import com.us.hotr.ui.activity.beauty.SelectSubjectActivity;
import com.us.hotr.ui.activity.beauty.SubjectActivity;
import com.us.hotr.ui.activity.found.GroupDetailActivity;
import com.us.hotr.ui.activity.found.NearbyActivity;
import com.us.hotr.ui.activity.info.InviteFriendActivity;
import com.us.hotr.ui.activity.info.LoginActivity;
import com.us.hotr.ui.activity.massage.MassageActivity;
import com.us.hotr.ui.activity.massage.MasseurActivity;
import com.us.hotr.ui.activity.massage.SpaActivity;
import com.us.hotr.ui.activity.party.PartyActivity;
import com.us.hotr.util.Tools;

import java.util.Properties;

/**
 * Created by liaobo on 2017/12/12.
 */

public class AdvFragment extends Fragment {
    private Adv mAdv;
    private ImageView ivImage, ivClose;
    private WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_adv, container, false);
    }

    public static AdvFragment newInstance(Adv adv) {
        AdvFragment advFragment = new AdvFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.PARAM_DATA, adv);
        advFragment.setArguments(b);
        return advFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdv = (Adv)getArguments().getSerializable(Constants.PARAM_DATA);

        ivImage = (ImageView) view.findViewById(R.id.iv_image);
        ivClose = (ImageView) view.findViewById(R.id.iv_close);
        webView = (WebView) view.findViewById(R.id.wv_content);

        Glide.with(this).load(mAdv.getImage()).into(ivImage);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickEvent();
            }
        });

    }

    private void handleClickEvent(){
        Properties prop = new Properties();
        prop.setProperty("page", mAdv.getPage()+"");
        StatService.trackCustomKVEvent(getActivity(), Constants.MTA_ID_SPLASH_SCREEN, prop);

        switch (Integer.valueOf(mAdv.getForwart_type())){
            case 1:
                Intent i = new Intent(getContext(), SelectSubjectActivity.class);
                i.putExtra(Constants.PARAM_TITLE, getContext().getString(R.string.product1));
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 2:
                i = new Intent(getContext(), ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, getContext().getString(R.string.doctor_list_title));
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_DOCTOR);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 3:
                i = new Intent(getContext(), ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, getContext().getString(R.string.hospital_list_title));
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_HOSPITAL);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 4:
                i = new Intent(getContext(), SubjectActivity.class);
                Bundle b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 5:
                i = new Intent(getContext(), ProductActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 6:
                i = new Intent(getContext(), DoctorActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 7:
                i = new Intent(getContext(), HospitalActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 8:
                i = new Intent(getContext(), ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, getContext().getString(R.string.massage_list_title));
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 9:
                i = new Intent(getContext(), ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, getContext().getString(R.string.masseur_list_title));
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MASSEUR);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 10:
                i = new Intent(getContext(), ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, getContext().getString(R.string.spa_list_title));
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_SPA);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 11:
                i = new Intent(getContext(), MasseurActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 12:
                i = new Intent(getContext(), SpaActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 13:
                i = new Intent(getContext(), MassageActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 14:
                i = new Intent(getContext(), ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, getContext().getString(R.string.massage_list_title));
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_MASSAGE);
                i.putExtra(Constants.PARAM_ID, Integer.parseInt(mAdv.getForwart_url().trim()));
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 15:
                i = new Intent(getContext(), ListWithSearchActivity.class);
                i.putExtra(Constants.PARAM_TITLE, getContext().getString(R.string.case_list_title));
                i.putExtra(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 16:
                i = new Intent(getContext(), CaseActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_CASE);
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 17:
                i = new Intent(getContext(), GroupDetailActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 18:
                i = new Intent(getContext(), CaseActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_POST);
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 19:
                if(Constants.INVITE_FRIENDS_SHARE_URL.equals(mAdv.getForwart_url().trim())){
                    if(Tools.isUserLogin(getContext())){
                        getContext().startActivity(new Intent(getContext(), InviteFriendActivity.class));
                        getActivity().finish();
                    }else{
                        LoginActivity.setLoginListener(new LoginActivity.LoginListener() {
                            @Override
                            public void onLoginSuccess() {
                                getContext().startActivity(new Intent(getContext(), InviteFriendActivity.class));
                                getActivity().finish();
                            }
                        });
                        getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                }else {
                    i = new Intent(getContext(), WebViewActivity.class);
                    b = new Bundle();
                    b.putString(Constants.PARAM_DATA, mAdv.getForwart_url().trim());
                    b.putInt(Constants.PARAM_TYPE, WebViewActivity.TYPE_URL);
                    i.putExtras(b);
                    getContext().startActivity(i);
                    getActivity().finish();
                }
                break;
            case 20:
                i = new Intent(getContext(), NearbyActivity.class);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 21:
                i = new Intent(getContext(), ListActivity.class);
                b = new Bundle();
                b.putString(Constants.PARAM_TITLE, getContext().getString(R.string.discovery_title));
                b.putInt(Constants.PARAM_TYPE, Constants.TYPE_OFFICIAL_POST);
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
            case 23:
                i = new Intent(getContext(), PartyActivity.class);
                b = new Bundle();
                b.putLong(Constants.PARAM_ID, Long.parseLong(mAdv.getForwart_url().trim()));
                i.putExtras(b);
                getContext().startActivity(i);
                getActivity().finish();
                break;
        }
    }
}
