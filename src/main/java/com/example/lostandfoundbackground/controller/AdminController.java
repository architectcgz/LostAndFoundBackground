package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;
}