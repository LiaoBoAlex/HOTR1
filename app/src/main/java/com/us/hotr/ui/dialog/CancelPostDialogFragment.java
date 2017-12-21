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
 * Created by Mloong on 2017/9/22.
 */

public class CancelPostDialogFragment extends BottomSheetDialogFragment {
    TextView tvOption1, tvOption2, tvOption3, tvOption4;
    private BottomSheetBehavior mBehavior;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialog_draft, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        tvOption1 = (TextView) view.findViewById(R.id.tv_option1);
        tvOption2 = (TextView) view.findViewById(R.id.tv_option2);
        tvOption3 = (TextView) view.findViewById(R.id.tv_option3);
        tvOption4 = (TextView) view.findViewById(R.id.tv_option4);

        tvOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        tvOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                getActivity().finish();
            }
        });
        tvOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                getActivity().finish();
            }
        });
        tvOption4.setOnClickListener(new View.OnClickListener() {
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
