package com.zhch.paysvc.utils;



import com.zhch.paysvc.entity.Bill;
import com.zhch.paysvc.entity.BillDetail;
import com.zhch.paysvc.support.wepay.WepayField;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2017/9/20.
 */
public class BillFileReader {

    public static final String BILL_FILE_DIR_PATH = "bill/data/";
    private static final String DOWNLOAD_FILE_NAME = "downloadFileName";

    public static Bill analysisAlipayBill(String url, String sellerId, String channelCode, String billDate) throws Exception {
        Bill bill = new Bill();
        bill.setChannelCode(channelCode);
        String folder = System.getProperty("java.io.tmpdir") + "/";

        String newZip = folder + channelCode +"_"+ billDate+ ".zip";
        // 开始下载
        FileUtil.downloadNet(url, newZip);
        // 解压到指定目录
        FileUtil.unZip(newZip, folder);
        // 遍历文件 获取需要的汇整csv
        File[] fs = new File(folder).listFiles();

        for (File file : fs) {
            if (file.getAbsolutePath().contains(sellerId)) {
                if (file.getAbsolutePath().contains("汇总")) {
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader ir = new InputStreamReader(fis, "GBK");
                    LineNumberReader lineNumberReader = new LineNumberReader(ir);
                    ArrayList<String> tempList = new ArrayList<>();
                    String read = null;
                    while ((read = lineNumberReader.readLine()) != null) {
                        tempList.add(read);
                    }
                    for (int i = 0; i < tempList.size(); i++) {
                        if (i == (tempList.size() - 3)) {
                            bill.reSetProperties(tempList.get(i).split(","));
                        }
                    }
                    fis.close();
                    ir.close();
                    lineNumberReader.close();
                } else {
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader ir = new InputStreamReader(fis, "GBK");
                    LineNumberReader lineNumberReader = new LineNumberReader(ir);
                    ArrayList<String> tempList = new ArrayList<>();
                    String read = null;
                    while ((read = lineNumberReader.readLine()) != null) {
                        tempList.add(read);
                    }
                    for (int i = 0; i < tempList.size(); i++) {
                        if (i > 4 && i < tempList.size() - 4) {
                            BillDetail billDetail = new BillDetail(tempList.get(i).split(","));
                            billDetail.setAccountPeriod(billDate);
                            billDetail.setChannelCode(channelCode);
                            if (bill.getBillDetails() == null) {
                                bill.setBillDetails(new ArrayList<>());
                            }
                            bill.getBillDetails().add(billDetail);
                        }
                    }
                    fis.close();
                    ir.close();
                    lineNumberReader.close();
                }
                file.delete();
            }
        }

        return bill;
    }

    public static Bill readerWepayBillString(String responseBody, String sellerId, String channelCode,String billDate) {
        Bill bill = new Bill();
        String[] array = responseBody.split("\\r\\n");
        List<BillDetail> billDetails = new ArrayList<>();
        int transCount = 0;
        int refundCount = 0;
        for (int i = 1 ; i < array.length-2; i ++ ){
            String[] tempArray = array[i].replace("`","").split(",");
            BillDetail billDetail= new BillDetail();
            if (tempArray[9].equals(WepayField.SUCCESS)){
                billDetail.setTradeType("交易");
                billDetail.setTotalAmount(tempArray[12]);
                billDetail.setReceiptAmount(tempArray[12]);
                transCount = transCount+1;
            }else if (tempArray[9].equals(WepayField.REFUND)){
                billDetail.setTradeType("退款");
                billDetail.setTotalAmount("-"+tempArray[16]);
                billDetail.setReceiptAmount("-"+tempArray[16]);
                refundCount = refundCount+1;
            }
            billDetail.setAccountPeriod(billDate);
            billDetail.setStartTime(tempArray[0]);
            billDetail.setChannelCode(channelCode);
            billDetail.setChannelDiscount("0.00");
            billDetail.setEnvelopes("0.00");
            billDetail.setBuyerId(tempArray[7]);
            billDetail.setCardSpendingAmount("0.00");
            billDetail.setCoupon("0.00");
            billDetail.setServiceAmount("0.00");
            billDetail.setStoreDiscount("0.00");
            billDetail.setOutRequestNo(tempArray[15]);
            billDetail.setMark("");
            billDetail.setTreasure("0.00");
            billDetail.setTransNo(tempArray[5]);
            billDetail.setTerminalId(tempArray[4]);
            billDetail.setCouponName("");
            billDetail.setSubject(tempArray[20]);
            billDetail.setOutTransNo(tempArray[6]);
            billDetail.setOperator("");

            billDetail.setStoreEnvelopes("0.00");
            billDetail.setPayTime("");
            billDetail.setPayTime(tempArray[0]);
            billDetail.setPoint("0.00");
            billDetail.setStoreId("");
            billDetail.setStoreName("");
            billDetails.add(billDetail);
        }
        String totalRecord = array[array.length-1];
        totalRecord = totalRecord.replace("`","");
        String[] tempArray = totalRecord.split(",");
        bill.setChannelCode(channelCode);
        bill.setBillDetails(billDetails);
        BigDecimal b1 = new BigDecimal(tempArray[1]);
        BigDecimal b2 = new BigDecimal(tempArray[2]);
        bill.setAmount(b1.subtract(b2).doubleValue()+"");
        bill.setActualAmount(bill.getAmount());
        bill.setDiscount("0.00");
        bill.setMerchantDiscount("0.00");
        bill.setServiceCharge("0.00");
        bill.setCardConsumeAmount("0.00");
        bill.setStoreName("");
        bill.setStoreNum("合计");
        bill.setTransCount(transCount+"");
        bill.setRevenue("");
        bill.setRefundCount(refundCount+"");
        bill.setShareBenefit("0.00");
        return bill;
    }
}
