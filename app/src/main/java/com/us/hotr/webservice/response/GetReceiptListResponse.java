package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.MassageReceipt;
import com.us.hotr.storage.bean.ProductReceipt;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/2/23.
 */

public class GetReceiptListResponse implements Serializable {
    private List<ProductReceipt> ymList;
    private List<MassageReceipt> amList;

    public List<ProductReceipt> getYmList() {
        return ymList;
    }

    public void setYmList(List<ProductReceipt> ymList) {
        this.ymList = ymList;
    }

    public List<MassageReceipt> getAmList() {
        return amList;
    }

    public void setAmList(List<MassageReceipt> amList) {
        this.amList = amList;
    }
}
