package com.example.lostandfoundbackground;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String res = passwordEncoder.encode("aaaaabbbbbcccccddddd");
        System.out.println(res);
        System.out.println(res.length());
    }
}
