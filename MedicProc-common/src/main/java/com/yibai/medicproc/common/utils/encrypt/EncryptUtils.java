package com.yibai.medicproc.common.utils.encrypt;


import java.util.UUID;

public class EncryptUtils extends CodingUtil {

    private static final String secretKey = "www.fit2cloud.co";
    private static final String iv = "1234567890123456";


    public static Object aesEncrypt(Object o) {
        if (o == null) {
            return null;
        }
        return aesEncrypt(o.toString(), secretKey, iv);
    }

    public static Object aesDecrypt(Object o) {
        if (o == null) {
            return null;
        }
        return aesDecrypt(o.toString(), secretKey, iv);
    }


    public static Object md5Encrypt(Object o) {
        if (o == null) {
            return null;
        }
        return md5(o.toString());
    }

    public static void main(String[] args) throws Exception {
        String accessKey = "<your access key>";
        String secretKey = "your secret key";
        System.out.println(aesEncrypt(accessKey + "|" + UUID.randomUUID() + "|" + System.currentTimeMillis(), secretKey, accessKey));
    }
}
