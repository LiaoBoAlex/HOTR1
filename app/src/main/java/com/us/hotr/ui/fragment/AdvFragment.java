package com.us.hotr.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.us.hotr.Constants;
import com.us.hotr.R;
import com.us.hotr.storage.bean.Adv;
import com.us.hotr.ui.activity.MainActivity;

/**
 * Created by liaobo on 2017/12/12.
 */

public class AdvFragment extends Fragment {
    private Adv mAdv;
    private ImageView ivImage, ivClose;
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

        Glide.with(getContext()).load(mAdv.getImage()).into(ivImage);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
