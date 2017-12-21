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

public class CancelOrderDialogFragment extends BottomSheetDialogFragment {
    TextView tvCancel, tvDone, tvOption1, tvOption2, tvOption3, tvOption4, lastSelectedView;
    private BottomSheetBehavior mBehavior;

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((TextView)view).setTextColor(getResources().getColor(R.color.text_black));
            ((TextView)view).setBackgroundResource(R.color.cyan1);
            if(lastSelectedView!=null){
                lastSelectedView.setTextColor(getResources().getColor(R.color.text_grey2));
                lastSelectedView.setBackgroundResource(R.color.white);
            }
            lastSelectedView = (TextView) view;
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getActivity(), R.layout.dialog_cancel_order, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        tvDone = (TextView) view.findViewById(R.id.tv_done);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvOption1 = (TextView) view.findViewById(R.id.tv_option1);
        tvOption2 = (TextView) view.findViewById(R.id.tv_option2);
        tvOption3 = (TextView) view.findViewById(R.id.tv_option3);
        tvOption4 = (TextView) view.findViewById(R.id.tv_option4);

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        tvOption1.setOnClickListener(buttonOnClickListener);
        tvOption2.setOnClickListener(buttonOnClickListener);
        tvOption3.setOnClickListener(buttonOnClickListener);
        tvOption4.setOnClickListener(buttonOnClickListener);

        tvOption1.setTextColor(getResources().getColor(R.color.text_black));
        tvOption1.setBackgroundResource(R.color.cyan1);
        lastSelectedView = tvOption1;

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
