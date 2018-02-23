package com.us.hotr.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
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
import com.us.hotr.storage.bean.CityModel;
import com.us.hotr.storage.bean.DistrictModel;
import com.us.hotr.storage.bean.ProvinceModel;
import com.us.hotr.util.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * 省市区三级选择
 * 作者：liji on 2015/12/17 10:40
 * 邮箱：lijiwork@sina.com
 */
public class CityPicker implements NumberPickerView.OnValueChangeListener {

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

    protected String[] mProvinceDatas;
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName = "";
    protected String mCurrentZipCode = "";

    private OnCityItemClickListener listener;

    @Override
    public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
        if (picker == mViewProvince) {

            updateCities();
        } else if (picker == mViewCity) {
            updateAreas();
        } else if (picker == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newVal];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
        }
    }

    public interface OnCityItemClickListener {
        void onSelected(String... citySelected);
        void onCancel();
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.listener = listener;
    }
    private String defaultProvinceName;
    private String defaultCityName;
    private String defaultDistrict;
    private boolean showProvinceAndCity = false;

    private CityPicker(Builder builder) {
        this.context = builder.mContext;

        this.defaultDistrict = builder.defaultDistrict;
        this.defaultCityName = builder.defaultCityName;
        this.defaultProvinceName = builder.defaultProvinceName;

        this.showProvinceAndCity = builder.showProvinceAndCity;

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


        if (this.showProvinceAndCity) {
            mViewDistrict.setVisibility(View.GONE);
        } else {
            mViewDistrict.setVisibility(View.VISIBLE);
        }

        //初始化城市数据
        initProvinceDatas(context);

        // 添加change事件
        mViewProvince.setOnValueChangedListener(this);
        // 添加change事件
        mViewCity.setOnValueChangedListener(this);
        // 添加change事件
        mViewDistrict.setOnValueChangedListener(this);
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
                if (showProvinceAndCity) {
                    listener.onSelected(mCurrentProviceName, mCurrentCityName, "", mCurrentZipCode);
                } else {
                    listener.onSelected(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName, mCurrentZipCode);
                }
                hide();
            }
        });

    }

    public static class Builder {

        private Context mContext;
        private String defaultProvinceName;
        private String defaultCityName;
        private String defaultDistrict;
        private boolean showProvinceAndCity = false;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder onlyShowProvinceAndCity(boolean flag) {
            this.showProvinceAndCity = flag;
            return this;
        }

        /**
         * 第一次默认的显示省份，一般配合定位，使用
         *
         * @param defaultProvinceName
         * @return
         */
        public Builder province(String defaultProvinceName) {
            this.defaultProvinceName = defaultProvinceName;
            return this;
        }

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         *
         * @param defaultCityName
         * @return
         */
        public Builder city(String defaultCityName) {
            this.defaultCityName = defaultCityName;
            return this;
        }

        /**
         * 第一次默认地区显示，一般配合定位，使用
         *
         * @param defaultDistrict
         * @return
         */
        public Builder district(String defaultDistrict) {
            this.defaultDistrict = defaultDistrict;
            return this;
        }


        public CityPicker build() {
            CityPicker cityPicker = new CityPicker(this);
            return cityPicker;
        }

    }

    private void setUpData() {
        int provinceDefault = 0;
        if (!TextUtils.isEmpty(defaultProvinceName) && mProvinceDatas.length > 0) {
            for (int i = 0; i < mProvinceDatas.length; i++) {
                if (mProvinceDatas[i].contains(defaultProvinceName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        mViewProvince.setDisplayedValuesAndPickedIndex(mProvinceDatas, provinceDefault, true);
        updateCities();
        updateAreas();
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas(Context context) {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(),
                                districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
//                        JLogUtils.D("zipcode: " + mProvinceDatas[i] + cityNames[j] +
//                                districtList.get(k).getName() + "  " + districtList.get(k).getZipcode());
                        mZipcodeDatasMap.put(mProvinceDatas[i] + cityNames[j] +
                                        districtList.get(k).getName(),
                                districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getValue();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }

        int districtDefault = 0;
        if (!TextUtils.isEmpty(defaultDistrict) && areas.length > 0) {
            for (int i = 0; i < areas.length; i++) {
                if (areas[i].contains(defaultDistrict)) {
                    districtDefault = i;
                    break;
                }
            }
        }

        mViewDistrict.setDisplayedValuesAndPickedIndex(areas, districtDefault, true);
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[districtDefault];
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
        int minValue = mViewDistrict.getMinValue();
        int oldMaxValue = mViewDistrict.getMaxValue();
        int oldSpan = oldMaxValue - minValue + 1;
        int newMaxValue = areas.length - 1;
        int newSpan = newMaxValue - minValue + 1;
        if (newSpan > oldSpan) {
            mViewDistrict.setMaxValue(newMaxValue);
        } else {
            mViewDistrict.setMaxValue(newMaxValue);
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getValue();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }

        int cityDefault = 0;
        if (!TextUtils.isEmpty(defaultCityName) && cities.length > 0) {
            for (int i = 0; i < cities.length; i++) {
                if (cities[i].contains(defaultCityName)) {
                    cityDefault = i;
                    break;
                }
            }
        }
        mViewCity.setDisplayedValuesAndPickedIndex(cities, cityDefault, true);
        int minValue = mViewCity.getMinValue();
        int oldMaxValue = mViewCity.getMaxValue();
        int oldSpan = oldMaxValue - minValue + 1;
        int newMaxValue = cities.length - 1;
        int newSpan = newMaxValue - minValue + 1;
        if (newSpan > oldSpan) {
            mViewCity.setMaxValue(newMaxValue);
        } else {
            mViewCity.setMaxValue(newMaxValue);
        }
        updateAreas();
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
            setBackgroundAlpha(1.0f);
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
