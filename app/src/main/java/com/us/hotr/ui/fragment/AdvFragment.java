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
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Adv;
import com.us.hotr.ui.activity.MainActivity;
import com.us.hotr.ui.activity.WebViewActivity;

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
                Intent i = new Intent(getActivity(), WebViewActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.PARAM_TYPE, WebViewActivity.TYPE_URL);
                b.putString(Constants.PARAM_DATA, mAdv.getForwart_url());
                i.putExtras(b);
                startActivity(i);
                getActivity().finish();

            }
        });

    }
}
