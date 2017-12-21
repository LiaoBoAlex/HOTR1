package com.lljjcoder.citypickerview.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lljjcoder.citypickerview.R;
import com.lljjcoder.citypickerview.widget.wheel.OnWheelChangedListener;
import com.lljjcoder.citypickerview.widget.wheel.WheelView;
import com.lljjcoder.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 省市区三级选择
 * 作者：liji on 2015/12/17 10:40
 * 邮箱：lijiwork@sina.com
 */
public class MyDatePicker implements CanShow, OnWheelChangedListener {

    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mViewYear;

    private WheelView mViewMonth;

    private WheelView mViewDay;

    private RelativeLayout mRelativeTitleBg;

    private TextView mTvOK;

    private TextView mTvTitle;

    private TextView mTvCancel;

    /**
     * 所有省
     */
    protected Integer[] mYears;

    protected Integer[] mMonths;

    protected Integer[] mDays;

    private int nowYear;
    private int nowMonth;
    private int nowDay;
//
//    /**
//     * key - 省 value - 市
//     */
//    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
//
//    /**
//     * key - 市 values - 区
//     */
//    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
//
//    /**
//     * key - 区 values - 邮编
//     */
//    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected int mCurrentYear;

    /**
     * 当前市的名称
     */
    protected int mCurrentMonth;

    /**
     * 当前区的名称
     */
    protected int mCurrentDay;

//    /**
//     * 当前区的邮政编码
//     */
//    protected String mCurrentZipCode = "";

    private OnCityItemClickListener listener;

    public interface OnCityItemClickListener {
        void onSelected(String... citySelected);
        void onCancel();
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;

    private int textSize = DEFAULT_TEXT_SIZE;

    /**
     * 滚轮显示的item个数
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;

    /**
     * 省滚轮是否循环滚动
     */
    private boolean isYearCyclic = true;

    /**
     * 市滚轮是否循环滚动
     */
    private boolean isMonthCyclic = true;

    /**
     * 区滚轮是否循环滚动
     */
    private boolean isDayCyclic = true;

    /**
     * item间距
     */
    private int padding = 5;


    /**
     * Color.BLACK
     */
    private int cancelTextColorStr = 0x000000;


    /**
     * Color.BLUE
     */
    private int confirmTextColorStr = 0x0000FF;

    /**
     * 标题背景颜色
     */
    private int titleBackgroundColorStr = 0xE9E9E9;
    /**
     * 标题颜色
     */
    private int titleTextColorStr = 0xE9E9E9;

    /**
     * 第一次默认的显示省份，一般配合定位，使用
     */
    private int defaultYear;

    /**
     * 第一次默认得显示城市，一般配合定位，使用
     */
    private int defaultMonth;

    /**
     * 第一次默认得显示，一般配合定位，使用
     */
    private int defaultDay;

//    /**
//     * 两级联动
//     */
//    private boolean showProvinceAndCity = false;

    /**
     * 标题
     */
    private String mTitle = "选择地区";

    /**
     * 设置popwindow的背景
     */
    private int backgroundPop = 0xa0000000;

    private MyDatePicker(Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.visibleItems = builder.visibleItems;
        this.isYearCyclic = builder.isYearCyclic;
        this.isMonthCyclic = builder.isMonthCyclic;
        this.isDayCyclic = builder.isDayCyclic;
        this.context = builder.mContext;
        this.padding = builder.padding;
        this.mTitle = builder.mTitle;
        this.titleBackgroundColorStr = builder.titleBackgroundColorStr;
        this.confirmTextColorStr = builder.confirmTextColorStr;
        this.cancelTextColorStr = builder.cancelTextColorStr;
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
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

        this.backgroundPop = builder.backgroundPop;
        this.titleTextColorStr = builder.titleTextColorStr;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_citypicker, null);

        mViewYear = (WheelView) popview.findViewById(R.id.id_province);
        mViewMonth = (WheelView) popview.findViewById(R.id.id_city);
        mViewDay = (WheelView) popview.findViewById(R.id.id_district);
        mRelativeTitleBg = (RelativeLayout) popview.findViewById(R.id.rl_title);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvTitle = (TextView) popview.findViewById(R.id.tv_title);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);


        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
//        popwindow.setBackgroundDrawable(new ColorDrawable(backgroundPop));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);


        /**
         * 设置标题背景颜色
         */
        mRelativeTitleBg.setBackgroundColor(this.titleBackgroundColorStr);

        /**
         * 设置标题
         */
        if (!TextUtils.isEmpty(this.mTitle)) {
            mTvTitle.setText(this.mTitle);
        }


        //设置确认按钮文字颜色
        mTvTitle.setTextColor(this.titleTextColorStr);


        //设置确认按钮文字颜色
        mTvOK.setTextColor(this.confirmTextColorStr);

        //设置取消按钮文字颜色
        mTvCancel.setTextColor(this.cancelTextColorStr);


//        //只显示省市两级联动
//        if (this.showProvinceAndCity) {
//            mViewDistrict.setVisibility(View.GONE);
//        } else {
//            mViewDistrict.setVisibility(View.VISIBLE);
//        }

        // 添加change事件
        mViewYear.addChangingListener(this);
        // 添加change事件
        mViewMonth.addChangingListener(this);
        // 添加change事件
        mViewDay.addChangingListener(this);
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
        /**
         * Default text color
         */
        public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

        /**
         * Default text size
         */
        public static final int DEFAULT_TEXT_SIZE = 18;

        // Text settings
        private int textColor = DEFAULT_TEXT_COLOR;

        private int textSize = DEFAULT_TEXT_SIZE;

        /**
         * 滚轮显示的item个数
         */
        private static final int DEF_VISIBLE_ITEMS = 5;

        // Count of visible items
        private int visibleItems = DEF_VISIBLE_ITEMS;

        /**
         * 省滚轮是否循环滚动
         */
        private boolean isYearCyclic = true;

        /**
         * 市滚轮是否循环滚动
         */
        private boolean isMonthCyclic = true;

        /**
         * 区滚轮是否循环滚动
         */
        private boolean isDayCyclic = true;

        private Context mContext;

        /**
         * item间距
         */
        private int padding = 5;


        /**
         * Color.BLACK
         */
        private int cancelTextColorStr = 0x000000;


        /**
         * Color.BLUE
         */
        private int confirmTextColorStr = 0x0000FF;

        /**
         * 标题背景颜色
         */
        private int titleBackgroundColorStr = 0xE9E9E9;

        /**
         * 标题颜色
         */
        private int titleTextColorStr = 0xE9E9E9;


        /**
         * 第一次默认的显示省份，一般配合定位，使用
         */
        private int defaultYear = -1;

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         */
        private int defaultMonth = -1;

        /**
         * 第一次默认得显示，一般配合定位，使用
         */
        private int defaultDay = 1;

        /**
         * 标题
         */
        private String mTitle = "选择地区";

//        /**
//         * 两级联动
//         */
//        private boolean showProvinceAndCity = false;

        /**
         * 设置popwindow的背景
         */
        private int backgroundPop = 0xa0000000;

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * 设置popwindow的背景
         *
         * @param backgroundPopColor
         * @return
         */
        public Builder backgroundPop(int backgroundPopColor) {
            this.backgroundPop = backgroundPopColor;
            return this;
        }

        /**
         * 设置标题背景颜色
         *
         * @param colorBg
         * @return
         */
        public Builder titleBackgroundColor(int colorBg) {
            this.titleBackgroundColorStr = colorBg;
            return this;
        }

        /**
         * 设置标题背景颜色
         *
         * @param titleTextColorStr
         * @return
         */
        public Builder titleTextColor(int titleTextColorStr) {
            this.titleTextColorStr = titleTextColorStr;
            return this;
        }


        /**
         * 设置标题
         *
         * @param mtitle
         * @return
         */
        public Builder title(String mtitle) {
            this.mTitle = mtitle;
            return this;
        }

//        /**
//         * 是否只显示省市两级联动
//         *
//         * @param flag
//         * @return
//         */
//        public Builder onlyShowProvinceAndCity(boolean flag) {
//            this.showProvinceAndCity = flag;
//            return this;
//        }

        /**
         * 第一次默认的显示省份，一般配合定位，使用
         *
         * @param defaultYear
         * @return
         */
        public Builder year(int defaultYear) {
            this.defaultYear = defaultYear;
            return this;
        }

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         *
         * @param defaultMonth
         * @return
         */
        public Builder month(int defaultMonth) {
            this.defaultMonth = defaultMonth;
            return this;
        }

        /**
         * 第一次默认地区显示，一般配合定位，使用
         *
         * @param defaultDay
         * @return
         */
        public Builder day(int defaultDay) {
            this.defaultDay = defaultDay;
            return this;
        }

        //        /**
        //         * 确认按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder confirTextColor(int color) {
        //            this.confirmTextColor = color;
        //            return this;
        //        }

        /**
         * 确认按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder confirTextColor(int color) {
            this.confirmTextColorStr = color;
            return this;
        }

        //        /**
        //         * 取消按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder cancelTextColor(int color) {
        //            this.cancelTextColor = color;
        //            return this;
        //        }

        /**
         * 取消按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder cancelTextColor(int color) {
            this.cancelTextColorStr = color;
            return this;
        }

        /**
         * item文字颜色
         *
         * @param textColor
         * @return
         */
        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * item文字大小
         *
         * @param textSize
         * @return
         */
        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 滚轮显示的item个数
         *
         * @param visibleItems
         * @return
         */
        public Builder visibleItemsCount(int visibleItems) {
            this.visibleItems = visibleItems;
            return this;
        }

        /**
         * 省滚轮是否循环滚动
         *
         * @param isYearCyclic
         * @return
         */
        public Builder yearCyclic(boolean isYearCyclic) {
            this.isYearCyclic = isYearCyclic;
            return this;
        }

        /**
         * 市滚轮是否循环滚动
         *
         * @param isMonthCyclic
         * @return
         */
        public Builder monthCyclic(boolean isMonthCyclic) {
            this.isMonthCyclic = isMonthCyclic;
            return this;
        }

        /**
         * 区滚轮是否循环滚动
         *
         * @param isDayCyclic
         * @return
         */
        public Builder dayCyclic(boolean isDayCyclic) {
            this.isDayCyclic = isDayCyclic;
            return this;
        }

        /**
         * item间距
         *
         * @param itemPadding
         * @return
         */
        public Builder itemPadding(int itemPadding) {
            this.padding = itemPadding;
            return this;
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
        mYears = new Integer[50];
        String[] mYearsData = new String[50];
        for(int i= 0;i<50;i++) {
            mYears[i] = nowYear + i;
            mYearsData[i] = mYears[i] + context.getString(R.string.year);
        }
        int yearDefaultIndex = -1;
        if (defaultYear>=0 && mYears.length > 0) {
            for (int i = 0; i < mYears.length; i++) {
                if (mYears[i]==defaultYear) {
                    yearDefaultIndex = i;
                    break;
                }
            }
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(context, mYearsData);
        mViewYear.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != yearDefaultIndex) {
            mViewYear.setCurrentItem(yearDefaultIndex);
        }
        // 设置可见条目数量
        mViewYear.setVisibleItems(visibleItems);
        mViewMonth.setVisibleItems(visibleItems);
        mViewDay.setVisibleItems(visibleItems);
        mViewYear.setCyclic(isYearCyclic);
        mViewMonth.setCyclic(isMonthCyclic);
        mViewDay.setCyclic(isDayCyclic);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);

        updateMonths();
        updateDays();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateDays() {
        int pCurrent = mViewMonth.getCurrentItem();
        mCurrentMonth = mMonths[pCurrent];
        Calendar mycal = new GregorianCalendar(mCurrentYear, mCurrentMonth-1, 1);
        int days = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String[] mDaysData;
        if(mCurrentYear == nowYear && mCurrentMonth==nowMonth){
            mDays = new Integer[days-nowDay+1];
            mDaysData = new String[days-nowDay+1];
            for (int i = 0; i < days-nowDay+1; i++) {
                mDays[i] = nowDay + i;
                mDaysData[i] = mDays[i] + context.getString(R.string.day);
            }
        }else {
            mDays = new Integer[days];
            mDaysData = new String[days];
            for (int i = 0; i < days; i++) {
                mDays[i] = i + 1;
                mDaysData[i] = mDays[i] + context.getString(R.string.day);
            }
        }
        int dayDefaultIndex = -1;
        if (defaultDay>=0 && mDays.length > 0 && defaultYear == mCurrentYear && defaultMonth == mCurrentMonth) {
            for (int i = 0; i < mDays.length; i++) {
                if (mDays[i]==defaultDay) {
                    dayDefaultIndex = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter districtWheel = new ArrayWheelAdapter<String>(context, mDaysData);
        // 设置可见条目数量
        districtWheel.setTextColor(textColor);
        districtWheel.setTextSize(textSize);
        mViewDay.setViewAdapter(districtWheel);
        if (-1 != dayDefaultIndex) {
            mViewDay.setCurrentItem(dayDefaultIndex);
            //获取默认设置的区
            mCurrentDay = defaultDay;
        } else {
            mViewDay.setCurrentItem(0);
            //获取第一个区名称
            mCurrentDay = mDays[0];

        }
        districtWheel.setPadding(padding);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateMonths() {
        int pCurrent = mViewYear.getCurrentItem();
        mCurrentYear = mYears[pCurrent];
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String[] mMonthData;
        if(mCurrentYear == year){
            mMonths = new Integer[12-nowMonth+1];
            mMonthData = new String[12-nowMonth+1];
            for (int i = 0; i < 12-nowMonth+1; i++) {
                mMonths[i] = nowMonth + i;
                mMonthData[i] = mMonths[i] + context.getString(R.string.month);
            }
        }else {
            mMonths = new Integer[12];
            mMonthData = new String[12];
            for (int i = 0; i < 12; i++) {
                mMonths[i] = i + 1;
                mMonthData[i] = mMonths[i] + context.getString(R.string.month);
            }
        }
        int cityDefaultIndex = -1;
        if (defaultMonth>=0 && mMonths.length > 0 && defaultYear == mCurrentYear) {
            for (int i = 0; i < mMonths.length; i++) {
                if (mMonths[i]==defaultMonth) {
                    cityDefaultIndex = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter cityWheel = new ArrayWheelAdapter<String>(context, mMonthData);
        // 设置可见条目数量
        cityWheel.setTextColor(textColor);
        cityWheel.setTextSize(textSize);
        mViewMonth.setViewAdapter(cityWheel);
        if (-1 != cityDefaultIndex) {
            mViewMonth.setCurrentItem(cityDefaultIndex);
        } else {
            mViewMonth.setCurrentItem(0);
        }

        cityWheel.setPadding(padding);
        updateDays();
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        if (!isShow()) {
            setUpData();
            setBackgroundAlpha(0.5f);
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            setBackgroundAlpha(1.0f);
            popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewYear) {

            updateMonths();
        } else if (wheel == mViewMonth) {
            updateDays();
        } else if (wheel == mViewDay) {
            mCurrentDay = mDays[newValue];
        }
    }
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }
}
