package com.us.hotr.customview;

/**
 * Created by Mloong on 2017/9/26.
 */

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

import com.lljjcoder.citypickerview.widget.CanShow;
import com.lljjcoder.citypickerview.widget.wheel.OnWheelChangedListener;
import com.lljjcoder.citypickerview.widget.wheel.WheelView;
import com.lljjcoder.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;

/**
 * 省市区三级选择
 * 作者：liji on 2015/12/17 10:40
 * 邮箱：lijiwork@sina.com
 */
public class ItemPicker implements CanShow, OnWheelChangedListener {

    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mViewProvince;

    private WheelView mViewCity;

    private WheelView mViewDistrict;

    private RelativeLayout mRelativeTitleBg;

    private TextView mTvOK;

    private TextView mTvTitle;

    private TextView mTvCancel;

    /**
     * 所有省
     */
    protected String[] mItemDatas;

    /**
     * 当前省的名称
     */
    protected String mCurrentItemName;
    protected int mCurrentItemPosition;


    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onSelected(String itemSelected, int position);

        void onCancel();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0x585858;

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
    private boolean isItemCyclic = false;


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
    private String defaultItemName;

    /**
     * 标题
     */
    private String mTitle = "";

    /**
     * 设置popwindow的背景
     */
    private int backgroundPop = 0xa0000000;

    private ItemPicker(Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.visibleItems = builder.visibleItems;
        this.isItemCyclic = builder.isItemCyclic;
        this.context = builder.mContext;
        this.padding = builder.padding;
        this.mTitle = builder.mTitle;
        this.titleBackgroundColorStr = builder.titleBackgroundColorStr;
        this.confirmTextColorStr = builder.confirmTextColorStr;
        this.cancelTextColorStr = builder.cancelTextColorStr;

        this.defaultItemName = builder.defaultItemName;

        this.backgroundPop = builder.backgroundPop;
        this.titleTextColorStr = builder.titleTextColorStr;
        this.mItemDatas = builder.mItemDatas;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(com.lljjcoder.citypickerview.R.layout.pop_citypicker, null);

        mViewProvince = (WheelView) popview.findViewById(com.lljjcoder.citypickerview.R.id.id_province);
        mViewCity = (WheelView) popview.findViewById(com.lljjcoder.citypickerview.R.id.id_city);
        mViewDistrict = (WheelView) popview.findViewById(com.lljjcoder.citypickerview.R.id.id_district);
        mRelativeTitleBg = (RelativeLayout) popview.findViewById(com.lljjcoder.citypickerview.R.id.rl_title);
        mTvOK = (TextView) popview.findViewById(com.lljjcoder.citypickerview.R.id.tv_confirm);
        mTvTitle = (TextView) popview.findViewById(com.lljjcoder.citypickerview.R.id.tv_title);
        mTvCancel = (TextView) popview.findViewById(com.lljjcoder.citypickerview.R.id.tv_cancel);


        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        popwindow.setBackgroundDrawable(new ColorDrawable(backgroundPop));
        popwindow.setAnimationStyle(com.lljjcoder.citypickerview.R.style.AnimBottom);
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


        mViewDistrict.setVisibility(View.GONE);
        mViewCity.setVisibility(View.GONE);

        // 添加change事件
        mViewProvince.addChangingListener(this);

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
        /**
         * Default text color
         */
        public static final int DEFAULT_TEXT_COLOR = 0x585858;

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
        private boolean isItemCyclic = true;


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
        private String defaultItemName;

        /**
         * 标题
         */
        private String mTitle;

        /**
         * 设置popwindow的背景
         */
        private int backgroundPop = 0xa0000000;

        private String[] mItemDatas;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setData(String[] mItemDatas) {
            this.mItemDatas = mItemDatas;
            return this;
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


        /**
         * 第一次默认的显示省份，一般配合定位，使用
         *
         * @param defaultItemName
         * @return
         */
        public Builder defalutItem(String defaultItemName) {
            this.defaultItemName = defaultItemName;
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
         * @param isItemCyclic
         * @return
         */
        public Builder itemCyclic(boolean isItemCyclic) {
            this.isItemCyclic = isItemCyclic;
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

        public ItemPicker build() {
            ItemPicker itemPicker = new ItemPicker(this);
            return itemPicker;
        }

    }

    private void setUpData() {
        int provinceDefault = -1;
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
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(context, mItemDatas);
        mViewProvince.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
            mCurrentItemName = mItemDatas[provinceDefault];
            mCurrentItemPosition = provinceDefault;
        }
        // 设置可见条目数量
        mViewProvince.setVisibleItems(visibleItems);
        mViewCity.setVisibleItems(visibleItems);
        mViewDistrict.setVisibleItems(visibleItems);
        mViewProvince.setCyclic(isItemCyclic);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);
    }


    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        if (!isShow()) {
            setUpData();
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
            setBackgroundAlpha(0.5f);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
            setBackgroundAlpha(1.0f);
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            mCurrentItemName = mItemDatas[newValue];
            mCurrentItemPosition = newValue;
        }
    }


}
