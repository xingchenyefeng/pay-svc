package com.zhch.paysvc.utils;

import java.util.Random;
import java.util.UUID;

public class IdGenerator {

    public static String getFixLenthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    public static String getSixRandNum() {
        Random rad=new Random();
        String result  = rad.nextInt(1000000) +"";
        if(result.length()!=6){
            return getSixRandNum();
        }
        return result;
    }

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
}
