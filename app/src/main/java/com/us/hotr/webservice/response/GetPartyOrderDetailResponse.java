package com.us.hotr.webservice.response;

import com.us.hotr.storage.bean.PartyOrder;
import com.us.hotr.storage.bean.Ticket;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaobo on 2018/2/8.
 */

public class GetPartyOrderDetailResponse implements Serializable {
    private PartyOrder order;
    private List<Ticket> detail;
    private Payment payment;

    public PartyOrder getOrder() {
        return order;
    }

    public void setOrder(PartyOrder order) {
        this.order = order;
    }

    public List<Ticket> getDetail() {
        return detail;
    }

    public void setDetail(List<Ticket> detail) {
        this.detail = detail;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public class Payment{
        private long id;
        private String payment_sn;
        private String pay_time;
        private double payment_amount;
        private double order_price;
        private int payment_type;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getPayment_sn() {
            return payment_sn;
        }

        public void setPayment_sn(String payment_sn) {
            this.payment_sn = payment_sn;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public double getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(double payment_amount) {
            this.payment_amount = payment_amount;
        }

        public double getOrder_price() {
            return order_price;
        }

        public void setOrder_price(double order_price) {
            this.order_price = order_price;
        }

        public int getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(int payment_type) {
            this.payment_type = payment_type;
        }
    }
}
