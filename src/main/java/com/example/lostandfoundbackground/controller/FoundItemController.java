package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author archi
 */
@RestController
@RequestMapping("/found")
public class FoundItemController {
    @Autowired
    private FoundItemService foundItemService;
}
