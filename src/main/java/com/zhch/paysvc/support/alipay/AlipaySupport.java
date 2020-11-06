package com.zhch.paysvc.support.alipay;

import com.alipay.api.response.*;
import com.zhch.paysvc.support.PaySupport;


/**
 *
 * @author lumos
 * @date 2017/11/9
 */
public interface AlipaySupport extends PaySupport {


    /**
     * 扫码支付
     *
     * @param outTradeNo
     * @param barCode
     * @param authCode
     * @param subject
     * @param totalAmount
     * @return
     */
    AlipayTradePayResponse tradePay(String outTradeNo, String barCode, String authCode, String subject, String totalAmount);

    /**
     * 统一收单交易撤销接口
     *
     * @param tradeNo
     * @return
     */
    AlipayTradeCancelResponse tradeCancel(String tradeNo);



}
