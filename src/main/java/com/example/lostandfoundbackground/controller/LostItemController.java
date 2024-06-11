package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.LostItemDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.service.LostItemService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author archi
 */
@RestController
@RequestMapping("/lost")
public class LostItemController {
    @Resource
    private LostItemService lostItemService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/publish")
    public Result publishLostInfo(@Validated @RequestBody LostItemDTO lostItemDTO){
        return lostItemService.add(lostItemDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update")
    public Result updateLostInfo(@Validated @RequestBody LostItemDTO lostItemDTO){
        return lostItemService.update(lostItemDTO);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public Result searchLostInfo(@RequestParam("title")String title){
        return lostItemService.searchByTitle(title);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public Result deleteLostInfo(@RequestParam("id")Long id){
        return lostItemService.delete(id);
    }

    @GetMapping("/item-detail")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Result getLostInfoDetailById(@RequestParam("id")Long id){
        return lostItemService.getDetailById(id);
    }

    @GetMapping("/list")
    public Result previewList(@RequestParam int pageNum, @RequestParam int pageSize){
        return lostItemService.previewList(pageNum,pageSize);
    }

}
