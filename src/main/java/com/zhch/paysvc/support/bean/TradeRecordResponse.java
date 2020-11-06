package com.zhch.paysvc.support.bean;


/**
 * Created by ThinkPad on 2017/11/27.
 */
public class TradeRecordResponse extends TradeResponse {

    private static final long serialVersionUID = -7653445945547125930L;

    private BillContainer billContainer;

    public BillContainer getBillContainer() {
        return billContainer;
    }

    public void setBillContainer(BillContainer billContainer) {
        this.billContainer = billContainer;
    }

}
