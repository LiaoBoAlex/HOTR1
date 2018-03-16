package com.us.hotr.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.R;

/**
 * Created by Mloong on 2017/8/29.
 */

public class SearchView extends LinearLayout implements View.OnClickListener  {

    private Context mContext;

    private EditText etInput;
    private ImageView imgDelete;
    private TextView tvCancel;
    private SearchViewListener mListener;


    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
        initViews();
    }

    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    private void initViews(){
        etInput = (EditText) findViewById(R.id.et_input);
        imgDelete = (ImageView) findViewById(R.id.img_delete);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener !=null)
                    mListener.onCancel();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);

            }
        });

        imgDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etInput.setText("");
                if(mListener !=null)
                    mListener.onRefreshAutoComplete("");
            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mListener !=null)
                    mListener.onRefreshAutoComplete(charSequence + "");
                if(charSequence.length()>0)
                    imgDelete.setVisibility(View.VISIBLE);
                else {
                    imgDelete.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etInput, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (mListener != null) {
                        mListener.onSearch(etInput.getText().toString().trim());
                    }
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
                }
                return true;
            }
        });
    }

    public String getEtInput(){
        return etInput.getText().toString().trim();
    }

    public void setEtInput(String text){
        if(etInput!=null)
            etInput.setText(text);
        if (mListener != null) {
            mListener.onSearch(etInput.getText().toString().trim());
        }
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
    }

    public void setEtInputEx(String text){
        if(etInput!=null)
            etInput.setText(text);
    }

    @Override
    public void onClick(View view) {

    }

    public interface SearchViewListener {
        void onRefreshAutoComplete(String text);
        void onSearch(String text);
        void onCancel();
    }
}
