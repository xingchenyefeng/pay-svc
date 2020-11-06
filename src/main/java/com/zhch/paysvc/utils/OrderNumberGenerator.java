package com.zhch.paysvc.utils;


/**
 *
 */
public class OrderNumberGenerator {

    public static String generatorOrderNum(String channelCode){
        StringBuffer id = new StringBuffer();
        String prefix = DataFormatUtils.getTimeStringNoSpace(Dates.now());
        id.append(prefix).append(channelCode).append(IdGenerator.getFixLenthString(6));
        return id.toString();
    }

}