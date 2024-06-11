package com.example.lostandfoundbackground;

public class EnvTest {
    public static void main(String[] args) {
        System.out.println(System.getenv("ALIBABA_OSS_ACCESS_KEY_ID"));
        System.out.println(System.getenv("ALIBABA_OSS_ACCESS_KEY_SECRET"));
        System.out.println(System.getenv("ALIBABA_OSS_BUCKET_NAME"));
    }
}
