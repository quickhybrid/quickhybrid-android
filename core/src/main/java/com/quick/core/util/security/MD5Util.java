package com.quick.core.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dailichun on 2017/12/6.
 * 比如明文是yanzi1225627,得到MD5散列后的字符串是:14F2AE15259E2C276A095E7394DA0CA9但不能由后面一大串倒推出yanzi1225627。
 * 可以用于下载文件校验文件是否中途被篡改。
 *
 * 注意：这不是一个加密算法，是散列
 */

public class MD5Util {
    /**
     * SHA1加密，不可逆
     *
     * @param password
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    public static String authPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(password.getBytes());
            return bytes2Hex(md.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * 转为16进制
     *
     * @param bts
     * @return
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    /**
     * MD5加密
     *
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest;
        StringBuilder md5StrBuff = new StringBuilder();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }
}
