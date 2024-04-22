package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
@Validated
public class AdminController {
    @Autowired
    private AdminService adminService;

    //@PostMapping("/login")

}
