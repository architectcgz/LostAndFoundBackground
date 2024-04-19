package com.example.lostandfoundbackground.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author archi
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserController userController;
}
