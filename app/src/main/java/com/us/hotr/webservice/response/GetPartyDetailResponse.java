package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.Party;
import com.us.hotr.storage.bean.Ticket;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2017/12/26.
 */

public class GetPartyDetailResponse implements Serializable {
    private int orderNum;
    private Party travel;
    private List<Ticket> ticket;
    private int is_collected;

    public int getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(int is_collected) {
        this.is_collected = is_collected;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public Party getTravel() {
        return travel;
    }

    public void setTravel(Party travel) {
        this.travel = travel;
    }

    public List<Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(List<Ticket> ticket) {
        this.ticket = ticket;
    }
}
