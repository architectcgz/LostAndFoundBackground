package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author archi
 */
@RestController
@RequestMapping("/lost")
public class LostItemController {
    @Autowired
    private LostItemService lostItemService;

}
