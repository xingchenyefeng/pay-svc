package com.zhch.paysvc.support.bean;

import com.zhch.paysvc.entity.Bill;

public class BillContainer {
        private Bill bill;
        private String payType;
        private String state;
        private String stateDesc;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStateDesc() {
            return stateDesc;
        }

        public void setStateDesc(String stateDesc) {
            this.stateDesc = stateDesc;
        }

        public Bill getBill() {
            return bill;
        }

        public void setBill(Bill bill) {
            this.bill = bill;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }
    }