package com.us.hotr.customview;

/**
 * Created by Mloong on 2017/9/26.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.hotr.R;

/**
 * 省市区三级选择
 * 作者：liji on 2015/12/17 10:40
 * 邮箱：lijiwork@sina.com
 */
public class ItemPicker implements NumberPickerView.OnValueChangeListener {

    private Context context;
    private PopupWindow popwindow;
    private View popview;
    private NumberPickerView mViewProvince;
    private NumberPickerView mViewCity;
    private NumberPickerView mViewDistrict;
    private RelativeLayout mRelativeTitleBg;
    private TextView mTvOK;
    private TextView mTvTitle;
    private TextView mTvCancel;

    protected String[] mItemDatas;
    protected String mCurrentItemName;
    protected int mCurrentItemPosition;
    private OnItemClickListener listener;
    private String defaultItemName;

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        if (picker == mViewProvince) {
            mCurrentItemName = mItemDatas[newVal];
            mCurrentItemPosition = newVal;
        }
    }

    public interface OnItemClickListener {
        void onSelected(String itemSelected, int position);

        void onCancel();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private ItemPicker(Builder builder) {
        this.context = builder.mContext;
        this.defaultItemName = builder.defaultItemName;
        this.mItemDatas = builder.mItemDatas;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_citypicker, null);

        mViewProvince = (NumberPickerView) popview.findViewById(R.id.id_province);
        mViewCity = (NumberPickerView) popview.findViewById(R.id.id_city);
        mViewDistrict = (NumberPickerView) popview.findViewById(R.id.id_district);
        mRelativeTitleBg = (RelativeLayout) popview.findViewById(R.id.rl_title);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvTitle = (TextView) popview.findViewById(R.id.tv_title);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);

        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popwindow.setBackgroundDrawable(new BitmapDrawable());
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(true);
        popwindow.setFocusable(true);

        popwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });

        mViewDistrict.setVisibility(View.GONE);
        mViewCity.setVisibility(View.GONE);

        // 添加change事件
        mViewProvince.setOnValueChangedListener(this);

        // 添加onclick事件
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                hide();
            }
        });
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(mCurrentItemName, mCurrentItemPosition);
                hide();
            }
        });

    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public static class Builder {
        private Context mContext;
        private String defaultItemName;
        private String[] mItemDatas;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setData(String[] mItemDatas) {
            this.mItemDatas = mItemDatas;
            return this;
        }

        public Builder defalutItem(String defaultItemName) {
            this.defaultItemName = defaultItemName;
            return this;
        }

        public ItemPicker build() {
            ItemPicker itemPicker = new ItemPicker(this);
            return itemPicker;
        }

    }

    private void setUpData() {
        int provinceDefault = 0;
        mCurrentItemName = mItemDatas[0];
        mCurrentItemPosition = 0;
        if (!TextUtils.isEmpty(defaultItemName) && mItemDatas.length > 0) {
            for (int i = 0; i < mItemDatas.length; i++) {
                if (mItemDatas[i].contains(defaultItemName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        mViewProvince.setDisplayedValuesAndPickedIndex(mItemDatas, provinceDefault, true);
        mCurrentItemName = mItemDatas[provinceDefault];
        mCurrentItemPosition = provinceDefault;
    }

    public void show() {
        if (!isShow()) {
            setUpData();
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
            setBackgroundAlpha(0.5f);
        }
    }

    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }

    public boolean isShow() {
        return popwindow.isShowing();
    }
}
