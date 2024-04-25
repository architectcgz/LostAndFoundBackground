package com.example.lostandfoundbackground;

import org.jasypt.util.text.BasicTextEncryptor;

public class JasyptTest {
    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密需要的salt
        //这里使用了环境变量里的salt
        textEncryptor.setPassword("${JASYPT_ENCRYPTOR_PASSWORD:}");
        //要加密的数据 数据库的用户名和密码
        String username = textEncryptor.encrypt("47.97.116.255");
        String password = textEncryptor.encrypt("123456");
        System.out.println(username);
        System.out.println(password);
        //解密测试
        //String decryptUsername = textEncryptor.decrypt("pOW1EBquTAd3Awoa/riAwqnfv/dzjFOt");
        //System.out.println(decryptUsername);
    }
}
