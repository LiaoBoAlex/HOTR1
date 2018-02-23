package com.us.hotr.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

import com.us.hotr.R;

import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;


/**
 * Created by Mloong on 2017/9/12.
 */

public class PriceQueryDialogFragment extends BottomSheetDialogFragment {
    TextView tvDone;

    private BottomSheetBehavior mBehavior;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_price_query, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        tvDone = (TextView) view.findViewById(R.id.tv_done);

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        return dialog;
    }



    @Override
    public void onStart()
    {
        super.onStart();
        mBehavior.setState(STATE_EXPANDED);
    }

    public void doclick(View v)
    {
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

}
