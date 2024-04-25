package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.service.UserService;
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
    private UserService userService;
}
