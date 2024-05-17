package com.example.lostandfoundbackground;

import com.example.lostandfoundbackground.config.security.jwt.JwtTokenProvider;

public class TokenTest {

    public static void main(String[] args) throws InterruptedException {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

        String testToken = jwtTokenProvider.generateToken("czf","ADMIN",1000L);
        System.out.println(jwtTokenProvider.getUserRole(testToken));
        System.out.println(jwtTokenProvider.getUserName(testToken));
    }
}
