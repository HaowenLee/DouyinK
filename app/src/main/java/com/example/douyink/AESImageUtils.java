package com.example.douyink;

import com.blankj.utilcode.util.EncryptUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AESImageUtils {
    /* renamed from: iv */
    private static String f8964iv = "";
    private static String key = "wPK8CxWaOwPuVzgs";
    private static String transformation = "AES/ECB/PKCS7Padding";

    private static InputStream byte2Input(byte[] bArr) {
        return new ByteArrayInputStream(bArr);
    }

    private static byte[] input2byte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr, 0, 1024);
            if (read <= 0) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static InputStream decrypt(InputStream inputStream) throws IOException {
        byte[] input2byte = input2byte(inputStream);
        byte[] o = EncryptUtils.decryptAES(input2byte, key.getBytes(), transformation, null);
        if (o != null) {
            return byte2Input(o);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("===当前bytes size:");
        stringBuilder.append(input2byte.length);
        return byte2Input(input2byte);
    }

    public static byte[] decrypt(byte[] bArr) {
        return EncryptUtils.decryptAES(bArr, key.getBytes(), transformation, null);
    }

    public static String decrypt(String str) {
        String str2 = "";
        byte[] n = EncryptUtils.decryptAES(str.getBytes(), key.getBytes(), transformation, null);
        return n != null ? new String(n) : str2;
    }

    public static String encrypt(String str) {
        String str2 = "";
        if (transformation.equals("AES/ECB/NoPadding")) {
            while (str.getBytes().length % 16 != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(' ');
                str = stringBuilder.toString();
            }
        }
        byte[] k = EncryptUtils.encryptAES(str.getBytes(), key.getBytes(), transformation, null);
        return k != null ? new String(k) : str2;
    }

    public static byte[] encrypt(byte[] bArr) {
        return EncryptUtils.encryptAES(bArr, key.getBytes(), transformation, null);
    }
}