package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.FoundItemDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.service.FoundItemService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author archi
 */
@RestController
@RequestMapping("/found")
public class FoundItemController {
    @Resource
    private FoundItemService foundItemService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/publish")
    public Result publishFoundInfo(@Validated @RequestBody FoundItemDTO foundItemDTO){
        return foundItemService.add(foundItemDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update")
    public Result updateFoundInfo(@Validated @RequestBody FoundItemDTO foundItemDTO){
        return foundItemService.update(foundItemDTO);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public Result searchFoundInfo(@RequestParam("title")String title){
        return foundItemService.searchByTitle(title);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public Result deleteLostInfo(@RequestParam("id")Long id){
        return foundItemService.delete(id);
    }

}
