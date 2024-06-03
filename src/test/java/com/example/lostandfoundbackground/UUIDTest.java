package com.example.lostandfoundbackground;


import cn.hutool.core.lang.UUID;

public class UUIDTest {
    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString(true));
        System.out.println(UUID.randomUUID().toString(false));
    }
}
