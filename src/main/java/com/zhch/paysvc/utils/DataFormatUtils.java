package com.zhch.paysvc.utils;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ThinkPad on 2017/8/9.
 */
public class DataFormatUtils {

    private static final SimpleDateFormat DATE_FORMAT_NO_SPACE = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_NO_SPACE = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("MM/dd");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat HOUR_MINUTE_FORMAT = new SimpleDateFormat("HH:mm");
    private static final DecimalFormat decimalFormat = new DecimalFormat("######.00");


    public static String getTimeStringNoSpace(Date date) {
        if (date != null) {
            return DATE_FORMAT_NO_SPACE.format(date);
        } else {
            return null;
        }
    }

    public static String getTimeString(Date date) {
        if (date != null) {
            return DATE_FORMAT.format(date);
        } else {
            return null;
        }
    }

    public static Date parserDay(String str) throws Exception {
        return SIMPLE_DATE_FORMAT.parse(str);
    }

    public static Date simpleParser(String str) throws Exception {
        return SIMPLE_DATE_FORMAT_NO_SPACE.parse(str);
    }

    public static Date parser(String str) throws Exception {
        return DATE_FORMAT.parse(str);
    }

    public static Date noSpaceParser(String str) throws Exception {
        return DATE_FORMAT_NO_SPACE.parse(str);
    }


    public static String getSimpleTimeString(Date date) {
        if (date != null) {
            return SIMPLE_DATE_FORMAT.format(date);
        } else {
            return null;
        }
    }

    public static String getSimpleDayString(String dateStr) throws ParseException {
        if (dateStr != null) {
            Date date = SIMPLE_DATE_FORMAT.parse(dateStr);
            return DAY_FORMAT.format(date);
        } else {
            return null;
        }
    }

    public static Date dateTimeToDate(Date dateIn) throws ParseException {
        if (dateIn != null) {
            String dateStr = SIMPLE_DATE_FORMAT.format(dateIn);
            return SIMPLE_DATE_FORMAT.parse(dateStr);
        } else {
            return null;
        }
    }

    public static String getFormatMoney(Double money) {
        if (money != null) {
            String temp = decimalFormat.format(money);
            return temp;
        } else {
            return null;
        }
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static String transDayStrSpace(String str) throws Exception {
        Date date = SIMPLE_DATE_FORMAT_NO_SPACE.parse(str);
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public static String transDayStrSpace2(String str) throws Exception {
        Date date = DATE_FORMAT_NO_SPACE.parse(str);
        return DATE_FORMAT.format(date);
    }


    public static String getHourMinuteString(Date date) {
        if (date != null) {
            return HOUR_MINUTE_FORMAT.format(date);
        } else {
            return null;
        }
    }

    public static String getNoSpaceTimeString(String str){
        return str.replace("-","");
    }

//    public static String getWepayFormatAmount(String original){
//        String totalFee = Math.floor((Double.valueOf(original) * 100)) + "";
//        if (totalFee.lastIndexOf(".")>0){
//            totalFee = totalFee.substring(0,totalFee.lastIndexOf("."));
//        }
//        Assert.isTrue(Double.valueOf(original)*100==Double.valueOf(totalFee),"金额转换失败");
//        return totalFee;
//    }


    public static String getWepayFormatAmount(String original){
        BigDecimal bigDecimal = new BigDecimal(original);
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal temp = bigDecimal.multiply(hundred);
        int amount = temp.intValue();
        Assert.isTrue(temp.divide(hundred).doubleValue()== bigDecimal.doubleValue(),"金额转换失败");
        return amount+"";
    }

    public static String getAmountFormatWepayAmount(String original){
        BigDecimal bigDecimal = new BigDecimal(original);
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal temp = bigDecimal.divide(hundred);
        double amount = temp.doubleValue();
        Assert.isTrue(temp.multiply(hundred).doubleValue()== bigDecimal.doubleValue(),"金额转换失败");
        return amount+"";
    }

    public static String getSubtractAmount(String total,String subtractTarget){
        BigDecimal totalDecimal = new BigDecimal(total);
        BigDecimal subtractTargetDecimal = new BigDecimal(subtractTarget);
        BigDecimal temp = totalDecimal.subtract(subtractTargetDecimal);
        double amount = temp.doubleValue();
        return amount+"";
    }


    public static String parserWepayAmount(String original){
        BigDecimal bigDecimal = new BigDecimal(original);
        BigDecimal hundred = new BigDecimal(100);
        BigDecimal temp = bigDecimal.divide(hundred);
        double amount = temp.doubleValue();
        return amount+"";
    }
}
