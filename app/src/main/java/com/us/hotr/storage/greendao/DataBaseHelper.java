package com.us.hotr.storage.greendao;

import android.content.Context;

import com.us.hotr.storage.HOTRSharePreference;
import com.us.hotr.storage.bean.SearchHistory;
import com.us.hotr.storage.bean.SearchTypeResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mloong on 2017/8/30.
 */

public class DataBaseHelper {
    private static DataBaseHelper instanse;
    private static Context mContext;

    public DataBaseHelper(Context context) {
        this.mContext = context;
    }

    public static DataBaseHelper getInstance(Context context) {
        if (null == instanse) {
            instanse = new DataBaseHelper(context);
        }
        return instanse;
    }

    public static List<String> getAllSearchHistory(){
        List<SearchHistory> shl = GreenDaoManager.getInstance(mContext).getSession().getSearchHistoryDao().loadAll();
        List<String> s = new ArrayList<>();
        if(shl != null && shl.size() > 0){
            for(int i=shl.size()-1;i>=Math.max(shl.size()-10, 0);i--)
                s.add(shl.get(i).getKeyword());
        }
        return s;
    }

    public static void insertSearchHistory(String s){
        SearchHistoryDao dao = GreenDaoManager.getInstance(mContext).getSession().getSearchHistoryDao();
        SearchHistory sh = new SearchHistory(s);
        dao.delete(sh);
        dao.insert(sh);

        List<SearchHistory> shl = dao.loadAll();
        if(shl != null && shl.size()>20){
            List<SearchHistory> shlnew = new ArrayList<>();
            for(int i=shl.size()-10;i<shl.size();i++){
                shlnew.add(shl.get(i));
            }
            dao.deleteAll();
            for(SearchHistory search:shlnew)
                dao.insert(search);
        }
    }

    public static void clearSearchHistory(){
        GreenDaoManager.getInstance(mContext).getSession().getSearchHistoryDao().deleteAll();
    }
}
