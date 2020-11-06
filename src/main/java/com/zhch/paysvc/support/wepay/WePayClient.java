package com.zhch.paysvc.support.wepay;


import cn.hutool.system.SystemUtil;
import com.zhch.paysvc.config.PayTypeEnum;
import com.zhch.paysvc.entity.Bill;
import com.zhch.paysvc.utils.BillFileReader;
import com.zhch.paysvc.utils.DataFormatUtils;
import com.zhch.paysvc.utils.WePayConstants;
import com.zhch.paysvc.utils.WePayUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/11/20.
 */
public class WePayClient {


    private final String appId;
    private final String sellerId;
    private final String appKey;
    private final String sysServiceProviderId;
    private final InputStream certStream;
    private final String notifyUrl;
    private final String limitPay;
    private final String createIP;
    private final int connectTimeoutMs = 3000;
    private final int readTimeoutMs = 6000;
    private final PoolingHttpClientConnectionManager connectionManager;

    public String getAppId() {
        return appId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public InputStream getCertStream() {
        return certStream;
    }

    public WePayClient(String appId, String sellerId, String appKey, String notifyUrl, String limitPay, String sysServiceProviderId, InputStream certStream) throws Exception {
        this.appId = appId;
        this.sellerId = sellerId;
        this.appKey = appKey;
        this.notifyUrl = notifyUrl;
        this.limitPay = limitPay;
        this.certStream = certStream;
        this.sysServiceProviderId = sysServiceProviderId;
        // 证书
        char[] password = this.getSellerId().toCharArray();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(certStream, password);
        // 实例化密钥库 & 初始化密钥工厂
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password);

        // 创建 SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1"},
                null,
                new DefaultHostnameVerifier());

        /**
         * 重点关注此段代码，避免高并发情况下管道数据异常关闭造成报错
         */
        this.connectionManager = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build());
        this.createIP = InetAddress.getLocalHost().getHostAddress();
    }

    private String requestOnce(final String domain, String urlSuffix, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).build();
        String url = "https://" + domain + urlSuffix;
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);
        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + this.getSellerId());  // TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
        httpPost.setEntity(postEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = EntityUtils.toString(httpEntity, "UTF-8");
        EntityUtils.consume(httpEntity);
        return result;
    }


    public Map<String, String> qrPay(String tradeNo, String subject, String totalAmount, String timeStart, String terminalId, String transIntroduce, String timeoutExpress) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        // 业务必需参数
        params.put(WepayField.BODY, subject);
        params.put(WepayField.OUT_TRADE_NO, tradeNo);
        params.put(WepayField.TOTAL_FEE, totalAmount);
        params.put(WepayField.SPBILL_CREATE_IP, this.createIP);
        params.put(WepayField.NOTIFY_URL, this.notifyUrl);
        params.put(WepayField.FEE_TYPE, "CNY");
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        Date timeStartDate = DataFormatUtils.noSpaceParser(timeStart);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeStartDate);
        calendar.add(Calendar.MINUTE, Integer.parseInt(timeoutExpress));
        params.put(WepayField.TIME_START, timeStart);
        params.put(WepayField.TIME_EXPIRE, DataFormatUtils.getTimeStringNoSpace(calendar.getTime()));
        params.put(WepayField.TRADE_TYPE, TradeType.NATIVE.type());
        // 业务可选参数
        params.put(WepayField.DEVICE_INFO, terminalId);
        if (StringUtils.hasLength(sysServiceProviderId)) {
            params.put(WepayField.SUB_MCH_ID, sysServiceProviderId);
        }
        params.put(WepayField.ATTACH, subject);
        params.put(WepayField.LIMIT_PAY, this.limitPay);
        params.put(WepayField.PRODUCT_ID, tradeNo);
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.UNIFIEDORDER_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }

    public Map<String, String> queryByTradeNo(String tradeNo) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        params.put(WepayField.OUT_TRADE_NO, tradeNo);
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.ORDERQUERY_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }

    public Map<String, String> refundApply(String tradeNo, String totalAmount, String refundDesc) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        params.put(WepayField.OUT_TRADE_NO, tradeNo);
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.OUT_REFUND_NO, tradeNo);
        params.put(WepayField.TOTAL_FEE, totalAmount);
        params.put(WepayField.REFUND_FEE, totalAmount);
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.REFUND_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }


    public Map<String, String> closeTradeByTradeNo(String tradeNo) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        params.put(WepayField.OUT_TRADE_NO, tradeNo);
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.CLOSEORDER_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }

    public Map<String, String> tradePay(String tradeNo, String subject, String totalAmount, String timeStart, String terminalId, String transIntroduce, String authCode) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        // 业务必需参数
        params.put(WepayField.BODY, subject);
        params.put(WepayField.OUT_TRADE_NO, tradeNo);
        params.put(WepayField.TOTAL_FEE, totalAmount);
        params.put(WepayField.SPBILL_CREATE_IP, this.createIP);
        params.put(WepayField.FEE_TYPE, "CNY");
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.TIME_START, timeStart);
        // 业务可选参数
        if (StringUtils.hasLength(sysServiceProviderId)) {
            params.put(WepayField.SUB_MCH_ID, sysServiceProviderId);
        }
        params.put(WepayField.DEVICE_INFO, terminalId);
        params.put(WepayField.ATTACH, subject);
        params.put(WepayField.LIMIT_PAY, this.limitPay);
        params.put(WepayField.AUTH_CODE, authCode);
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.MICROPAY_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }

    public Map<String, Object> billDownload(String billDate, String sellerId, String channelCode) throws Exception {
        Map<String, Object> resMap = null;
        Map<String, String> params = new HashMap<>();
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());

        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.BILL_DATE, DataFormatUtils.getNoSpaceTimeString(billDate));
        params.put(WepayField.BILL_TYPE, WepayField.ALL);
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.DOWNLOADBILL_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        if (responseBody.startsWith("<xml>")) {
            Map<String, String> temp = WePayUtil.xmlToMap(responseBody);
            resMap = new HashMap<>(temp);
        } else {
            resMap = new HashMap<>();
            resMap.put(WepayField.RETURN_CODE, WepayField.SUCCESS);
            resMap.put(WepayField.RETURN_MSG, "账单下载成功");
            Bill bill = BillFileReader.readerWepayBillString(responseBody, sellerId, channelCode, billDate);
            bill.setPayType(PayTypeEnum.WEPAY.getPayType());
            resMap.put("bill", bill);
        }
        return resMap;
    }

    public String rsaSign(String signContent) throws Exception {
        String sign = WePayUtil.HMACSHA256(signContent, this.appKey);
        return sign;
    }

    public Map<String, String> refundPartApply(String tradeNo, String totalAmount, String refundAmount, String outRequestNo, String refundDesc) throws Exception {
        Map<String, String> params = new HashMap<>(16);
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        params.put(WepayField.OUT_TRADE_NO, tradeNo);
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.TOTAL_FEE, totalAmount);
        params.put(WepayField.REFUND_FEE, refundAmount);
        params.put(WepayField.OUT_REFUND_NO, outRequestNo);
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.REFUND_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }

    public Map<String, String> queryByTransactionId(String transactionId) throws Exception {
        Map<String, String> params = new HashMap<>(16);
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        params.put(WepayField.TRANSACTION_ID, transactionId);
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.ORDERQUERY_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }

    public Map<String, String> refundPartApplyByTransactionId(String transactionId, String totalAmount, String refundAmount, String outRequestNo, String refundDesc) throws Exception {
        Map<String, String> params = new HashMap<>(16);
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        params.put(WepayField.TRANSACTION_ID, transactionId);
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.TOTAL_FEE, totalAmount);
        params.put(WepayField.REFUND_FEE, refundAmount);
        params.put(WepayField.OUT_REFUND_NO, outRequestNo);
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.REFUND_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }

    public Map<String, String> queryRefundBillByTransactionId(String transactionId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        params.put(WepayField.TRANSACTION_ID, transactionId);
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.REFUNDQUERY_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }

    public Map<String, String> queryRefundBillByTradeNo(String tradeNo) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(WepayField.APP_ID, this.getAppId());
        params.put(WepayField.MCH_ID, this.getSellerId());
        params.put(WepayField.OUT_TRADE_NO, tradeNo);
        params.put(WepayField.NONCE_STR, WePayUtil.generateNonceStr());
        params.put(WepayField.SIGN_TYPE, WePayConstants.HMACSHA256);
        String sign = WePayUtil.generateSignature(params, this.appKey, WePayConstants.SignType.HMACSHA256);
        params.put(WepayField.SIGN, sign);
        String requestBody = WePayUtil.mapToXml(params);
        String responseBody = requestOnce(WePayConstants.DOMAIN_API, WePayConstants.REFUNDQUERY_URL_SUFFIX, requestBody, connectTimeoutMs, readTimeoutMs, true);
        Map<String, String> resMap = WePayUtil.xmlToMap(responseBody);
        if (resMap.get(WepayField.RETURN_CODE).equals(WepayField.SUCCESS)) {
            boolean flag = WePayUtil.isSignatureValid(resMap, this.appKey, WePayConstants.SignType.HMACSHA256);
            Assert.isTrue(flag, "验签失败,请核对支付服务地址.");
        }
        return resMap;
    }
}
