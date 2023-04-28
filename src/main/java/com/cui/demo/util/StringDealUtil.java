package com.cui.demo.util;

import org.springframework.util.DigestUtils;

public class StringDealUtil {

    /**
     * 去除首位的占位符
     * @param str      要处理的字符串
     * @param trimStr  要去除的字符串
     * @param pos      left-左,right-右,all-左右都删除
     * @return
     */
    public static String trimStr(String str, String trimStr, String pos){
        int length = trimStr.length();
        int leftSubPos = 0;
        int rightSubPos = str.length();
        if(pos.equals("left") || pos.equals("all")){
            //处理左边的字符
             int leftPos = str.indexOf(trimStr);
             if(leftPos == 0){
                 leftSubPos = length;
             }
        }

        if(pos.equals("right") || pos.equals("all")){
            //处理右边的字符
            int rightPos = str.lastIndexOf(trimStr);
            if(rightPos == (str.length() - length)){
                rightSubPos = rightPos;
            }
        }
        return str.substring(leftSubPos, rightSubPos);
    }

    /**
     * 对字符的md5加密
     * @param str
     * @return
     */
    public static String md5String(String str){
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
}
