package com.us.hotr.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.hotr.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 省市区三级选择
 * 作者：liji on 2015/12/17 10:40
 * 邮箱：lijiwork@sina.com
 */
public class MyDatePicker implements NumberPickerView.OnValueChangeListener {

    private Context context;
    private PopupWindow popwindow;
    private View popview;
    private NumberPickerView mViewYear;
    private NumberPickerView mViewMonth;
    private NumberPickerView mViewDay;
    private RelativeLayout mRelativeTitleBg;
    private TextView mTvOK;
    private TextView mTvTitle;
    private TextView mTvCancel;

    protected Integer[] mYears;
    protected Integer[] mMonths;
    protected Integer[] mDays;
    private int nowYear;
    private int nowMonth;
    private int nowDay;

    protected int mCurrentYear;
    protected int mCurrentMonth;
    protected int mCurrentDay;

    private int defaultYear;
    private int defaultMonth;
    private int defaultDay;

    private boolean forward;
    private int limit;


    private OnCityItemClickListener listener;

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        if (picker == mViewYear) {
            updateMonths(false);
        } else if (picker == mViewMonth) {
            updateDays(false);
        } else if (picker == mViewDay) {
            mCurrentDay = mDays[newVal];
        }
    }

    public interface OnCityItemClickListener {
        void onSelected(String... citySelected);
        void onCancel();
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.listener = listener;
    }

    private MyDatePicker(Builder builder) {
        this.context = builder.mContext;
        final Calendar c = Calendar.getInstance();
        if(builder.defaultDay>=0)
            this.defaultDay = builder.defaultDay;
        else
            this.defaultDay = c.get(Calendar.DAY_OF_MONTH);
        if(builder.defaultMonth>=0)
            this.defaultMonth = builder.defaultMonth;
        else
            this.defaultMonth = c.get(Calendar.MONTH);
        if(builder.defaultYear>=0)
            this.defaultYear = builder.defaultYear;
        else
            this.defaultYear = c.get(Calendar.YEAR);
        this.forward = builder.forward;
        this.limit = builder.limit;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_citypicker, null);

        mViewYear = (NumberPickerView) popview.findViewById(R.id.id_province);
        mViewMonth = (NumberPickerView) popview.findViewById(R.id.id_city);
        mViewDay = (NumberPickerView) popview.findViewById(R.id.id_district);
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


        // 添加change事件
        mViewYear.setOnValueChangedListener(this);
        // 添加change事件
        mViewMonth.setOnValueChangedListener(this);
        // 添加change事件
        mViewDay.setOnValueChangedListener(this);
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
                listener.onSelected(mCurrentYear+"", mCurrentMonth+"", mCurrentDay+"");
                hide();
            }
        });

    }

    public static class Builder {

        private Context mContext;
        private int defaultYear = -1;
        private int defaultMonth = -1;
        private int defaultDay = -1;
        private boolean forward = true;
        private int limit = 50;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder year(int defaultYear) {
            this.defaultYear = defaultYear;
            return this;
        }

        public Builder month(int defaultMonth) {
            this.defaultMonth = defaultMonth;
            return this;
        }

        public Builder day(int defaultDay) {
            this.defaultDay = defaultDay;
            return this;
        }

        public Builder setForward(boolean forward){
            this.forward = forward;
            return  this;
        }

        public Builder setLimit(int limit){
            this.limit = limit;
            return  this;
        }

        public MyDatePicker build() {
            MyDatePicker myDatePicker = new MyDatePicker(this);
            return myDatePicker;
        }

    }

    private void setUpData() {
        final Calendar c = Calendar.getInstance();
        nowYear = c.get(Calendar.YEAR);
        nowMonth = c.get(Calendar.MONTH)+1;
        nowDay = c.get(Calendar.DAY_OF_MONTH);
        mYears = new Integer[limit];
        String[] mYearsData = new String[limit];
        if(forward) {
            for (int i = 0; i < limit; i++) {
                mYears[i] = nowYear + i;
                mYearsData[i] = mYears[i] + context.getString(R.string.year);
            }
        }else{
            for (int i = 0; i < limit; i++) {
                mYears[i] = nowYear - i;
                mYearsData[i] = mYears[i] + context.getString(R.string.year);
            }
        }
        int yearDefaultIndex = 0;
        if (defaultYear>=0 && mYears.length > 0) {
            for (int i = 0; i < mYears.length; i++) {
                if (mYears[i]==defaultYear) {
                    yearDefaultIndex = i;
                    break;
                }
            }
        }

        mViewYear.setDisplayedValuesAndPickedIndex(mYearsData, yearDefaultIndex, true);
        mCurrentYear = mYears[yearDefaultIndex];
        updateMonths(true);
        updateDays(true);
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateDays(boolean isInit) {
        if(!isInit) {
            int pCurrent = mViewMonth.getValue();
            mCurrentMonth = mMonths[pCurrent];
        }
        Calendar mycal = new GregorianCalendar(mCurrentYear, mCurrentMonth-1, 1);
        int days = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] mDaysData = null;
        if(mCurrentYear == nowYear && mCurrentMonth==nowMonth){
            if(forward) {
                mDays = new Integer[days - nowDay + 1];
                mDaysData = new String[days - nowDay + 1];
                for (int i = 0; i < days - nowDay + 1; i++) {
                    mDays[i] = nowDay + i;
                    mDaysData[i] = mDays[i] + context.getString(R.string.day);
                }
            }else{
                mDays = new Integer[nowDay];
                mDaysData = new String[nowDay];
                for (int i = 0; i < nowDay; i++) {
                    mDays[i] = i + 1;
                    mDaysData[i] = mDays[i] + context.getString(R.string.day);
                }
            }
        }else {
            mDays = new Integer[days];
            mDaysData = new String[days];
            for (int i = 0; i < days; i++) {
                mDays[i] = i + 1;
                mDaysData[i] = mDays[i] + context.getString(R.string.day);
            }
        }
        int dayDefaultIndex = 0;
        if (defaultDay>=0 && mDays.length > 0 && defaultYear >=0 && defaultMonth >=0 && isInit) {
            for (int i = 0; i < mDays.length; i++) {
                if (mDays[i]==defaultDay) {
                    dayDefaultIndex = i;
                    break;
                }
            }
        }

        mViewDay.setDisplayedValuesAndPickedIndex(mDaysData, dayDefaultIndex, true);
        mCurrentDay = mDays[dayDefaultIndex];

        int minValue = mViewDay.getMinValue();
        int oldMaxValue = mViewDay.getMaxValue();
        int oldSpan = oldMaxValue - minValue + 1;
        int newMaxValue = mDaysData.length - 1;
        int newSpan = newMaxValue - minValue + 1;
        if (newSpan > oldSpan) {
            mViewDay.setMaxValue(newMaxValue);
        } else {
            mViewDay.setMaxValue(newMaxValue);
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateMonths(boolean isInit) {
        if(!isInit) {
            int pCurrent = mViewYear.getValue();
            mCurrentYear = mYears[pCurrent];
        }
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String[] mMonthData;
        if(mCurrentYear == year){
            if(forward) {
                mMonths = new Integer[12 - nowMonth + 1];
                mMonthData = new String[12 - nowMonth + 1];
                for (int i = 0; i < 12 - nowMonth + 1; i++) {
                    mMonths[i] = nowMonth + i;
                    mMonthData[i] = mMonths[i] + context.getString(R.string.month);
                }
            }else{
                mMonths = new Integer[nowMonth];
                mMonthData = new String[nowMonth];
                for (int i = 0; i < nowMonth; i++) {
                    mMonths[i] = i + 1;
                    mMonthData[i] = mMonths[i] + context.getString(R.string.month);
                }
            }
        }else {
            mMonths = new Integer[12];
            mMonthData = new String[12];
            for (int i = 0; i < 12; i++) {
                mMonths[i] = i + 1;
                mMonthData[i] = mMonths[i] + context.getString(R.string.month);
            }
        }
        int cityDefaultIndex = 0;
        if (defaultMonth>=0 && mMonths.length > 0 && defaultYear >= 0 && isInit) {
            for (int i = 0; i < mMonths.length; i++) {
                if (mMonths[i]==defaultMonth+1) {
                    cityDefaultIndex = i;
                    break;
                }
            }
        }

        mViewMonth.setDisplayedValuesAndPickedIndex(mMonthData, cityDefaultIndex, true);
        mCurrentMonth = mMonths[cityDefaultIndex];

        int minValue = mViewMonth.getMinValue();
        int oldMaxValue = mViewMonth.getMaxValue();
        int oldSpan = oldMaxValue - minValue + 1;
        int newMaxValue = mMonthData.length - 1;
        int newSpan = newMaxValue - minValue + 1;
        if (newSpan > oldSpan) {
            mViewMonth.setMaxValue(newMaxValue);
        } else {
            mViewMonth.setMaxValue(newMaxValue);
        }
        updateDays(isInit);
    }

    public void show() {
        if (!isShow()) {
            setUpData();
            setBackgroundAlpha(0.5f);
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
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

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }
}
