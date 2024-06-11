package com.example.lostandfoundbackground;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
    public static void main(String[] args) {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //$2a$10$Q7ZBtmXoq7y6I3sNZ.LkcOFUHtqEAw7q3NI0VyEUxYc36zL20Fedu

        //$2a$10$qW01DkmExFrQgRW9QDtSaOMcBhIukAqKwGWfDDecO9dtyVg/TId4e
        String res = passwordEncoder.encode("Xhj123Xhj!");
        System.out.println(passwordEncoder.matches("Xhj123Xhj!",
                "$2a$10$j5o3QDSd3tslnToqi.6vz.EXSKtyBp1DNjcw4pt4tbgG2Bb2thdTy"));
        System.out.println(res);
        System.out.println(res.length());
    }
}
