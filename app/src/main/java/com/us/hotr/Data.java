package com.us.hotr;

import com.us.hotr.storage.bean.BeautyItem;
import com.us.hotr.storage.bean.SearchHistory;
import com.us.hotr.storage.bean.SearchTypeResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mloong on 2017/8/28.
 */

public class Data {


    public static List<SearchHistory> getSearchHistory(){
        List<SearchHistory> searchHistoryList = new ArrayList<>();
        SearchHistory s = new SearchHistory("香薰精油");
        searchHistoryList.add(s);
        s = new SearchHistory("SPA按摩");
        searchHistoryList.add(s);
        s = new SearchHistory("超级无敌大帅哥来伺候你");
        searchHistoryList.add(s);
        s = new SearchHistory("水光针");
        searchHistoryList.add(s);
        return searchHistoryList;
    }

    public static List<String> getSearchPopular(){
        List<String> list = new ArrayList<String>(Arrays.asList("香薰精油", "SPA按摩", "超级无敌大帅哥来伺候你", "水光针"));
        return list;
    }

    public static List<String> getSearchHint(){
        List<String> list = new ArrayList<String>(Arrays.asList("陈水舟", "水氧活肤", "水", "水光针"));
        return list;
    }

    public static List<SearchTypeResult> getSearchTypeResult(){
        List<SearchTypeResult> searchTypeResultList = new ArrayList<>();
        SearchTypeResult s = new SearchTypeResult("医生", "1", 222);
        searchTypeResultList.add(s);
        s = new SearchTypeResult("案例", "1", 345);
        searchTypeResultList.add(s);
        s = new SearchTypeResult("医院", "1", 45);
        searchTypeResultList.add(s);
        s = new SearchTypeResult("医美项目", "1", 1236);
        searchTypeResultList.add(s);
        s = new SearchTypeResult("用户", "1", 1236);
        searchTypeResultList.add(s);
        s = new SearchTypeResult("按摩项目", "1", 345);
        searchTypeResultList.add(s);
        s = new SearchTypeResult("技师", "1", 45);
        searchTypeResultList.add(s);
        s = new SearchTypeResult("SPA", "1", 1236);
        searchTypeResultList.add(s);
        s = new SearchTypeResult("帖子", "1", 1236);
        searchTypeResultList.add(s);
        s = new SearchTypeResult("派对", "1", 1236);
        searchTypeResultList.add(s);
        return searchTypeResultList;
    }


    public static List<BeautyItem> getPopularPostItems(){
        List<BeautyItem> items = new ArrayList<>();
//        BeautyItem item = new BeautyItem();
//        item.setData("banner_data");
//        item.setId(Constants.BANNER_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("subject_data");
//        item.setId(Constants.MODULE_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("subject_data");
//        item.setId(Constants.GROUP_ID);
//        items.add(item);

//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.POST_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("news_data");
//        item.setId(Constants.POST_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("news_data");
//        item.setId(Constants.POST_ID);
//        items.add(item);

        return items;

    }


    public static List<BeautyItem> getMassageItems(){
        List<BeautyItem> items = new ArrayList<>();
//        BeautyItem item = new BeautyItem();
//        item = new BeautyItem();
//        item.setData("news_data");
//        item.setId(Constants.AD1_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("subject_data");
//        item.setId(Constants.MODULE_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.DIVIDER_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.INTERVIEW_LIST_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.TITLE_IMGE_ID);
//        items.add(item);

//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.MASSEUR_ID);
//        items.add(item);

//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.MASSEUR_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.MASSEUR_ID);
//        items.add(item);

//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.MASSEUR_ID);
//        items.add(item);

//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.DIVIDER_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.TITLE_IMGE_ID);
//        items.add(item);

//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.SPA_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.SPA_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.SPA_ID);
//        items.add(item);
//
//        item = new BeautyItem();
//        item.setData("divider_data");
//        item.setId(Constants.SPA_ID);
//        items.add(item);



        return items;

    }
}
