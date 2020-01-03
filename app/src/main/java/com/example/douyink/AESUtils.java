package com.example.douyink;

import com.blankj.utilcode.util.EncryptUtils;

/**
 * ================================================
 * 作    者：Herve、Li
 * 创建日期：2020/1/3
 * 描    述：
 * 修订历史：
 * ================================================
 */
/* renamed from: com.niming.cartoon.utils.a */
public class AESUtils {

    /* renamed from: a */
    private static String f15078a = "";

    /* renamed from: b */
    private static String f15079b = "1s1z1GYRRNZRSJam";

    /* renamed from: c */
    private static String f15080c = "AES/ECB/PKCS7Padding";

    /* renamed from: d */
    private static String f15081d = "";

    /* renamed from: a */
    public static String m21238a(String str) {
        byte[] n = EncryptUtils.decryptBase64AES(str.getBytes(), f15079b.getBytes(), f15080c, (byte[]) null);
        if (n != null) {
            return new String(n);
        }
        return "";
    }

    /* renamed from: b */
    public static String m21239b(String str) {
        if (f15080c.equals("AES/ECB/NoPadding")) {
            while (str.getBytes().length % 16 != 0) {
                str = str + ' ';
            }
        }
        byte[] k = EncryptUtils.encryptAES(str.getBytes(), f15079b.getBytes(), f15080c, (byte[]) null);
        if (k != null) {
            return new String(k);
        }
        return "";
    }
}
